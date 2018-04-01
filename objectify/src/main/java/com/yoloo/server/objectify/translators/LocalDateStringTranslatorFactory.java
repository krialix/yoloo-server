package com.yoloo.server.objectify.translators;

import java.time.LocalDate;

/**
 * Converts JSR-310 {@link LocalDate} fields to an ISO-8601 string representation for persistence in
 * the datastore. All values are converted to UTC for persistence to allow lexicographic sorting by
 * index and to reduce zone-based idiosyncratic behaviour. Supports nanosecond precision.
 */
public class LocalDateStringTranslatorFactory
    extends AbstractSimpleTranslatorFactory<LocalDate, String> {

  /** Create a new instance. */
  public LocalDateStringTranslatorFactory() {
    super(LocalDate.class);
  }

  @Override
  protected SimpleTranslator<LocalDate, String> createTranslator() {
    return new SimpleTranslator<LocalDate, String>() {
      @Override
      public LocalDate loadValue(String datastoreValue) {
        return LocalDate.parse(datastoreValue);
      }

      @Override
      public String saveValue(LocalDate pojoValue) {
        return pojoValue.toString();
      }
    };
  }
}
