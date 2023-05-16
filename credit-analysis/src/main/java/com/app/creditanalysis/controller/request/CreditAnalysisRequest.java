package com.app.creditanalysis.controller.request;

import com.app.creditanalysis.exception.MaximumMonthlyIncomeExceededException;
import com.app.creditanalysis.exception.RequestedAmountExceedsMonthlyIncome;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

public record CreditAnalysisRequest(UUID clientId, BigDecimal monthlyIncome,
                                    BigDecimal requestedAmount) {
    @Builder
    public CreditAnalysisRequest {
        if (monthlyIncome != null && monthlyIncome.compareTo(BigDecimal.valueOf(50000.00)) > 0) {
            throw new MaximumMonthlyIncomeExceededException("Monthly income exceeds the maximum allowed value.");
        }
        if (requestedAmount != null && requestedAmount.compareTo(monthlyIncome) > 0) {
            throw new RequestedAmountExceedsMonthlyIncome("Requested amount exceeds the monthly income.");
        }
    }
}

