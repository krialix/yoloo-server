package com.yoloo.server.util;

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

public class RequestCapturingAsyncDatastore implements AsyncDatastore {

  private static final List<List<Key>> READS = synchronizedList(new ArrayList<>());

  // Each outer lists represents Datastore operations, with inner lists representing the keys or
  // entities involved in that operation. We use static lists because we care about overall calls to
  // Datastore, not calls via a specific instance of the service.
  private static final List<List<Key>> DELETES = synchronizedList(new ArrayList<>());
  private static final List<List<FullEntity<?>>> PUTS = synchronizedList(new ArrayList<>());

  private final AsyncDatastore delegate;

  public RequestCapturingAsyncDatastore(AsyncDatastore delegate) {
    this.delegate = delegate;
  }

  public static List<List<Key>> getReads() {
    return READS;
  }

  public static List<List<Key>> getDeletes() {
    return DELETES;
  }

  public static List<List<FullEntity<?>>> getPuts() {
    return PUTS;
  }

  @Override
  public AsyncTransaction newTransaction(Runnable afterCommit) {
    return delegate.newTransaction(afterCommit);
  }

  @Override
  public Future<Map<Key, Entity>> get(Collection<Key> keys, ReadOption... options) {
    READS.add(ImmutableList.copyOf(keys));
    return delegate.get(keys, options);
  }

  @Override
  public <T> QueryResults<T> run(Query<T> query) {
    return delegate.run(query);
  }

  @Override
  public Future<Void> delete(Iterable<Key> keys) {
    DELETES.add(ImmutableList.copyOf(keys));
    return delegate.delete(keys);
  }

  @Override
  public Future<List<Key>> put(Iterable<? extends FullEntity<?>> entities) {
    PUTS.add(ImmutableList.copyOf(entities));
    return delegate.put(entities);
  }
}
