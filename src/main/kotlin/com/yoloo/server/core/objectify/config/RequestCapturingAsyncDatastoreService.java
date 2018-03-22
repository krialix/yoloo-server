package com.yoloo.server.core.objectify.config;

import com.google.appengine.api.datastore.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static java.util.Collections.synchronizedList;

/**
 * A proxy for {@link AsyncDatastoreService} that exposes call counts.
 */
public class RequestCapturingAsyncDatastoreService implements AsyncDatastoreService {

  private static List<List<Key>> reads = synchronizedList(new ArrayList<>());

  // Each outer lists represents Datastore operations, with inner lists representing the keys or
  // entities involved in that operation. We use static lists because we care about overall calls to
  // Datastore, not calls via a specific instance of the service.
  private static List<List<Key>> deletes = synchronizedList(new ArrayList<>());
  private static List<List<Entity>> puts = synchronizedList(new ArrayList<>());

  private final AsyncDatastoreService delegate;

  RequestCapturingAsyncDatastoreService(AsyncDatastoreService delegate) {
    this.delegate = delegate;
  }

  public static List<List<Key>> getReads() {
    return reads;
  }

  public static List<List<Key>> getDeletes() {
    return deletes;
  }

  public static List<List<Entity>> getPuts() {
    return puts;
  }

  @Override
  public Collection<Transaction> getActiveTransactions() {
    return delegate.getActiveTransactions();
  }

  @Override
  public Transaction getCurrentTransaction() {
    return delegate.getCurrentTransaction();
  }

  @Override
  public Transaction getCurrentTransaction(Transaction transaction) {
    return delegate.getCurrentTransaction(transaction);
  }

  @Override
  public PreparedQuery prepare(Query query) {
    return delegate.prepare(query);
  }

  @Override
  public PreparedQuery prepare(Transaction transaction, Query query) {
    return delegate.prepare(transaction, query);
  }

  @Override
  public Future<KeyRange> allocateIds(String kind, long num) {
    return delegate.allocateIds(kind, num);
  }

  @Override
  public Future<KeyRange> allocateIds(Key parent, String kind, long num) {
    return delegate.allocateIds(parent, kind, num);
  }

  @Override
  public Future<Transaction> beginTransaction() {
    return delegate.beginTransaction();
  }

  @Override
  public Future<Transaction> beginTransaction(TransactionOptions transaction) {
    return delegate.beginTransaction(transaction);
  }

  @Override
  public Future<Void> delete(Key... keys) {
    deletes.add(ImmutableList.copyOf(keys));
    return delegate.delete(keys);
  }

  @Override
  public Future<Void> delete(Iterable<Key> keys) {
    deletes.add(ImmutableList.copyOf(keys));
    return delegate.delete(keys);
  }

  @Override
  public Future<Void> delete(Transaction transaction, Key... keys) {
    deletes.add(ImmutableList.copyOf(keys));
    return delegate.delete(transaction, keys);
  }

  @Override
  public Future<Void> delete(Transaction transaction, Iterable<Key> keys) {
    deletes.add(ImmutableList.copyOf(keys));
    return delegate.delete(transaction, keys);
  }

  @Override
  public Future<Entity> get(Key key) {
    reads.add(ImmutableList.of(key));
    return delegate.get(key);
  }

  @Override
  public Future<Map<Key, Entity>> get(Iterable<Key> keys) {
    reads.add(ImmutableList.copyOf(keys));
    return delegate.get(keys);
  }

  @Override
  public Future<Entity> get(Transaction transaction, Key key) {
    reads.add(ImmutableList.of(key));
    return delegate.get(transaction, key);
  }

  @Override
  public Future<Map<Key, Entity>> get(Transaction transaction, Iterable<Key> keys) {
    reads.add(ImmutableList.copyOf(keys));
    return delegate.get(transaction, keys);
  }

  @Override
  public Future<DatastoreAttributes> getDatastoreAttributes() {
    return delegate.getDatastoreAttributes();
  }

  @Override
  public Future<Map<Index, Index.IndexState>> getIndexes() {
    return delegate.getIndexes();
  }

  @Override
  public Future<Key> put(Entity entity) {
    puts.add(ImmutableList.of(entity));
    return delegate.put(entity);
  }

  @Override
  public Future<List<Key>> put(Iterable<Entity> entities) {
    puts.add(ImmutableList.copyOf(entities));
    return delegate.put(entities);
  }

  @Override
  public Future<Key> put(Transaction transaction, Entity entity) {
    puts.add(ImmutableList.of(entity));
    return delegate.put(transaction, entity);
  }

  @Override
  public Future<List<Key>> put(Transaction transaction, Iterable<Entity> entities) {
    puts.add(ImmutableList.copyOf(entities));
    return delegate.put(transaction, entities);
  }
}
