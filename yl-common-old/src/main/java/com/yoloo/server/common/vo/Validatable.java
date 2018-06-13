package com.yoloo.server.common.vo;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;

public interface Validatable {

  default void validate() {
    Set<ConstraintViolation<Validatable>> violations =
        Validation.buildDefaultValidatorFactory().getValidator().validate(this);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
