package com.yoloo.server.common.validation.constraints;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.StringUtils;

public class ConditionalValidator implements ConstraintValidator<Conditional, Object> {

  private String selected;
  private String[] required;
  private String message;
  private String[] values;

  @Override
  public void initialize(Conditional annotation) {
    selected = annotation.selected();
    required = annotation.required();
    message = annotation.message();
    values = annotation.values();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    boolean valid = true;

    BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);
    String selectedValue = (String) beanWrapper.getPropertyValue(selected);
    if (Arrays.asList(values).contains(selectedValue)) {
      for (String propName : required) {
        String requiredValue = (String) beanWrapper.getPropertyValue(propName);
        valid = !StringUtils.isEmpty(requiredValue);
        if (!valid) {
          context.disableDefaultConstraintViolation();
          context
              .buildConstraintViolationWithTemplate(message)
              .addPropertyNode(propName)
              .addConstraintViolation();
        }
      }
    }

    return valid;
  }
}
