package com.yoloo.server.objectify;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;
import com.yoloo.server.objectify.configuration.ObjectifyAutoConfiguration;

import java.io.Closeable;
import java.util.function.Supplier;

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

  /**
   * Runs one unit of work, making the root Objectify context available. This does not start a
   * transaction, but it makes the static ofy() method return an appropriate object. Equivalent to
   * calling {@link ObjectifyService#run(Work)}.
   *
   * @param work Unit of work.
   * @param <R> Result type.
   * @return Result of running unit of work.
   */
  static <R> R run(Work<R> work) {
    return ObjectifyService.run(work);
  }

  /**
   * Runs one unit of work, making the root Objectify context available. This does not start a
   * transaction, but it makes the static ofy() method return an appropriate object. Equivalent to
   * calling {@link ObjectifyService#run(Work)}.
   *
   * @param work Unit of work.
   * @param <R> Result type.
   * @return Result of running unit of work.
   */
  static <R> R run(Supplier<R> work) {
    return ObjectifyService.run(work::get);
  }

  /**
   * Runs one unit of work, making the root Objectify context available. This does not start a
   * transaction, but it makes the static ofy() method return an appropriate object. Equivalent to
   * calling {@link ObjectifyService#run(Work)}.
   *
   * @param work Unit of work.
   */
  static void run(Runnable work) {
    run(
        (Work<Void>)
            () -> {
              work.run();
              return null;
            });
  }

  /**
   * An alternative to {@link #run(Work)} which is somewhat easier to use with testing (ie, @Before
   * and @After) frameworks. You must {@link Closeable#close()} the return value at the end of the
   * request in a finally block.
   *
   * @return Closeable used to close the unit of work.
   */
  static Closeable begin() {
    return ObjectifyService.begin();
  }
}
