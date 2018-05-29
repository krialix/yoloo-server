package com.yoloo.server.objectify;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.yoloo.server.objectify.configuration.ObjectifyAutoConfiguration;

/**
 * Provides a non-static access alternative to the {@link ObjectifyService}. Using the objectify
 * proxy bean in tandem with the {@link ObjectifyAutoConfiguration} mechanism ensures that any calls
 * to Objectify will occur after all entities, translators, etc have been registered.
 */
public interface ObjectifyProxy {

  /**
   * Get an objectify instance. Equivalent to calling {@link ObjectifyService#ofy()}
   *
   * @return Objectify instance.
   */
  static Objectify ofy() {
    return ObjectifyService.ofy();
  }

  /**
   * Get the Objectify instance factory. Equivalent to calling {@link ObjectifyService#factory()}
   *
   * @return The Objectify instance factory.
   */
  static ObjectifyFactory factory() {
    return ObjectifyService.factory();
  }
}
