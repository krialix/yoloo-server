package com.yoloo.server.objectify.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;

/**
 * Objectify repository for asynchronously deleting entities.
 * Each call returns a hook to the asynchronous operation that can be used to complete the operation at a later time. The operation
 * will eventually complete (or fail) regardless of whether the hook is used or not, it simply provides a mechanism for callers to
 * optionally wait for the operation to complete (i.e. treat the operation as synchronous) if required.
 */
@NoRepositoryBean
public interface AsyncDeleteRepository extends ObjectifyAware, Repository {
  /**
   * Delete the given entity asynchronously.
   *
   * @param entity The entity to delete.
   * @return Callback that can be used to complete the delete operation later.
   */
  @Nonnull
  default <E> Runnable deleteAsync(E entity) {
    final Result<Void> deleteOperation = ofy().delete().entity(entity);
    return deleteOperation::now;
  }

  /**
   * Delete the given entities asynchronously.
   *
   * @param entities The entities to delete.
   * @return Callback that can be used to complete the delete operation later.
   */
  @Nonnull
  default <E> Runnable deleteAsync(Collection<E> entities) {
    final Result<Void> deleteOperation = ofy().delete().entities(entities);
    return deleteOperation::now;
  }

  /**
   * Delete the given entities asynchronously.
   *
   * @param entities The entities to delete.
   * @return Callback that can be used to complete the delete operation later.
   */
  @Nonnull
  @SuppressWarnings("unchecked")
  default <E> Runnable deleteAsync(E... entities) {
    return deleteAsync(Arrays.asList(entities));
  }

  /**
   * Delete the entity with the given key asynchronously.
   *
   * @param key Key of the entity to delete.
   * @return Callback that can be used to complete the delete operation later.
   */
  @Nonnull
  default <E> Runnable deleteByKeyAsync(Key<E> key) {
    Result<Void> deleteOperation = ofy().delete().key(key);
    return deleteOperation::now;
  }

  /**
   * Delete the entities with the given keys asynchronously.
   *
   * @param keys Keys of the entities to delete.
   * @return Callback that can be used to complete the delete operation later.
   */
  @Nonnull
  default <E> Runnable deleteByKeyAsync(Collection<Key<E>> keys) {
    Result<Void> deleteOperation = ofy().delete().entities(keys);
    return deleteOperation::now;
  }

  /**
   * Delete the entities with the given keys asynchronously.
   *
   * @param keys Keys of the entities to delete.
   * @return Callback that can be used to complete the delete operation later.
   */
  @Nonnull
  @SuppressWarnings("unchecked")
  default <E> Runnable deleteByKeyAsync(Key<E>... keys) {
    return deleteByKeyAsync(Arrays.asList(keys));
  }
}
