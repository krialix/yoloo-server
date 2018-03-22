package com.yoloo.server.core.objectify;

import org.springframework.util.ReflectionUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic {@link EntityMetadata} implementation.
 */
public class EntityMetadataImpl implements EntityMetadata {
  private final ObjectifyProxy objectify;

  private final Map<Class<?>, Field> idFieldCache;
  private final Map<Class<?>, Class<?>> idTypeCache;

  /**
   * Create a new instance.
   *
   * @param objectify Objectify proxy.
   */
  public EntityMetadataImpl(ObjectifyProxy objectify) {
    this.objectify = objectify;
    this.idFieldCache = new HashMap<>();
    this.idTypeCache = new HashMap<>();
  }

  @Nonnull
  @Override
  public <E> Field getIdField(Class<E> entityClass) {
    return idFieldCache.computeIfAbsent(entityClass, this::getIdFieldFromObjectify);
  }

  @Nonnull
  @Override
  public <E> Class<?> getIdType(Class<E> entityClass) {
    return idTypeCache.computeIfAbsent(entityClass, this::getIdTypeFromObjectify);
  }

  protected <E> Field getIdFieldFromObjectify(Class<E> entityClass) {
    String idFieldName = objectify.ofy()
        .factory()
        .getMetadata(entityClass)
        .getKeyMetadata()
        .getIdFieldName();
    return ReflectionUtils.findField(entityClass, idFieldName);
  }

  protected <E> Class<?> getIdTypeFromObjectify(Class<E> entityClass) {
    return objectify.ofy()
        .factory()
        .getMetadata(entityClass)
        .getKeyMetadata()
        .getIdFieldType();
  }
}
