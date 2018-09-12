package com.yoloo.server.objectify.repository;

import com.googlecode.objectify.Key;
import org.springframework.data.repository.NoRepositoryBean;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

/**
 * Objectify Repository.
 * <p>
 * Extends {@link AsyncSaveRepository}, providing synchronous methods to save and load entities using Objectify.
 */
@NoRepositoryBean
interface SaveRepository extends AsyncSaveRepository {
  /**
   * Save the given entity.
   *
   * @param entity The entity.
   * @return the entity.
   */
  @Nonnull
  default <E> Key<E> save(final E entity) {
    return saveAsync(entity).get();
  }

  /**
   * Save the given entities.
   *
   * @param entities The entities.
   * @return the list of saved entities.
   */
  @Nonnull
  default <E> Map<Key<E>, E> save(final Collection<E> entities) {
    return saveAsync(entities).get();
  }

  /**
   * Save the given entities.
   *
   * @param entities The entities.
   * @return the list of saved entities.
   */
  @Nonnull
  @SuppressWarnings("unchecked")
  default <E> Map<Key<E>, E> save(E... entities) {
    return saveAsync(entities).get();
  }
}
