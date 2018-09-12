package com.yoloo.server.objectify.repository;

import com.googlecode.objectify.Objectify;

/**
 * Mark a class as aware of Objectify.
 * <p>
 * Exposes the {@link Objectify} via the {@link #ofy()} method.
 */
public interface ObjectifyAware {
  /**
   * @return Objectify.
   */
  Objectify ofy();
}
