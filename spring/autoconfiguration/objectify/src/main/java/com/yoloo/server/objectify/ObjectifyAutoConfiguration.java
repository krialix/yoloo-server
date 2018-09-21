package com.yoloo.server.objectify;

import com.github.takemikami.objectify.appengine.AppEngineMemcacheClientService;
import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
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

  @Autowired
  public ObjectifyAutoConfiguration(List<ObjectifyConfigurer> configurers) {
    this.configurers = ImmutableList.copyOf(configurers);
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
    return new ServletListenerRegistrationBean<>(new ObjectifyListener(configurers));
  }

  static class ObjectifyListener implements ServletContextListener {
    private final List<ObjectifyConfigurer> configurers;

    ObjectifyListener(List<ObjectifyConfigurer> configurers) {
      this.configurers = configurers;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
      ObjectifyFactory factory = new ObjectifyFactory(new AppEngineMemcacheClientService());
      ObjectifyService.init(factory);

      registerTranslators(factory, configurers);
      registerEntities(factory, configurers);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
  }
}
