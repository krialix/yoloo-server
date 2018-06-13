package com.yoloo.server.common.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({TYPE})
@Retention(RUNTIME)
@Repeatable(Conditional.List.class)
@Documented
@Constraint(validatedBy = {ConditionalValidator.class})
public @interface Conditional {

  String selected();

  String[] required();

  String[] values();

  String message() default "must not be null";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target(TYPE)
  @Retention(RUNTIME)
  @Documented
  @interface List {

    Conditional[] value();
  }
}
