package com.yoloo.server.objectify;

import com.google.cloud.datastore.FullEntity;
import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.cmd.Saver;

import java.util.Arrays;
import java.util.Map;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * A Saver that forwards to {@code ofy().save()}, but can be augmented via subclassing to do custom
 * processing on the entities to be saved prior to their saving.
 */
abstract class AugmentedSaver implements Saver {
  private final Saver delegate = ofy().save();

  /** Extension method to allow this Saver to do extra work prior to the actual save. */
  protected abstract void handleSave(Iterable<?> entities);

  @Override
  public <E> Result<Map<Key<E>, E>> entities(Iterable<E> entities) {
    handleSave(entities);
    return delegate.entities(entities);
  }

  @Override
  @SafeVarargs
  public final <E> Result<Map<Key<E>, E>> entities(E... entities) {
    handleSave(Arrays.asList(entities));
    return delegate.entities(entities);
  }

  @Override
  public <E> Result<Key<E>> entity(E entity) {
    handleSave(ImmutableList.of(entity));
    return delegate.entity(entity);
  }

  @Override
  public FullEntity toEntity(Object pojo) {
    // No call to the extension method, since toEntity() doesn't do any actual saving.
    return delegate.toEntity(pojo);
  }
}
