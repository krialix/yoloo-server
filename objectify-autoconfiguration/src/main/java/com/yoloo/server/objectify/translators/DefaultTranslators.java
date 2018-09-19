package com.yoloo.server.objectify.translators;

import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.impl.translate.opt.BigDecimalLongTranslatorFactory;

public final class DefaultTranslators {

  private DefaultTranslators() {}

  public static void add(ObjectifyFactory fact) {
    fact.getTranslators().add(new InstantTranslatorFactory());
    fact.getTranslators().add(new BigDecimalLongTranslatorFactory());
    fact.getTranslators().add(new CuckooFilterTranslatorFactory());
  }
}
