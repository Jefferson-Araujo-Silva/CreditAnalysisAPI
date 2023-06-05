package com.app.creditanalysis.util;
import com.app.creditanalysis.model.CreditAnalysis;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

public class RequestedValueValidator {
    private final Validator validator;

    public RequestedValueValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Melhor não validar um regra de negocio em uma classe utilitaria
    public boolean isValid(CreditAnalysis creditAnalysis) {
        BigDecimal monthlyIncome = creditAnalysis.monthlyIncome();
        BigDecimal requestedAmount = creditAnalysis.requestedAmount();

        return requestedAmount.compareTo(monthlyIncome) <= 0;
    }

    public Set<ConstraintViolation<CreditAnalysis>> validate(CreditAnalysis creditAnalysis) {
        return validator.validate(creditAnalysis);
    }
}