package com.yoloo.server.objectify.configuration;

import static com.yoloo.server.objectify.ObjectifyProxy.factory;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.impl.translate.Translators;
import com.googlecode.objectify.impl.translate.opt.BigDecimalLongTranslatorFactory;
import com.yoloo.server.objectify.ObjectifyProxy;
import com.yoloo.server.objectify.translators.LocalDateDateTranslatorFactory;
import com.yoloo.server.objectify.translators.LocalDateTimeDateTranslatorFactory;
import com.yoloo.server.objectify.translators.OffsetDateTimeDateTranslatorFactory;
import com.yoloo.server.objectify.translators.ZonedDateTimeDateTranslatorFactory;
import java.util.Arrays;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Automatic objectify configuration. Provides the following beans and services:
 *
 * <ul>
 * <li>Registers an {@link ObjectifyProxy} configured by any registered {@link
 * ObjectifyConfigurer} beans.
 * <li>Registers the {@link ObjectifyFilter} to manage Objectify sessions per-request.
 * </ul>
 */
@Configuration
@ConditionalOnClass(Objectify.class)
@ConditionalOnMissingBean(ObjectifyProxy.class)
public class ObjectifyAutoConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(ObjectifyAutoConfiguration.class);

  private final Collection<ObjectifyConfigurer> configurers;

  @Autowired
  public ObjectifyAutoConfiguration(ApplicationContext context) {
    this.configurers = getConfigurerImplementations(context);
  }

  @Bean("ofyFilter")
  public FilterRegistrationBean<ObjectifyFilter> objectifyFilterRegistration() {
    return new FilterRegistrationBean<>(new ObjectifyFilter());
  }

  @DependsOn("ofyFilter")
  @Bean("ofy")
  public ObjectifyProxy ofy() {
    ObjectifyProxy proxy = new ObjectifyProxy() {
    };

    registerTranslators(factory().getTranslators());
    registerEntities(factory());

    return proxy;
  }

  private void registerTranslators(Translators translators) {
    Arrays.asList(
        new LocalDateDateTranslatorFactory(),
        new LocalDateTimeDateTranslatorFactory(),
        new ZonedDateTimeDateTranslatorFactory(),
        new OffsetDateTimeDateTranslatorFactory(),
        new BigDecimalLongTranslatorFactory())
        .forEach(translators::add);

    configurers
        .stream()
        .map(ObjectifyConfigurer::registerTranslators)
        .flatMap(Collection::stream)
        .distinct()
        .peek(
            translatorFactory ->
                LOGGER.info(
                    "{} added to translators", translatorFactory.getClass().getSimpleName()))
        .forEach(translators::add);
  }

  private void registerEntities(ObjectifyFactory factory) {
    configurers
        .stream()
        .map(ObjectifyConfigurer::registerEntities)
        .flatMap(Collection::stream)
        .distinct()
        .forEach(factory::register);
  }

  /**
   * Gather all the {@link ObjectifyConfigurer} beans registered with the container. These will be
   * used to configure the beans created here.
   *
   * @param {@link ObjectifyConfigurer} list.
   */
  private Collection<ObjectifyConfigurer> getConfigurerImplementations(ApplicationContext context) {
    return context.getBeansOfType(ObjectifyConfigurer.class).values();
  }
}
