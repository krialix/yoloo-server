package com.yoloo.server.filter;

import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.common.util.concurrent.Futures;
import com.googlecode.objectify.Key;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static java.util.stream.Collectors.toList;

@EnableScheduling
@Service
public class FilterServiceImpl implements FilterService {

  private static final Logger logger = LoggerFactory.getLogger(FilterServiceImpl.class);

  private final AsyncMemcacheService memcacheService;

  public FilterServiceImpl(AsyncMemcacheService memcacheService) {
    this.memcacheService = memcacheService;
  }

  private static <T> Key<T> createKey(Class<T> type) {
    return Key.create(type, )
  }

  @Override
  public EntityFilter get() {
    EntityFilter cache;
    try {
      cache = (EntityFilter) memcacheService.get(EntityFilter.createId()).get();

      if (cache == null) {
        cache = loadFromDb();
      }
    } catch (InterruptedException | ExecutionException e) {
      cache = loadFromDb();
    }
    return cache;
  }

  private EntityFilter loadFromDb(Class<?> type) {
    return ofy().load().key(EntityFilter.createKey()).now();
  }

  @Override
  public <T extends Filter> T get(@NotNull Class<T> type) {
    return loadFromDb();
  }

  @Override
  public void save(EntityFilter cache) {
    try {
      memcacheService.put(EntityFilter.createId(), cache).get();
    } catch (InterruptedException | ExecutionException e) {
      logger.error("An error occurred while memcache sync put operation", e);
      // if an error occurs while saving to memcache, save entity to datastore directly
      ofy().save().entity(cache);
    }
  }

  @Override
  public void saveAsync(EntityFilter cache) {
    memcacheService.put(EntityFilter.createId(), cache);
  }

  // schedule at every 10 mins
  @Scheduled(cron = "*/10 * * * *")
  protected void backupEntityCacheToDb() {
    EntityFilter cache = get();
    if (cache != null) {
      saveAsync(cache);
    }
  }

  @SafeVarargs
  @Override
  public final <T extends Filter> Map<Key<T>, Object> get(@Nonnull Key<T>... keys) {
    List<String> ids = Arrays.stream(keys).map(Key::getName).collect(toList());
    Future<Map<String, Object>> valueFuture = memcacheService.getAll(ids);
    @SuppressWarnings("UnstableApiUsage")
    Map<String, Object> map = Futures.getChecked(valueFuture, RuntimeException.class);
    return null;
  }

  @Override
  public void save(Filter filter) {

  }

  @CachePut(cacheNames = "#filter.filterId()", unless = "#filter == null")
  @Override
  public void saveAsync(Filter filter) {
    memcacheService.put()
  }
}
