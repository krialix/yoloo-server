package com.yoloo.server.util;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * An immutable 2-tuple with value-equals semantics.
 *
 * @param <A> The type of the 1st item in the pair.
 * @param <B> The type of the 2nd item in the pair.
 * @author William Farner
 */
public class Pair<A, B> {

  private final A first;
  private final B second;

  /**
   * Creates a new pair.
   *
   * @param first The first value.
   * @param second The second value.
   */
  private Pair(A first, B second) {
    this.first = first;
    this.second = second;
  }

  /**
   * Creates a function that can extract the first item of pairs of the given type parametrization.
   *
   * @param <S> The type of the 1st item in the pair.
   * @param <T> The type of the 2nd item in the pair.
   * @return A function that will extract the 1st item in a pair.
   */
  public static <S, T> Function<Pair<S, T>, S> first() {
    return pair -> pair.first;
  }

  /**
   * Creates a function that can extract the second item of pairs of the given type parametrization.
   *
   * @param <S> The type of the 1st item in the pair.
   * @param <T> The type of the 2nd item in the pair.
   * @return A function that will extract the 2nd item in a pair.
   */
  public static <S, T> Function<Pair<S, T>, T> second() {
    return pair -> pair.second;
  }

  /**
   * Convenience method to create a pair.
   *
   * @param a The first value.
   * @param b The second value.
   * @param <A> The type of the 1st item in the pair.
   * @param <B> The type of the 2nd item in the pair.
   * @return A new pair of [a, b].
   */
  public static <A, B> Pair<A, B> of(@Nonnull A a, @Nonnull B b) {
    return new Pair<>(a, b);
  }

  public A getFirst() {
    return first;
  }

  public B getSecond() {
    return second;
  }
}
