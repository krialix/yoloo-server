package com.yoloo.server.common.service;

import java.util.Map;

public interface CacheService {

  Map<Object, Object> getByKey(String key);
}
