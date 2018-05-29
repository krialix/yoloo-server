package com.yoloo.server.objectify.translators;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Converts JSR-310 {@link ZonedDateTime} fields to an ISO-8601 string representation for
 * persistence in the datastore. All values are converted to UTC for persistence to allow
 * lexicographic sorting by index and to reduce zone-based idiosyncratic behaviour. Supports
 * nanosecond precision.
 */
public class ZonedDateTimeStringTranslatorFactory
    extends AbstractSimpleTranslatorFactory<ZonedDateTime, String> {

  /**
   * Create a new instance.
   */
  public ZonedDateTimeStringTranslatorFactory() {
    super(ZonedDateTime.class);
  }

  @Override
  protected SimpleTranslator<ZonedDateTime, String> createTranslator() {
    return new SimpleTranslator<ZonedDateTime, String>() {
      @Override
      public ZonedDateTime loadValue(String datastoreValue) {
        return Instant.parse(datastoreValue).atZone(ZoneId.systemDefault());
      }

      @Override
      public String saveValue(ZonedDateTime pojoValue) {
        return pojoValue
            .withZoneSameInstant(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_DATE_TIME);
      }
    };
  }
}
