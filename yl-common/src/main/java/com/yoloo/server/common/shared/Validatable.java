package com.yoloo.server.common.shared;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import java.util.Set;

public interface Validatable {

  default void validate() {
    Set<ConstraintViolation<Validatable>> violations =
        Validation.buildDefaultValidatorFactory().getValidator().validate(this);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
