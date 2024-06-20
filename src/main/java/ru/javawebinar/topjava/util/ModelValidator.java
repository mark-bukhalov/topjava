package ru.javawebinar.topjava.util;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ModelValidator {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public <T> void validate(T model) {
        Set<ConstraintViolation<T>> validate = validator.validate(model);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }
    }
}
