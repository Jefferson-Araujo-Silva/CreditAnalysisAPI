package com.app.creditanalysis.controller.request;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

public record CreditAnalysisRequest(UUID clientId, BigDecimal monthlyIncome, BigDecimal requestedAmount) {
    @Builder(toBuilder = true)
    public CreditAnalysisRequest {
    }
}

