package com.yoloo.server.core.user;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.*;

import java.util.Locale;

public class LocaleTranslatorFactory extends ValueTranslatorFactory<Locale, String> {

  /**
   * Create a new instance.
   */
  public LocaleTranslatorFactory() {
    super(Locale.class);
  }

  @Override
  protected ValueTranslator<Locale, String> createValueTranslator(TypeKey<Locale> tk, CreateContext ctx, Path path) {
    return new ValueTranslator<Locale, String>(String.class) {
      @Override
      protected Locale loadValue(String value, LoadContext ctx, Path path) throws SkipException {
        return Locale.forLanguageTag(value);
      }

      @Override
      protected String saveValue(Locale value, boolean index, SaveContext ctx, Path path) throws SkipException {
        return value.getLanguage();
      }
    };
  }
}
