package com.yoloo.server.objectify.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Generic Objectify repository for asynchronously saving entities.
 * Each call returns a hook to the asynchronous operation that can be used to complete the operation at a later time. The operation
 * will eventually complete (or fail) regardless of whether the hook is used or not, it simply provides a mechanism for callers to
 * optionally wait for the operation to complete (i.e. treat the operation as synchronous) if required.
 */
@NoRepositoryBean
public interface AsyncSaveRepository extends ObjectifyAware, Repository {

  /**
   * Put an entity asynchronously asynchronously.
   *
   * @param entity The entity to save.
   * @return Supplier that can be used to return the entity key later.
   */
  @Nonnull
  default <E> Supplier<Key<E>> saveAsync(final E entity) {
    final Result<Key<E>> saveOperation = ofy().save().entity(entity);
    return saveOperation::now;
  }

  /**
   * Save the given entities asynchronously.
   *
   * @param entities Collection of entities to save.
   * @return Supplier that can be used to return the collection of saved entities later.
   */
  @Nonnull
  default <E> Supplier<Map<Key<E>, E>> saveAsync(final Collection<E> entities) {
    final Result<Map<Key<E>, E>> saveOperation = ofy().save().entities(entities);
    return saveOperation::now;
  }

  /**
   * Save the given entities asynchronously.
   *
   * @param entities Collection of entities to save.
   * @return Supplier that can be used to return the collection of saved entities later.
   */
  @Nonnull
  @SuppressWarnings("unchecked")
  default <E> Supplier<Map<Key<E>, E>> saveAsync(final E... entities) {
    return saveAsync(Arrays.asList(entities));
  }
}