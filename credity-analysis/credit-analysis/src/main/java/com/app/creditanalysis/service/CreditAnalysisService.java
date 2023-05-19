package com.app.creditanalysis.service;

import com.app.creditanalysis.apicreditanalysis.ClientApiCreditAnalysis;
import com.app.creditanalysis.apicreditanalysis.clientdto.ClientDto;
import com.app.creditanalysis.controller.request.CreditAnalysisRequest;
import com.app.creditanalysis.controller.response.CreditAnalysisResponse;
import com.app.creditanalysis.exception.ClientNotFoundException;
import com.app.creditanalysis.exception.CreditAnalysisNotFound;
import com.app.creditanalysis.mapper.CreditAnalysisEntityMapper;
import com.app.creditanalysis.mapper.CreditAnalysisMapper;
import com.app.creditanalysis.mapper.CreditAnalysisResponseMapper;
import com.app.creditanalysis.model.CreditAnalysis;
import com.app.creditanalysis.repository.CreditAnalysisRepository;
import com.app.creditanalysis.repository.entity.CreditAnalysisEntity;
import feign.FeignException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public CreditAnalysisResponse creditAnalysing(CreditAnalysisRequest request) {
        final CreditAnalysis creditAnalysis = creditAnalysisMapper.from(request);
        getIdClient(request.clientId());
        final CreditAnalysis creditAnalysisWithWithdrawalAndCreditApproved = performCreditAnalysis(creditAnalysis);
        final CreditAnalysisEntity creditAnalysisEntity = creditAnalysisEntityMapper.from(creditAnalysisWithWithdrawalAndCreditApproved);
        final CreditAnalysisEntity creditAnalysisSaved = saveCreditAnalysis(creditAnalysisEntity);
        return creditAnalysisResponseMapper.from(creditAnalysisSaved);
    }

    public CreditAnalysis performCreditAnalysis(CreditAnalysis creditAnalysis) {
        final BigDecimal Withdrawal_Limit_Rate = BigDecimal.valueOf(0.10); // Limite de saque de 10%
        final BigDecimal maxValueConsidering = new BigDecimal("50000.00");
        final BigDecimal requestedAmount = creditAnalysis.requestedAmount();
        final BigDecimal monthlyIncome;

        if (creditAnalysis.monthlyIncome().compareTo(maxValueConsidering) > 0) {
            monthlyIncome = maxValueConsidering;
        } else {
            monthlyIncome = creditAnalysis.monthlyIncome();
        }

        final BigDecimal fiftyPercent = monthlyIncome.multiply(BigDecimal.valueOf(0.5));

        final BigDecimal approvedCreditAmount;
        final BigDecimal withdrawalLimit;

        if (requestedAmount.compareTo(fiftyPercent) > 0) {
            approvedCreditAmount = monthlyIncome.multiply(BigDecimal.valueOf(0.15)).setScale(2, RoundingMode.HALF_EVEN);
        } else {
            approvedCreditAmount = monthlyIncome.multiply(BigDecimal.valueOf(0.30)).setScale(2, RoundingMode.HALF_EVEN);
        }
        withdrawalLimit = approvedCreditAmount.multiply(Withdrawal_Limit_Rate).setScale(2, RoundingMode.HALF_EVEN);

        return creditAnalysis.toBuilder().approved(true).withdrawalLimitValue(withdrawalLimit).approvedLimit(approvedCreditAmount).build();
    }

    public CreditAnalysisEntity saveCreditAnalysis(CreditAnalysisEntity entity) {
        return creditAnalysisRepository.save(entity);
    }

    public void getIdClient(UUID id) {
        ClientDto clientReturned = null;
        try {
            clientReturned = clientApi.getClientById(id);
        } catch (FeignException e) {
            if(clientReturned == null) {
                ClientNotFoundException clientNotFoundException = new ClientNotFoundException("Client not found by id %s".formatted(id));
                clientNotFoundException.printStackTrace();
                throw clientNotFoundException;
            }
        }
    }

    public List<CreditAnalysisResponse> getAllCreditAnalysis() {
        final List<CreditAnalysisEntity> allEntities = creditAnalysisRepository.findAll();
        final List<CreditAnalysisResponse> allResponses = new ArrayList<>();
        for (CreditAnalysisEntity creditAnalysisEntity : allEntities) {
            final CreditAnalysisResponse actualResponse = creditAnalysisResponseMapper.from(creditAnalysisEntity);
            allResponses.add(actualResponse);
        }
        return allResponses;
    }

    public Optional<CreditAnalysisEntity> findAnalysisById(UUID id) {
        final Optional<CreditAnalysisEntity> response = creditAnalysisRepository.findById(id);
        if (response.isEmpty()) {
            throw new CreditAnalysisNotFound("Credit Analysis with id %s not exists".formatted(id));
        }
        return response;
    }

    public CreditAnalysisResponse findAnalysisByIdClient(UUID id) {
        final CreditAnalysisEntity responseEntity = creditAnalysisRepository.findFirstByClientId(id);
        if (responseEntity == null) {
            throw new CreditAnalysisNotFound("Credit Analysis with id client %s not exists".formatted(id));
        }
        creditAnalysisResponseMapper.from(responseEntity);
        return creditAnalysisResponseMapper.from(responseEntity);
    }

    public CreditAnalysisResponse findAnalysisByCpfClient(String cpf) {
        final ClientDto client;
        try {
            client = clientApi.getClientbyCpf(cpf);
        } catch (FeignException e) {
            throw new ClientNotFoundException("Client not found by cpf %s".formatted(cpf));
        }
        return findAnalysisByIdClient(client.id());
    }

}
