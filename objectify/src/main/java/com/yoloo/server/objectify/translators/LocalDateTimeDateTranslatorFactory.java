package com.yoloo.server.objectify.translators;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Converts JSR-310 {@link LocalDateTime} fields to {@link Date} values for persistence in the
 * datastore.
 */
public class LocalDateTimeDateTranslatorFactory
    extends AbstractSimpleTranslatorFactory<LocalDateTime, Date> {

  /**
   * Create a new instance.
   */
  public LocalDateTimeDateTranslatorFactory() {
    super(LocalDateTime.class);
  }

  @Override
  protected SimpleTranslator<LocalDateTime, Date> createTranslator() {
    return new SimpleTranslator<LocalDateTime, Date>() {
      @Override
      public LocalDateTime loadValue(Date datastoreValue) {
        return datastoreValue.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
      }

      @Override
      public Date saveValue(LocalDateTime pojoValue) {
        return Date.from(pojoValue.atZone(ZoneOffset.UTC).toInstant());
      }
    };
  }
}
