package com.yoloo.server.objectify.repository;

import com.googlecode.objectify.Key;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collection;

/**
 * Objectify repository for deleting entities.
 */
@NoRepositoryBean
public interface DeleteRepository extends AsyncDeleteRepository {
  /**
   * Delete the given entity.
   *
   * @param entity The entity.
   */
  default <E> void delete(E entity) {
    deleteAsync(entity).run();
  }

  /**
   * Delete a set of entities.
   *
   * @param entities The entities.
   */
  default <E> void delete(Collection<E> entities) {
    deleteAsync(entities).run();
  }

  /**
   * Delete a set of entities.
   *
   * @param entities The entities.
   */
  @SuppressWarnings("unchecked")
  default <E> void delete(E... entities) {
    deleteAsync(entities).run();
  }

  /**
   * Delete the entity with the given key.
   *
   * @param key Key of the entity to delete.
   */
  default <E> void deleteByKey(Key<E> key) {
    deleteByKeyAsync(key).run();
  }

  /**
   * Delete the entities with the given keys.
   *
   * @param keys Keys of the entities to delete.
   */
  default <E> void deleteByKey(Collection<Key<E>> keys) {
    deleteByKeyAsync(keys).run();
  }

  /**
   * Delete the entities with the given keys.
   *
   * @param keys Keys of the entities to delete.
   */
  @SuppressWarnings("unchecked")
  default <E> void deleteByKey(Key<E>... keys) {
    deleteByKeyAsync(keys).run();
  }
}
