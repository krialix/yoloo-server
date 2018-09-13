package com.yoloo.server.objectify;

import com.googlecode.objectify.impl.translate.SimpleTranslatorFactory;
import com.googlecode.objectify.impl.translate.opt.BigDecimalLongTranslatorFactory;

import java.util.Arrays;
import java.util.List;

enum DefaultTranslators {
  ;

  static List<SimpleTranslatorFactory<?, ?>> getTranslatorFactories() {
    return Arrays.asList(
        new InstantTranslatorFactory(),
        new BigDecimalLongTranslatorFactory(),
        new CuckooFilterTranslatorFactory());
  }
}
