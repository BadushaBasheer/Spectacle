package com.ecommerce.library.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoWhiteSpaceValidator implements ConstraintValidator<NoWhiteSpace, String> {
    @Override
    public void initialize(NoWhiteSpace constraintAnnotation) {
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return !value.contains(" "); // Check if the string contains white spaces.
    }
}
