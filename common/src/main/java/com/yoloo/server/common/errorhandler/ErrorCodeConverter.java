package com.yoloo.server.common.errorhandler;

/**
 * Strategy interface responsible for converting an instance of
 * {@linkplain Exception} to appropriate instance of {@linkplain ErrorCode}.
 * Implementations of this interface should be accessed indirectly through
 * the {@linkplain ErrorCodeFactory}'s factory method.
 *
 * @implSpec The {@linkplain #toErrorCode(Exception)} method should be called iff the
 * call to the {@linkplain #canHandle(Exception)} returns true for the same exception.
 *
 * @see ErrorCode
 * @see ErrorCodeFactory
 *
 * @author Ali Dehghani
 */
public interface ErrorCodeConverter {
  /**
   * Determines whether this implementation can handle the given exception or not.
   * Calling the {@linkplain #toErrorCode(Exception)} when this method returns
   * false, is strongly discouraged.
   *
   * @param exception The exception to examine
   * @return true if the implementation can handle the exception, false otherwise.
   */
  boolean canHandle(Exception exception);

  /**
   * Performs the actual mechanics of converting the given {@code exception}
   * to an instance of {@linkplain ErrorCode}.
   *
   * @implSpec Call this method iff the {@linkplain #canHandle(Exception)} returns
   * true.
   *
   * @param exception The exception to convert
   * @return An instance of {@linkplain ErrorCode} corresponding to the given
   * {@code exception}
   */
  ErrorCode toErrorCode(Exception exception);
}