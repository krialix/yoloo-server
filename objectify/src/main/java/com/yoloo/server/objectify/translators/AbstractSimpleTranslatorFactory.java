package com.yoloo.server.objectify.translators;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.CreateContext;
import com.googlecode.objectify.impl.translate.LoadContext;
import com.googlecode.objectify.impl.translate.SaveContext;
import com.googlecode.objectify.impl.translate.SkipException;
import com.googlecode.objectify.impl.translate.TypeKey;
import com.googlecode.objectify.impl.translate.ValueTranslator;
import com.googlecode.objectify.impl.translate.ValueTranslatorFactory;
import com.yoloo.server.objectify.util.TypeUtils;

/** Common boilerplate for translator factories. */
public abstract class AbstractSimpleTranslatorFactory<P, D> extends ValueTranslatorFactory<P, D> {

  public AbstractSimpleTranslatorFactory(Class<P> clazz) {
    super(clazz);
  }

  @Override
  protected ValueTranslator<P, D> createValueTranslator(
      TypeKey<P> tk, CreateContext ctx, Path path) {
    return new ValueTranslator<P, D>(
        new TypeUtils.TypeInstantiator<D>(getClass()) {}.getExactType()) {

      SimpleTranslator<P, D> simpleTranslator = createTranslator();

      @Override
      protected P loadValue(D value, LoadContext ctx, Path path) throws SkipException {
        return simpleTranslator.loadValue(value);
      }

      @Override
      protected D saveValue(P value, boolean index, SaveContext ctx, Path path)
          throws SkipException {
        return simpleTranslator.saveValue(value);
      }
    };
  }

  protected abstract SimpleTranslator<P, D> createTranslator();

  /** Translator with reduced boilerplate. */
  protected interface SimpleTranslator<P, D> {

    P loadValue(D datastoreValue);

    D saveValue(P pojoValue);
  }
}
