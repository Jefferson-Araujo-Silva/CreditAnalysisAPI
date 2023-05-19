package com.app.creditanalysis.util;

import com.app.creditanalysis.model.CreditAnalysis;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

public class ValidationCustom {

    private ValidationCustom(){
    }
    public static <T> T validator(T t) {
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<T>> validate = validator.validate(t);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }

        return t;
    }

    public boolean isValid(CreditAnalysis creditAnalysis, ConstraintValidatorContext context) {
        if (creditAnalysis == null) {
            return true;  // Considera válido se o objeto for nulo
        }

        BigDecimal monthlyIncome = creditAnalysis.monthlyIncome();
        BigDecimal requestedAmount = creditAnalysis.requestedAmount();

        return requestedAmount.compareTo(monthlyIncome) <= 0;
    }
}
