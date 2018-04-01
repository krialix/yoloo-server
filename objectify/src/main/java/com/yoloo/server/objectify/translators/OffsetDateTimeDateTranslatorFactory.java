package com.yoloo.server.objectify.translators;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Converts JSR-310 {@link OffsetDateTime} fields to {@link Date} values for persistence in the
 * datastore.
 */
public class OffsetDateTimeDateTranslatorFactory
    extends AbstractSimpleTranslatorFactory<OffsetDateTime, Date> {

  /** Create a new instance. */
  public OffsetDateTimeDateTranslatorFactory() {
    super(OffsetDateTime.class);
  }

  @Override
  protected SimpleTranslator<OffsetDateTime, Date> createTranslator() {
    return new SimpleTranslator<OffsetDateTime, Date>() {
      @Override
      public OffsetDateTime loadValue(Date datastoreValue) {
        return OffsetDateTime.ofInstant(datastoreValue.toInstant(), ZoneOffset.systemDefault());
      }

      @Override
      public Date saveValue(OffsetDateTime pojoValue) {
        return Date.from(pojoValue.atZoneSameInstant(ZoneOffset.systemDefault()).toInstant());
      }
    };
  }
}
