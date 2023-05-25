package com.app.creditanalysis.model;

import com.app.creditanalysis.exception.InvalidValueException;
import com.app.creditanalysis.util.RequestedValueValidator;
import com.app.creditanalysis.util.ValidationCustom;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.springframework.boot.context.properties.bind.DefaultValue;

public record CreditAnalysis(
        Boolean approved,
        BigDecimal approvedLimit,
        @NotNull @DecimalMin(value = "1.0")
        BigDecimal requestedAmount,
        BigDecimal withdrawalLimitValue,
        @NotNull
        UUID clientId,
        @NotNull
        @DecimalMin(value = "1.0")
        BigDecimal monthlyIncome,
        LocalDateTime date,
        Double annualInterest
) {
    @Builder(toBuilder = true)

    public CreditAnalysis(Boolean approved, BigDecimal approvedLimit, BigDecimal requestedAmount, BigDecimal withdrawalLimitValue, UUID clientId,
                          BigDecimal monthlyIncome, LocalDateTime date, Double annualInterest) {
        final BigDecimal minValueConsidering = new BigDecimal("1.00");
        if(monthlyIncome != null && requestedAmount != null) {
            if (requestedAmount.compareTo(minValueConsidering) < 0) {
                throw new InvalidValueException("Requested amount should not be negative");
            } else if (monthlyIncome.compareTo(minValueConsidering) < 0) {
                throw new InvalidValueException("Monthly income value should not be negative");
            }
        }
        this.approvedLimit = approvedLimit;
        this.requestedAmount = requestedAmount;
        this.withdrawalLimitValue = withdrawalLimitValue;
        this.clientId = clientId;
        this.monthlyIncome = monthlyIncome;
        this.date = date;
        this.annualInterest = 15.0;
        RequestedValueValidator validator = new RequestedValueValidator();
        boolean isValid = validator.isValid(this);
        ValidationCustom.validator(this);
        this.approved = isValid;
    }
}
