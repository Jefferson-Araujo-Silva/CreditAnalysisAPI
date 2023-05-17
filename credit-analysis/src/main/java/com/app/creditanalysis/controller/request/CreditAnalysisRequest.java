package com.app.creditanalysis.controller.request;

import com.app.creditanalysis.exception.MaximumMonthlyIncomeExceededException;
import com.app.creditanalysis.exception.RequestedAmountExceedsMonthlyIncome;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

public record CreditAnalysisRequest(UUID clientId, BigDecimal monthlyIncome, BigDecimal requestedAmount) {
    @Builder
    public CreditAnalysisRequest {
        final double maxValueConsidering = 50000.00;
        if (monthlyIncome != null && monthlyIncome.compareTo(BigDecimal.valueOf(maxValueConsidering)) > 0) {
            throw new MaximumMonthlyIncomeExceededException("Monthly income exceeds the maximum allowed value.");
        }
        if (requestedAmount != null && requestedAmount.compareTo(monthlyIncome) > 0) {
            throw new RequestedAmountExceedsMonthlyIncome("Requested amount exceeds the monthly income.");
        }
    }

}

