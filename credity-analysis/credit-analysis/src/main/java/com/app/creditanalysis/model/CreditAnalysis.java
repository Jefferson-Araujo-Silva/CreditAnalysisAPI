package com.app.creditanalysis.model;

import com.app.creditanalysis.exception.InvalidValueException;
import com.app.creditanalysis.exception.MaximumMonthlyIncomeExceededException;
import com.app.creditanalysis.exception.RequestedAmountExceedsMonthlyIncome;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

public record CreditAnalysis(Boolean approved, BigDecimal approvedLimit, BigDecimal requestedAmount, BigDecimal withdrawalLimitValue, UUID clientId,
                             BigDecimal monthlyIncome, LocalDateTime date, Double annualInterest) {
    @Builder(toBuilder = true)

    public CreditAnalysis(Boolean approved, BigDecimal approvedLimit, BigDecimal requestedAmount, BigDecimal withdrawalLimitValue, UUID clientId,
                          BigDecimal monthlyIncome, LocalDateTime date, Double annualInterest) {
        final BigDecimal minValueConsidering = new BigDecimal("1.00");
        if(requestedAmount.compareTo(monthlyIncome) > 0){
            throw new RequestedAmountExceedsMonthlyIncome("Requested amount exceeds the monthly income.");
        }
        if (requestedAmount.compareTo(minValueConsidering) < 0) {
            throw new InvalidValueException("Requested amount should not be negative");
        }
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
