package com.yoloo.server.objectify.translate.time;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Converts JSR-310 {@link LocalDateTime} fields to {@link Date} values for persistence in the datastore.
 */
public class LocalDateTimeDateTranslatorFactory extends ValueTranslatorFactory<LocalDateTime, Date> {

  /**
   * Create a new instance.
   */
  public LocalDateTimeDateTranslatorFactory() {
    super(LocalDateTime.class);
  }

  @Override
  protected ValueTranslator<LocalDateTime, Date> createValueTranslator(TypeKey<LocalDateTime> tk, CreateContext ctx, Path path) {
    return new ValueTranslator<LocalDateTime, Date>(Date.class) {
      @Override
      protected LocalDateTime loadValue(Date value, LoadContext ctx, Path path) throws SkipException {
        return value.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
      }

      @Override
      protected Date saveValue(LocalDateTime value, boolean index, SaveContext ctx, Path path) throws SkipException {
        return Date.from(value.atZone(ZoneOffset.UTC).toInstant());
      }
    };
  }
}
