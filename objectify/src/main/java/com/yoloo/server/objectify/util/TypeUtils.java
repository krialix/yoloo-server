package com.yoloo.server.objectify.util;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.Modifier;

import static com.google.common.base.Preconditions.checkArgument;

/** Utilities methods related to reflection. */
public class TypeUtils {

  public static <T> T instantiate(Class<? extends T> clazz) {
    checkArgument(
        Modifier.isPublic(clazz.getModifiers()),
        "AppEngine's custom security manager won't let us reflectively access non-public types");
    try {
      return clazz.getConstructor().newInstance();
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  /** A {@TypeToken} that removes an ugly cast in the common cases of getting a known type. */
  public static class TypeInstantiator<T> extends TypeToken<T> {
    protected TypeInstantiator(Class<?> declaringClass) {
      super(declaringClass);
    }

    @SuppressWarnings("unchecked")
    public Class<T> getExactType() {
      return (Class<T>) getRawType();
    }

    public T instantiate() {
      return TypeUtils.instantiate(getExactType());
    }
  }
}
