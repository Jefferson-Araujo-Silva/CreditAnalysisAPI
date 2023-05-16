package com.app.creditanalysis.service;

import com.app.creditanalysis.apicreditanalysis.ClientApiCreditAnalysis;
import com.app.creditanalysis.apicreditanalysis.clientdto.ClientDto;
import com.app.creditanalysis.controller.request.CreditAnalysisRequest;
import com.app.creditanalysis.controller.response.CreditAnalysisResponse;
import com.app.creditanalysis.exception.ClientNotFoundException;
import com.app.creditanalysis.mapper.CreditAnalysisEntityMapper;
import com.app.creditanalysis.mapper.CreditAnalysisMapper;
import com.app.creditanalysis.mapper.CreditAnalysisResponseMapper;
import com.app.creditanalysis.model.CreditAnalysis;
import com.app.creditanalysis.repository.CreditAnalysisRepository;
import com.app.creditanalysis.repository.entity.CreditAnalysisEntity;
import feign.FeignException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditAnalysisService {
    private final CreditAnalysisRepository creditAnalysisRepository;
    private final CreditAnalysisMapper creditAnalysisMapper;
    private final CreditAnalysisResponseMapper creditAnalysisResponseMapper;
    private final CreditAnalysisEntityMapper creditAnalysisEntityMapper;
    private final ClientApiCreditAnalysis clientApi;

    public CreditAnalysisResponse creditAnalising(CreditAnalysisRequest request) {
        UUID idClient = getIdClient(request.clientId());
        CreditAnalysis creditAnalysis = creditAnalysisMapper.from(request);
        CreditAnalysis creditAnalysisWithWithDrawAndCreditApproved = performCreditAnalysis(creditAnalysis);
        CreditAnalysisEntity creditAnalysisEntity = creditAnalysisEntityMapper.from(creditAnalysisWithWithDrawAndCreditApproved);
        CreditAnalysisEntity creditAnalysisSaved = saveCreditAnalysis(creditAnalysisEntity);
        return creditAnalysisResponseMapper.from(creditAnalysisSaved);
    }

    public CreditAnalysis performCreditAnalysis(CreditAnalysis creditAnalysis) {
        final BigDecimal CREDIT_INTEREST_RATE = BigDecimal.valueOf(0.15); // Juro anual de 15%
        final BigDecimal WITHDRAWAL_LIMIT_RATE = BigDecimal.valueOf(0.10); // Limite de saque de 10%
        BigDecimal monthlyIncome = creditAnalysis.monthlyIncome();
        BigDecimal requestedAmount = creditAnalysis.requestedAmount();
        BigDecimal fiftyPercent = monthlyIncome.multiply(BigDecimal.valueOf(0.5));


        BigDecimal approvedCreditAmount;
        BigDecimal withdrawalLimit;

        if (requestedAmount.compareTo(fiftyPercent) > 0) {
            approvedCreditAmount = monthlyIncome.multiply(CREDIT_INTEREST_RATE);
            withdrawalLimit = approvedCreditAmount.multiply(WITHDRAWAL_LIMIT_RATE);
        } else {
            approvedCreditAmount = monthlyIncome.multiply(BigDecimal.valueOf(0.30));
            withdrawalLimit = approvedCreditAmount.multiply(WITHDRAWAL_LIMIT_RATE);
        }

        return creditAnalysis.toBuilder().withdrawlLimitValue(withdrawalLimit)
                .approvedLimit(approvedCreditAmount)
                .build();
    }

    public CreditAnalysisEntity saveCreditAnalysis(CreditAnalysisEntity entity){
        return creditAnalysisRepository.save(entity);
    }
    public UUID getIdClient(UUID id) {
        ClientDto client;
        try {
            client = clientApi.getClientById(id);
        } catch (FeignException e) {
            throw new ClientNotFoundException("Client not found by id %s".formatted(id));
        }
        return client.id();
    }

    public List<CreditAnalysisResponse> getAllCreditAnalysis() {
        return new ArrayList<>();
    }

    public CreditAnalysisResponse findAnalysisById(UUID id) {
        final Double annualInterest = 12.0;
        return new CreditAnalysisResponse(UUID.randomUUID(), true, new BigDecimal("1000.00"), new BigDecimal("500"), annualInterest,
                UUID.randomUUID(), LocalDateTime.now());
    }
}
