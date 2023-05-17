package com.app.creditanalysis.controller.request;

import com.app.creditanalysis.exception.InvalidValueException;
import com.app.creditanalysis.exception.RequestedAmountExceedsMonthlyIncome;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

public record CreditAnalysisRequest(UUID clientId, BigDecimal monthlyIncome, BigDecimal requestedAmount) {
    @Builder
    public CreditAnalysisRequest {
        final BigDecimal minValueConsidering = new BigDecimal("1.00");
        if (requestedAmount.compareTo(monthlyIncome) > 0) {
            throw new RequestedAmountExceedsMonthlyIncome("Requested amount exceeds the monthly income.");
        }
        if (requestedAmount.compareTo(minValueConsidering) < 0) {
            throw new InvalidValueException("Requested amount should not be negative");
        }
    }

}

