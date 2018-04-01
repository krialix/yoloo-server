package com.yoloo.server.objectify.translators;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Converts JSR-310 {@link ZonedDateTime} fields to {@link Date} values for persistence in the
 * datastore.
 */
public class ZonedDateTimeDateTranslatorFactory
    extends AbstractSimpleTranslatorFactory<ZonedDateTime, Date> {

  /** Create a new instance. */
  public ZonedDateTimeDateTranslatorFactory() {
    super(ZonedDateTime.class);
  }

  @Override
  protected SimpleTranslator<ZonedDateTime, Date> createTranslator() {
    return new SimpleTranslator<ZonedDateTime, Date>() {
      @Override
      public ZonedDateTime loadValue(Date datastoreValue) {
        return ZonedDateTime.ofInstant(datastoreValue.toInstant(), ZoneOffset.systemDefault());
      }

      @Override
      public Date saveValue(ZonedDateTime pojoValue) {
        return Date.from(pojoValue.withZoneSameInstant(ZoneOffset.systemDefault()).toInstant());
      }
    };
  }
}
