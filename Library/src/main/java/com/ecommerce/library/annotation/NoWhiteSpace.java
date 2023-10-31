package com.ecommerce.library.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoWhiteSpaceValidator.class)
public @interface NoWhiteSpace {
    String message() default "Field should not be empty or contain white spaces";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
