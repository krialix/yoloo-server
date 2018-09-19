package com.yoloo.server.objectify;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.cmd.DeleteType;
import com.googlecode.objectify.cmd.Deleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * A Deleter that forwards to {@code ofy().delete()}, but can be augmented via subclassing to do
 * custom processing on the keys to be deleted prior to their deletion.
 */
abstract class AugmentedDeleter implements Deleter {
  private final Deleter delegate = ofy().delete();

  /** Extension method to allow this Deleter to do extra work prior to the actual delete. */
  protected abstract void handleDeletion(Iterable<Key<?>> keys);

  private void handleDeletionStream(Stream<?> entityStream) {
    //noinspection UnstableApiUsage
    handleDeletion(entityStream.map(Key::create).collect(toImmutableList()));
  }

  @Override
  public Result<Void> entities(Iterable<?> entities) {
    //noinspection UnstableApiUsage
    handleDeletionStream(Streams.stream(entities));
    return delegate.entities(entities);
  }

  @Override
  public Result<Void> entities(Object... entities) {
    handleDeletionStream(Arrays.stream(entities));
    return delegate.entities(entities);
  }

  @Override
  public Result<Void> entity(Object entity) {
    handleDeletionStream(Stream.of(entity));
    return delegate.entity(entity);
  }

  @Override
  public Result<Void> key(Key<?> key) {
    handleDeletion(Collections.singletonList(key));
    return delegate.keys(key);
  }

  @Override
  public Result<Void> keys(Iterable<? extends Key<?>> keys) {
    // Magic to convert the type Iterable<? extends Key<?>> (a family of types which allows for
    // homogeneous iterables of a fixed Key<T> type, e.g. List<Key<Lock>>, and is convenient for
    // callers) into the type Iterable<Key<?>> (a concrete type of heterogeneous keys, which is
    // convenient for users).
    handleDeletion(ImmutableList.copyOf(keys));
    return delegate.keys(keys);
  }

  @Override
  public Result<Void> keys(Key<?>... keys) {
    handleDeletion(Arrays.asList(keys));
    return delegate.keys(keys);
  }

  /** Augmenting this gets ugly; you can always just use keys(Key.create(...)) instead. */
  @Override
  public DeleteType type(Class<?> clazz) {
    throw new UnsupportedOperationException();
  }
}
