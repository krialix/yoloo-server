package com.yoloo.server.entity;

public interface EntityCacheService {

  EntityCache get();

  void save(EntityCache cache);

  void saveAsync(EntityCache cache);
}
