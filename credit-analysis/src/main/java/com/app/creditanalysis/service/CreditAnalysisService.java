package com.app.creditanalysis.service;

import com.app.creditanalysis.controller.request.CreditAnalysisRequest;
import com.app.creditanalysis.controller.response.CreditAnalysisResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class CreditAnalysisService {
    public CreditAnalysisResponse creditAnalising(CreditAnalysisRequest request){
        return new CreditAnalysisResponse(UUID.randomUUID(), true, new BigDecimal("1000.00"),
                new BigDecimal("500"), 12.0, UUID.randomUUID(), LocalDateTime.now());
    }
}
