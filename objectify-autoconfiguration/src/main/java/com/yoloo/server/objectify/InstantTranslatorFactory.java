package com.yoloo.server.objectify;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.TimestampValue;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.ValueType;
import com.googlecode.objectify.impl.translate.SimpleTranslatorFactory;

import java.time.Instant;

/**
 * Converts JSR-310 {@link Instant} fields to {@link Timestamp} values for persistence in the
 * datastore.
 */
class InstantTranslatorFactory extends SimpleTranslatorFactory<Instant, Timestamp> {

  /** Create a new instance. */
  InstantTranslatorFactory() {
    super(Instant.class, ValueType.TIMESTAMP);
  }

  @Override
  protected Instant toPojo(Value<Timestamp> value) {
    return Instant.ofEpochSecond(value.get().getSeconds());
  }

  @Override
  protected Value<Timestamp> toDatastore(Instant value) {
    return TimestampValue.of(Timestamp.of(java.sql.Timestamp.from(value)));
  }
}
