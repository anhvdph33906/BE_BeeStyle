package com.datn.beestyle.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Stream;

public class EnumValueValidator implements ConstraintValidator<EnumValue, CharSequence> {
    private List<String> acceptedValues;
    private String enumValue;

    // convert enum to list
    @Override
    public void initialize(EnumValue enumValue) {
        this.enumValue = enumValue.name();
        acceptedValues = Stream.of(enumValue.enumClass().getEnumConstants())
                .map(enumConstant -> enumConstant.name().toUpperCase())
                .toList();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null || !acceptedValues.contains(value.toString().toUpperCase())) {
            String errorMessage = String.format("%s must be one of the following values: %s",
                    enumValue, String.join(", ", acceptedValues));

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
            return false;
        }

        return true;
    }
}
