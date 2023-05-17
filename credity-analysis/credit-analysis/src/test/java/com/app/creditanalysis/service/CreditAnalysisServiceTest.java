package com.app.creditanalysis.service;

import com.app.creditanalysis.apicreditanalysis.ClientApiCreditAnalysis;
import com.app.creditanalysis.apicreditanalysis.clientdto.ClientDto;
import com.app.creditanalysis.controller.response.CreditAnalysisResponse;
import com.app.creditanalysis.mapper.CreditAnalysisEntityMapper;
import com.app.creditanalysis.mapper.CreditAnalysisEntityMapperImpl;
import com.app.creditanalysis.mapper.CreditAnalysisMapper;
import com.app.creditanalysis.mapper.CreditAnalysisMapperImpl;
import com.app.creditanalysis.mapper.CreditAnalysisResponseMapper;
import com.app.creditanalysis.mapper.CreditAnalysisResponseMapperImpl;
import com.app.creditanalysis.repository.CreditAnalysisRepository;
import com.app.creditanalysis.repository.entity.CreditAnalysisEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CreditAnalysisServiceTest {
    @InjectMocks
    CreditAnalysisService creditAnalysisService;
    @Captor
    ArgumentCaptor<UUID> idClientArgumentCaptor;
    @Captor
    ArgumentCaptor<CreditAnalysisEntity> creditAnalysisEntityArgumentCaptor;
    @Mock
    private ClientApiCreditAnalysis creditAnalysisApi;
    @Mock
    private CreditAnalysisRepository creditAnalysisRepository;
    @Spy
    private CreditAnalysisEntityMapper creditAnalysisEntityMapper = new CreditAnalysisEntityMapperImpl();
    @Spy
    private CreditAnalysisResponseMapper creditAnalysisResponseMapper = new CreditAnalysisResponseMapperImpl();
    private final CreditAnalysisMapper creditAnalysisMapper = new CreditAnalysisMapperImpl();

    @Test
    public void should_create_new_credit_analysis_respose(a){}

    public static CreditAnalysisResponse creditAnalysisFactory() {
        return CreditAnalysisResponse.builder()
                .id(UUID.randomUUID())
                .date(LocalDateTime.now())
                .approved(true)
                .approvedLimit(new BigDecimal("30.00"))
                .withdrawalLimitValue(new BigDecimal("3.00"))
                .annualInterest(15.0)
                .clientId(UUID.fromString("8cecf090-d8c5-46af-88f9-4892d19a2c17"))
                .build();
    }
    public static ClientDto clientFactory(){
        return ClientDto.builder()
                .cpf("53887957806")
                .id(UUID.fromString("8cecf090-d8c5-46af-88f9-4892d19a2c17"))
                .build();
    }
}
