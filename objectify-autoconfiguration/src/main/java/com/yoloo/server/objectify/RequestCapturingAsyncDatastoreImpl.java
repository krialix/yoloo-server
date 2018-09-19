package com.yoloo.server.objectify;

import com.google.cloud.datastore.*;
import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.impl.AsyncDatastore;
import com.googlecode.objectify.impl.AsyncTransaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static java.util.Collections.synchronizedList;

class RequestCapturingAsyncDatastoreImpl implements AsyncDatastore {

  private static List<List<Key>> reads = synchronizedList(new ArrayList<>());

  // Each outer lists represents Datastore operations, with inner lists representing the keys or
  // entities involved in that operation. We use static lists because we care about overall calls to
  // Datastore, not calls via a specific instance of the service.
  private static List<List<Key>> deletes = synchronizedList(new ArrayList<>());
  private static List<List<FullEntity<?>>> puts = synchronizedList(new ArrayList<>());

  private final AsyncDatastore delegate;

  RequestCapturingAsyncDatastoreImpl(AsyncDatastore delegate) {
    this.delegate = delegate;
  }

  public static List<List<Key>> getReads() {
    return reads;
  }

  public static List<List<Key>> getDeletes() {
    return deletes;
  }

  public static List<List<FullEntity<?>>> getPuts() {
    return puts;
  }

  @Override
  public AsyncTransaction newTransaction(Runnable afterCommit) {
    return delegate.newTransaction(afterCommit);
  }

  @Override
  public Future<Map<Key, Entity>> get(Collection<Key> keys, ReadOption... options) {
    reads.add(ImmutableList.copyOf(keys));
    return delegate.get(keys, options);
  }

  @Override
  public <T> QueryResults<T> run(Query<T> query) {
    return delegate.run(query);
  }

  @Override
  public Future<Void> delete(Iterable<Key> keys) {
    deletes.add(ImmutableList.copyOf(keys));
    return delegate.delete(keys);
  }

  @Override
  public Future<List<Key>> put(Iterable<? extends FullEntity<?>> entities) {
    puts.add(ImmutableList.copyOf(entities));
    return delegate.put(entities);
  }
}
