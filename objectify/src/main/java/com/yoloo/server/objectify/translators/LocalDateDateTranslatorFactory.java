package com.yoloo.server.objectify.translators;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Converts JSR-310 {@link LocalDate} fields to {@link Date} values for persistence in the
 * datastore.
 */
public class LocalDateDateTranslatorFactory
    extends AbstractSimpleTranslatorFactory<LocalDate, Date> {

  /** Create a new instance. */
  public LocalDateDateTranslatorFactory() {
    super(LocalDate.class);
  }

  @Override
  protected SimpleTranslator<LocalDate, Date> createTranslator() {
    return new SimpleTranslator<LocalDate, Date>() {
      @Override
      public LocalDate loadValue(Date datastoreValue) {
        return datastoreValue.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
      }

      @Override
      public Date saveValue(LocalDate pojoValue) {
        return Date.from(pojoValue.atStartOfDay(ZoneOffset.UTC).toInstant());
      }
    };
  }
}
