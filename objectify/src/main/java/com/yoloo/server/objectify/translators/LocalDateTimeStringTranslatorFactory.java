package com.yoloo.server.objectify.translators;

import java.time.LocalDateTime;

/**
 * Converts JSR-310 {@link LocalDateTime} fields to an ISO-8601 string representation for
 * persistence in the datastore. All values are converted to UTC for persistence to allow
 * lexicographic sorting by index and to reduce zone-based idiosyncratic behaviour. Supports
 * nanosecond precision.
 */
public class LocalDateTimeStringTranslatorFactory
    extends AbstractSimpleTranslatorFactory<LocalDateTime, String> {

  /**
   * Create a new instance.
   */
  public LocalDateTimeStringTranslatorFactory() {
    super(LocalDateTime.class);
  }

  @Override
  protected SimpleTranslator<LocalDateTime, String> createTranslator() {
    return new SimpleTranslator<LocalDateTime, String>() {
      @Override
      public LocalDateTime loadValue(String datastoreValue) {
        return LocalDateTime.parse(datastoreValue);
      }

      @Override
      public String saveValue(LocalDateTime pojoValue) {
        return pojoValue.toString();
      }
    };
  }
}
