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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@SuppressWarnings("UnusedReturnValue")
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
        final CreditAnalysis creditAnalysisToBeSaved = verifyIfApprovedOrNotApproved(creditAnalysis);
        final CreditAnalysisEntity creditAnalysisEntity = creditAnalysisEntityMapper.from(creditAnalysisToBeSaved);
        final CreditAnalysisEntity creditAnalysisSaved = saveCreditAnalysis(creditAnalysisEntity);
        return creditAnalysisResponseMapper.from(creditAnalysisSaved);
    }
    public CreditAnalysis verifyIfApprovedOrNotApproved(CreditAnalysis creditAnalysis){
        CreditAnalysis creditAnalysisToBeSaved;
        creditAnalysisToBeSaved = creditAnalysis.approved() ?
                performCreditAnalysis(creditAnalysis) :
                notApproved(creditAnalysis);
        return creditAnalysisToBeSaved;
    }
    public CreditAnalysis notApproved(CreditAnalysis creditAnalysis) {
        // new BigDecimal("0.0") substituir por BigDecimal.ZERO✅
        return creditAnalysis.toBuilder().approved(false).monthlyIncome(creditAnalysis.monthlyIncome()).approvedLimit(new BigDecimal("0.0"))
                .withdrawalLimitValue(BigDecimal.ZERO).date(LocalDateTime.now()).annualInterest(15.0).clientId(creditAnalysis.clientId())
                .requestedAmount(creditAnalysis.requestedAmount()).build();
    }

    public CreditAnalysis performCreditAnalysis(CreditAnalysis creditAnalysis) {
        final BigDecimal Withdrawal_Limit_Rate = BigDecimal.valueOf(0.10); // Limite de saque de 10%
        final BigDecimal maxValueConsidering = new BigDecimal("50000.00");
        final BigDecimal requestedAmount = creditAnalysis.requestedAmount();
        final BigDecimal monthlyIncome;
        final double percentIfRequestedAmountIsGreaterThenFiftyPercent = 0.15;
        final double percentIfRequestedAmountIsLessThenFiftyPercent = 0.30;

        if (creditAnalysis.monthlyIncome().compareTo(maxValueConsidering) > 0) {
            monthlyIncome = maxValueConsidering;
        } else {
            monthlyIncome = creditAnalysis.monthlyIncome();
        }

        final BigDecimal fiftyPercent = monthlyIncome.multiply(BigDecimal.valueOf(0.5));

        final BigDecimal approvedCreditAmount;
        final BigDecimal withdrawalLimit;

        if (requestedAmount.compareTo(fiftyPercent) > 0) {
            approvedCreditAmount =
                    monthlyIncome.multiply(BigDecimal.valueOf(percentIfRequestedAmountIsGreaterThenFiftyPercent)).setScale(2, RoundingMode.HALF_EVEN);
        } else {
            approvedCreditAmount =
                    monthlyIncome.multiply(BigDecimal.valueOf(percentIfRequestedAmountIsLessThenFiftyPercent)).setScale(2, RoundingMode.HALF_EVEN);
        }
        withdrawalLimit = approvedCreditAmount.multiply(Withdrawal_Limit_Rate).setScale(2, RoundingMode.HALF_EVEN);

        return creditAnalysis.toBuilder().approved(true).withdrawalLimitValue(withdrawalLimit).approvedLimit(approvedCreditAmount).build();
    }

    public CreditAnalysisEntity saveCreditAnalysis(CreditAnalysisEntity entity) {
        return creditAnalysisRepository.save(entity);
    }

    private UUID  getIdClient(UUID id) {
        try {
            ClientDto dto = clientApi.getClientById(id);
            if (dto == null) {
                ClientNotFoundException clientNotFoundException = new ClientNotFoundException("Client not found by id %s".formatted(id));
                clientNotFoundException.printStackTrace();
                throw clientNotFoundException;
            }
            return dto.id();
        } catch (FeignException e) {
            ClientNotFoundException clientNotFoundException = new ClientNotFoundException("Client not found by id %s".formatted(id));
            clientNotFoundException.printStackTrace();
            throw clientNotFoundException;
        }
    }

    public List<CreditAnalysisResponse> getAllCreditAnalysis() {
        final List<CreditAnalysisEntity> allEntities = creditAnalysisRepository.findAll();
        return allEntities.stream().map(creditAnalysisResponseMapper::from).collect(Collectors.toList());
    }

    public List<CreditAnalysisResponse> findAnalysisById(UUID id) {
        final Optional<CreditAnalysisEntity> responseEntity = creditAnalysisRepository.findById(id);
        if (responseEntity.isEmpty()) {
            throw new CreditAnalysisNotFound("Credit Analysis with id %s not exists".formatted(id));
        }
        List<CreditAnalysisEntity> response;
        response = responseEntity.stream().toList();

        return response.stream().map(creditAnalysisResponseMapper::from).collect(Collectors.toList());
    }

    public List<CreditAnalysisResponse> findAnalysisByIdClient(UUID id) {
        final List<CreditAnalysisEntity> responseEntity = creditAnalysisRepository.findAllByClientId(id);
        if (responseEntity.size() == 0) {
            throw new CreditAnalysisNotFound("Credit Analysis with id client %s not exists".formatted(id));
        }
        return responseEntity.stream().map(creditAnalysisResponseMapper::from).collect(Collectors.toList());
    }

    public List<CreditAnalysisResponse> findAnalysisByCpfClient(String cpf) {
        final ClientDto client;
        try {
            client = clientApi.getClientByCpf(cpf);
        } catch (FeignException e) {
            throw new ClientNotFoundException("Client not found by cpf %s".formatted(cpf));
        }
        return findAnalysisByIdClient(client.id());
    }

}
