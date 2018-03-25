package com.yoloo.server.objectify.translate.time;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.*;

import java.time.LocalDateTime;

/**
 * Converts JSR-310 {@link LocalDateTime} fields to an ISO-8601 string representation for persistence in the datastore.
 * All values are converted to UTC for persistence to allow lexicographic sorting by index and to reduce zone-based idiosyncratic behaviour.
 * Supports nanosecond precision.
 */
public class LocalDateTimeStringTranslatorFactory extends ValueTranslatorFactory<LocalDateTime, String> {

  /**
   * Create a new instance.
   */
  public LocalDateTimeStringTranslatorFactory() {
    super(LocalDateTime.class);
  }

  @Override
  protected ValueTranslator<LocalDateTime, String> createValueTranslator(TypeKey<LocalDateTime> tk, CreateContext ctx, Path path) {
    return new ValueTranslator<LocalDateTime, String>(String.class) {
      @Override
      protected LocalDateTime loadValue(String value, LoadContext ctx, Path path) throws SkipException {
        return LocalDateTime.parse(value);
      }

      @Override
      protected String saveValue(LocalDateTime value, boolean index, SaveContext ctx, Path path) throws SkipException {
        return value.toString();
      }
    };
  }
}
