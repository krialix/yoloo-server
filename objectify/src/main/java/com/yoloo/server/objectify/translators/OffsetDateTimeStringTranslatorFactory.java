package com.yoloo.server.objectify.translators;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Converts JSR-310 {@link OffsetDateTime} fields to an ISO-8601 string representation for
 * persistence in the datastore. All values are converted to UTC for persistence to allow
 * lexicographic sorting by index and to reduce zone-based idiosyncratic behaviour. Supports
 * nanosecond precision.
 */
public class OffsetDateTimeStringTranslatorFactory
    extends AbstractSimpleTranslatorFactory<OffsetDateTime, String> {

  /** Create a new instance. */
  public OffsetDateTimeStringTranslatorFactory() {
    super(OffsetDateTime.class);
  }

  @Override
  protected SimpleTranslator<OffsetDateTime, String> createTranslator() {
    return new SimpleTranslator<OffsetDateTime, String>() {
      @Override
      public OffsetDateTime loadValue(String datastoreValue) {
        Instant instant = Instant.parse(datastoreValue);
        ZoneOffset offset = ZoneOffset.systemDefault().getRules().getOffset(instant);
        return instant.atOffset(offset);
      }

      @Override
      public String saveValue(OffsetDateTime pojoValue) {
        return pojoValue.atZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);
      }
    };
  }
}
