package com.yoloo.server.filter;

import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.common.util.concurrent.Futures;
import com.googlecode.objectify.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.googlecode.objectify.ObjectifyService.ofy;

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

  private EntityIdFilter loadFromDb(Class<?> type) {
    return ofy().load().key(EntityIdFilter.createKey()).now();
  }

  @Override
  public void save(EntityIdFilter cache) {
    try {
      memcacheService.put(EntityIdFilter.createId(), cache).get();
    } catch (InterruptedException | ExecutionException e) {
      logger.error("An error occurred while memcache sync put operation", e);
      // if an error occurs while saving to memcache, save entity to datastore directly
      ofy().save().entity(cache);
    }
  }

  // schedule at every 10 mins
  @Scheduled(cron = "*/10 * * * *")
  protected void writeFiltersToDb() {
    List<com.google.cloud.datastore.Key> keys = Stream.of(EntityIdFilter.createKey(), PermissionFilter.createKey())
        .map(Key::getRaw)
        .collect(Collectors.toList());
    Collection<Object> values = Futures.getUnchecked(memcacheService.getAll(keys)).values();
    ofy().save().entities(values);
  }

  @SafeVarargs
  @Override
  public final <T extends Filter> Map<Key<T>, T> getAll(@Nonnull Key<T>... keys) {
    return ofy().load().keys(keys);
  }

  @Override
  public void saveAsync(Filter... filters) {
    Map<com.google.cloud.datastore.Key, Filter> map = new HashMap<>();
    for (Filter filter : filters) {
      com.google.cloud.datastore.Key raw = filter.toFilterKey().getRaw();
      map.putIfAbsent(raw, filter);
    }
    memcacheService.putAll(map);
  }
}
