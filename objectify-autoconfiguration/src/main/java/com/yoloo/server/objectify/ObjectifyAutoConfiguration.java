package com.yoloo.server.objectify;

import com.github.takemikami.objectify.appengine.AppEngineMemcacheClientService;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.AsyncDatastore;
import com.googlecode.objectify.impl.translate.opt.joda.JodaMoneyTranslators;
import com.yoloo.server.objectify.translators.DefaultTranslators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Collection;
import java.util.List;

/**
 * Automatic objectify configuration. Provides the following beans and services:
 *
 * <ul>
 *   <li>Registers the {@link ObjectifyFilter} to manage Objectify sessions per-request.
 * </ul>
 */
@Configuration
@ConditionalOnClass(Objectify.class)
public class ObjectifyAutoConfiguration {

  private final List<ObjectifyConfigurer> configurers;
  private final ObjectifyYamlProperties properties;

  @Autowired
  public ObjectifyAutoConfiguration(
      List<ObjectifyConfigurer> configurers, ObjectifyYamlProperties properties) {
    this.configurers = ImmutableList.copyOf(configurers);
    this.properties = properties;
  }

  private static void registerTranslators(
      ObjectifyFactory factory, List<ObjectifyConfigurer> configurers) {
    DefaultTranslators.add(factory);
    JodaMoneyTranslators.add(factory);

    configurers
        .stream()
        .map(ObjectifyConfigurer::registerTranslators)
        .flatMap(Collection::stream)
        .distinct()
        .forEach(translatorFactory -> factory.getTranslators().add(translatorFactory));
  }

  private static void registerEntities(
      ObjectifyFactory factory, List<ObjectifyConfigurer> configurers) {
    configurers
        .stream()
        .map(ObjectifyConfigurer::registerEntities)
        .flatMap(Collection::stream)
        .distinct()
        .forEach(factory::register);
  }

  @Bean
  public FilterRegistrationBean<ObjectifyFilter> objectifyFilterRegistration() {
    FilterRegistrationBean<ObjectifyFilter> bean =
        new FilterRegistrationBean<>(new ObjectifyFilter());
    bean.setOrder(Integer.MIN_VALUE); // must ensure loaded prior to security filter
    return bean;
  }

  @Bean
  public ServletListenerRegistrationBean<ObjectifyListener> listenerRegistrationBean() {
    ServletListenerRegistrationBean<ObjectifyListener> bean =
        new ServletListenerRegistrationBean<>();
    bean.setListener(new ObjectifyListener(properties));

    ObjectifyFactory factory = ObjectifyService.factory();
    registerTranslators(factory, configurers);
    registerEntities(factory, configurers);

    return bean;
  }

  static class ObjectifyListener implements ServletContextListener {

    private final ObjectifyYamlProperties properties;

    ObjectifyListener(ObjectifyYamlProperties properties) {
      this.properties = properties;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
      final Datastore datastoreService = DatastoreOptions.getDefaultInstance().getService();
      final AppEngineMemcacheClientService memcacheClientService =
          new AppEngineMemcacheClientService();

      ObjectifyFactory factory =
          new ObjectifyFactory(datastoreService, memcacheClientService) {
            @Override
            public AsyncDatastore asyncDatastore() {
              AsyncDatastore asyncDatastore = super.asyncDatastore();

              return properties.getEnableDatabaseRequestTracking()
                  ? new RequestCapturingAsyncDatastoreImpl(asyncDatastore)
                  : asyncDatastore;
            }
          };

      ObjectifyService.init(factory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
  }
}
