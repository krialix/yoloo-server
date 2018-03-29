package com.yoloo.server.objectify.configuration;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.impl.translate.Translators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Collection;

/**
 * Automatic objectify configuration.
 * Provides the following beans and services:
 * <ul>
 * <li>Registers an {@link ObjectifyProxy} configured by any registered {@link ObjectifyConfigurer} beans.</li>
 * <li>Registers the {@link ObjectifyFilter} to manage Objectify sessions per-request.</li>
 * </ul>
 */
@Configuration
@ConditionalOnClass(Objectify.class)
@ConditionalOnMissingBean(ObjectifyProxy.class)
public class ObjectifyAutoConfiguration {

  private final Collection<ObjectifyConfigurer> configurers;

  @Autowired
  public ObjectifyAutoConfiguration(ApplicationContext context) {
    this.configurers = getConfigurerImplementations(context);
  }

  /**
   * @return Register the default {@link ObjectifyProxy}.
   */
  @DependsOn("ofyFilter")
  @Bean
  public ObjectifyProxy ofy() {
    ObjectifyProxy objectify = new ObjectifyProxy() {
    };

    registerTranslators(ObjectifyProxy.factory().getTranslators());
    registerEntities(ObjectifyProxy.factory());

    return objectify;
  }

  @Bean("ofyFilter")
  public FilterRegistrationBean<ObjectifyFilter> objectifyFilterRegistration() {
    FilterRegistrationBean<ObjectifyFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new ObjectifyFilter());
    return registration;
  }

  private void registerTranslators(Translators translators) {
    configurers.stream()
        .map(ObjectifyConfigurer::registerObjectifyTranslators)
        .flatMap(Collection::stream)
        .forEach(translators::add);
  }

  private void registerEntities(ObjectifyFactory factory) {
    configurers.stream()
        .map(ObjectifyConfigurer::registerObjectifyEntities)
        .flatMap(Collection::stream)
        .forEach(factory::register);
  }

  /**
   * Gather all the {@link ObjectifyConfigurer} beans registered with the container.
   * These will be used to configure the beans created here.
   *
   * @param {@link ObjectifyConfigurer} list.
   */
  private Collection<ObjectifyConfigurer> getConfigurerImplementations(ApplicationContext context) {
    return context.getBeansOfType(ObjectifyConfigurer.class).values();
  }
}
