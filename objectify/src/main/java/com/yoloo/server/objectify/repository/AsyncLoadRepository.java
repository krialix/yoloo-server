package com.yoloo.server.objectify.repository;

import com.google.cloud.datastore.EntityQuery;
import com.google.common.base.Strings;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Loader;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Objectify repository for loading entities.
 */
@NoRepositoryBean
public interface AsyncLoadRepository<E> extends ObjectifyAware, EntityManager<E>, Repository {
  /**
   * Get the entity with the given key.
   *
   * @param key The key.
   * @return The entity or an empty {@link Optional} if none exists.
   */
  @Nonnull
  default Supplier<Optional<E>> findByKeyAsync(Key<E> key) {
    LoadResult<E> loadResult = ofy().load().key(key);
    return () -> Optional.ofNullable(loadResult.now());
  }

  /**
   * Find an entity by its web-safe key string.
   *
   * @param webSafeString Entity string.
   * @return The entity or an empty {@link Optional} if none exists.
   */
  @Nonnull
  @SuppressWarnings("unchecked")
  default Supplier<Optional<E>> findByWebSafeKeyAsync(String webSafeString) {
    return findByKeyAsync(Key.create(webSafeString));
  }

  @Nonnull
  default Map<Key<E>, E> findByKeysAsync(Collection<Key<E>> keys) {
    return ofy().load().keys(keys);
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  default Map<Key<E>, E> findByKeysAsync(Key<? extends E>... keys) {
    return findByKeysAsync(Arrays.asList((Key<E>[]) keys));
  }

  @Nonnull
  default Map<Key<E>, E> findByWebSafeKeysAsync(Collection<String> webSafeStrings) {
    List<Key<E>> keys = webSafeStrings.stream().map(Key::<E>create).collect(Collectors.toList());
    return findByKeysAsync(keys);
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  default Map<Key<E>, E> findByWebSafeKeysAsync(String... webSafeStrings) {
    return findByWebSafeKeysAsync(Arrays.asList(webSafeStrings));
  }

  default void findAll(EntityQuery query) {
    Loader loader = ofy().load();
    if (Strings.isNullOrEmpty(query.getKind())) {
      LoadType<Object> kind = loader.kind(query.getKind());
    }
  }
}
