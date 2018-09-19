package com.yoloo.server.objectify.translators;

import com.google.cloud.datastore.Blob;
import com.google.cloud.datastore.BlobValue;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.ValueType;
import com.googlecode.objectify.impl.translate.SimpleTranslatorFactory;
import net.cinnom.nanocuckoo.NanoCuckooFilter;
import org.springframework.util.SerializationUtils;

import java.util.Objects;

class CuckooFilterTranslatorFactory extends SimpleTranslatorFactory<NanoCuckooFilter, Blob> {

  CuckooFilterTranslatorFactory() {
    super(NanoCuckooFilter.class, ValueType.BLOB);
  }

  @Override
  protected NanoCuckooFilter toPojo(Value<Blob> value) {
    return (NanoCuckooFilter) SerializationUtils.deserialize(value.get().toByteArray());
  }

  @Override
  protected Value<Blob> toDatastore(NanoCuckooFilter value) {
    byte[] bytes = SerializationUtils.serialize(value);
    return BlobValue.of(Blob.copyFrom(Objects.requireNonNull(bytes)));
  }
}
