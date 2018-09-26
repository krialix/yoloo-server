package com.yoloo.server.entity;

import com.google.appengine.api.memcache.AsyncMemcacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static com.googlecode.objectify.ObjectifyService.ofy;

@EnableScheduling
@Service
public class EntityCacheServiceImpl implements EntityCacheService {

  private static final Logger logger = LoggerFactory.getLogger(EntityCacheServiceImpl.class);

  private final AsyncMemcacheService memcacheService;

  public EntityCacheServiceImpl(AsyncMemcacheService memcacheService) {
    this.memcacheService = memcacheService;
  }

  @Override
  public EntityCache get() {
    EntityCache cache;
    try {
      cache = (EntityCache) memcacheService.get(EntityCache.createId()).get();

      if (cache == null) {
        cache = loadFromDb();
      }
    } catch (InterruptedException | ExecutionException e) {
      cache = loadFromDb();
    }
    return cache;
  }

  private EntityCache loadFromDb() {
    return ofy().load().key(EntityCache.createKey()).now();
  }

  @Override
  public void save(EntityCache cache) {
    try {
      memcacheService.put(EntityCache.createId(), cache).get();
    } catch (InterruptedException | ExecutionException e) {
      logger.error("An error occurred while memcache sync put operation", e);
      // if an error occurs while saving to memcache, save entity to datastore directly
      ofy().save().entity(cache);
    }
  }

  @Override
  public void saveAsync(EntityCache cache) {
    memcacheService.put(EntityCache.createId(), cache);
  }

  // schedule at every 10 mins
  @Scheduled(cron = "*/10 * * * *")
  protected void backupEntityCacheToDb() {
    EntityCache cache = get();
    if (cache != null) {
      saveAsync(cache);
    }
  }
}
