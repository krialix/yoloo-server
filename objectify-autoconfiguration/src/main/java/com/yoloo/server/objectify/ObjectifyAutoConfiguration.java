package com.yoloo.server.objectify;

import com.github.takemikami.objectify.appengine.AppEngineMemcacheClientService;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.SimpleTranslatorFactory;
import com.googlecode.objectify.impl.translate.Translators;
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
import java.util.stream.Stream;

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

  @Autowired
  public ObjectifyAutoConfiguration(List<ObjectifyConfigurer> configurers) {
    this.configurers = ImmutableList.copyOf(configurers);
  }

  private static void registerTranslators(
      ObjectifyFactory factory, List<ObjectifyConfigurer> configurers) {
    Translators translators = factory.getTranslators();

    Stream<? extends SimpleTranslatorFactory<?, ?>> defaultTranslatorsStream =
        DefaultTranslators.getTranslatorFactories().stream();

    Stream<? extends SimpleTranslatorFactory<?, ?>> additionalTranslatorsStream =
        configurers
            .stream()
            .map(ObjectifyConfigurer::registerTranslators)
            .flatMap(Collection::stream);

    Stream.concat(defaultTranslatorsStream, additionalTranslatorsStream)
        .distinct()
        .forEach(translators::add);
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
    bean.setListener(new ObjectifyListener());

    ObjectifyFactory factory = ObjectifyService.factory();
    registerTranslators(factory, configurers);
    registerEntities(factory, configurers);

    return bean;
  }

  public static class ObjectifyListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
      ObjectifyService.init(
          new ObjectifyFactory(
              DatastoreOptions.newBuilder().setHost("http://localhost:8484").build().getService(),
              new AppEngineMemcacheClientService()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
  }
}
