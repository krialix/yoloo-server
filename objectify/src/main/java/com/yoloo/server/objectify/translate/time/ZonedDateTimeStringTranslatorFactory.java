package com.yoloo.server.objectify.translate.time;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Converts JSR-310 {@link ZonedDateTime} fields to an ISO-8601 string representation for persistence in the datastore.
 * All values are converted to UTC for persistence to allow lexicographic sorting by index and to reduce zone-based idiosyncratic behaviour.
 * Supports nanosecond precision.
 */
public class ZonedDateTimeStringTranslatorFactory extends ValueTranslatorFactory<ZonedDateTime, String> {

  /**
   * Create a new instance.
   */
  public ZonedDateTimeStringTranslatorFactory() {
    super(ZonedDateTime.class);
  }

  @Override
  protected ValueTranslator<ZonedDateTime, String> createValueTranslator(TypeKey<ZonedDateTime> tk, CreateContext ctx, Path path) {
    return new ValueTranslator<ZonedDateTime, String>(String.class) {
      @Override
      protected ZonedDateTime loadValue(String value, LoadContext ctx, Path path) throws SkipException {
        return Instant.parse(value).atZone(ZoneId.systemDefault());
      }

      @Override
      protected String saveValue(ZonedDateTime value, boolean index, SaveContext ctx, Path path) throws SkipException {
        return value.withZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);
      }
    };
  }
}
