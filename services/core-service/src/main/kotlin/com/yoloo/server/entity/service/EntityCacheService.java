package com.yoloo.server.entity.service;

public interface EntityCacheService {

  EntityCache get();

  void save(EntityCache cache);

  void saveAsync(EntityCache cache);
}
