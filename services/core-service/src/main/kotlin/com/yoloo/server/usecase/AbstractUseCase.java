package com.yoloo.server.usecase;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Abstract class that allows safe implementations of an {@link UseCase}.
 *
 * @param <I> is the {@link UseCase} input type.
 * @param <O> is the {@link UseCase} produced result type.
 */
public abstract class AbstractUseCase<I extends UseCase.Input, O> implements UseCase<I, O> {
  /**
   * Executes the {@link UseCase} logic, and guarantees that input, success callback and errors
   * handler are valid and not-null.
   *
   * @param input is the not-null {@link UseCase} input.
   */
  protected abstract O onExecute(@Nonnull I input);

  public final O execute(final I input) {
    return onExecute(Objects.requireNonNull(input));
  }
}
