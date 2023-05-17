package com.app.creditanalysis.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

public record CreditAnalysis(Boolean approved, BigDecimal approvedLimit, BigDecimal requestedAmount, BigDecimal withdrawalLimitValue, UUID clientId,
                             BigDecimal monthlyIncome, LocalDateTime date, Double annualInterest) {
    @Builder(toBuilder = true)

    public CreditAnalysis(Boolean approved, BigDecimal approvedLimit, BigDecimal requestedAmount, BigDecimal withdrawalLimitValue, UUID clientId,
                          BigDecimal monthlyIncome, LocalDateTime date, Double annualInterest) {
        this.approved = approved;
        this.approvedLimit = approvedLimit;
        this.requestedAmount = requestedAmount;
        this.withdrawalLimitValue = withdrawalLimitValue;
        this.clientId = clientId;
        this.monthlyIncome = monthlyIncome;
        this.date = date;
        this.annualInterest = 15.0;
    }
}
