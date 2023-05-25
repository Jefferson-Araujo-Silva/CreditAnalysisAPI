package com.app.creditanalysis.controller.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

public record CreditAnalysisRequest(
        @NotNull @NonNull
        UUID clientId,
        @NotNull @NonNull
        BigDecimal monthlyIncome,
        @NotNull @NonNull
        BigDecimal requestedAmount) {
    @Builder(toBuilder = true)
    public CreditAnalysisRequest {
    }
}

