package com.yoloo.server.common.shared;

import javax.annotation.Nonnull;

/**
 * This is a interface that represents a business use case. It's designed after ideas of Robert C.
 * Martin aka "Uncle Bob" that can be found by catchy name "Clean Architecture".
 *
 * <p>Use case will have only one method - {@link #execute(I)}. This method takes a
 * pair of request and response objects that should be instantiated externally in plugin way
 * pattern. That ensures decoupling of presentation from domain.
 */
@FunctionalInterface
public interface UseCase<I, O> {

  /**
   * Executes a business use case. Request and response object should be given
   *
   * @param request a request for use case
   */
  @Nonnull
  O execute(@Nonnull I request);
}
