package com.yoloo.server.common.validation;

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

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

    try {
      String selectedValue = BeanUtils.getProperty(value, selected);

      /*if (Arrays.asList(values).contains(selectedValue)) {
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
      }*/
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
    }

    return valid;
  }
}
