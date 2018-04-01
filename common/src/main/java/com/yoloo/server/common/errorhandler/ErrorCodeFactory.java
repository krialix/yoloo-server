package com.yoloo.server.common.errorhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Acts as a factory for {@linkplain ErrorCodeConverter} implementations.
 * By calling the {@linkplain #of(Exception)} factory method, clients can
 * find the corresponding {@linkplain ErrorCode} for the given
 * {@linkplain Exception}. Behind the scenes, this factory method, first finds
 * all available implementations of the {@linkplain ErrorCodeConverter}
 * strategy interface. Then selects the first implementation that can actually
 * handles the given exception and delegate the exception translation process
 * to that implementation.
 *
 * @author Ali Dehghani
 * @implNote If the factory method couldn't find any implementation for the given
 * {@linkplain Exception}, It would return the
 * {@linkplain ErrorCode#unknown()} which represent an Unknown Error.
 */
@Component
class ErrorCodeFactory {
  private final ApplicationContext context;

  @Autowired
  ErrorCodeFactory(ApplicationContext context) {
    this.context = context;
  }

  /**
   * Factory method to find the right {@linkplain ErrorCodeConverter}
   * implementation and delegates the conversion task to that implementation.
   * If it couldn't find any registered implementation, It would return an
   * Unknown Error represented by the {@linkplain UnknownError} implementation.
   *
   * @param exception The exception to find the implementation based on that
   * @return An instance of {@linkplain ErrorCode} corresponding the given
   * {@code exception}
   * @implNote Currently this method queries Spring's
   * {@linkplain ApplicationContext} to find the {@linkplain ErrorCodeConverter}
   * implementations. So in order to register your {@linkplain ErrorCodeConverter}
   * implementation, you should annotate your implementation with one of
   * Spring Stereotype annotations, e.g. {@linkplain Component}.
   * Our recommendation is to use the {@linkplain Component} annotation or
   * another meta annotation based on this annotation.
   */
  ErrorCode of(Exception exception) {
    return context.getBeansOfType(ErrorCodeConverter.class)
        .values()
        .stream()
        .filter(impl -> impl.canHandle(exception))
        .findFirst()
        .map(impl -> impl.toErrorCode(exception))
        .orElse(ErrorCode.unknown());
  }
}
