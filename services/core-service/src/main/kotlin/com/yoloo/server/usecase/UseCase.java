package com.yoloo.server.usecase;

/**
 * Interface that represents an application use-case.
 *
 * @param <I> is the {@link UseCase} input type.
 * @param <O> is the {@link UseCase} output type, e.g. the result produced by the use-case.
 */
@FunctionalInterface
public interface UseCase<I, O> {
  /**
   * Executes the {@link UseCase} logic.
   *
   * @param input is the {@link UseCase} input.
   */
  O execute(I input);
}
