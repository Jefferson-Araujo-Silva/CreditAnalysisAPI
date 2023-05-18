package com.app.creditanalysis.service;

import com.app.creditanalysis.apicreditanalysis.ClientApiCreditAnalysis;
import com.app.creditanalysis.apicreditanalysis.clientdto.ClientDto;
import com.app.creditanalysis.controller.request.CreditAnalysisRequest;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
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
   @Spy CreditAnalysisMapper creditAnalysisMapper = new CreditAnalysisMapperImpl();
    @Spy
    private CreditAnalysisResponseMapper creditAnalysisResponseMapper = new CreditAnalysisResponseMapperImpl();

    @Test
    public void should_create_new_credit_analysis_response() {
        ClientDto clientDto = clientFactory();
        when(creditAnalysisApi.getClientById(idClientArgumentCaptor.capture())).thenReturn(clientDto);
        when(creditAnalysisRepository.save(creditAnalysisEntityArgumentCaptor.capture()))
                .thenReturn(creditAnalysisEntityFactory());

        final CreditAnalysisRequest request = creditAnalysisRequestFactory();
        final CreditAnalysisResponse response = creditAnalysisService.creditAnalising(request);

        assertNotNull(response);
        assertNotNull(response.id());
        CreditAnalysisEntity entity = creditAnalysisEntityArgumentCaptor.getValue();
        assertEquals(response.annualInterest(), entity.getAnnualInterest());
        assertEquals(response.approved(), entity.getApproved());
        assertEquals(response.clientId(), entity.getClientId());
        assertEquals(response.withdrawalLimitValue(), entity.getWithdrawlLimitValue());
        assertEquals(response.approvedLimit(), entity.getApprovedLimit());
    }

    @Test
    public void should_return_where_client_where_id_client_is_1f017304_c7cf_45cb_e2c_5f6ce1f22560(){
        when(creditAnalysisRepository.findFirstByClientId(idClientArgumentCaptor.capture())).thenReturn(creditAnalysisEntityFactory());

        final CreditAnalysisEntity entity = creditAnalysisEntityFactory();
        final CreditAnalysisResponse response = creditAnalysisService
                .findAnalysisByIdClient(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560"));
        assertEquals(entity.getAnnualInterest(), response.annualInterest());
        assertEquals(entity.getApproved(), response.approved());
        assertEquals(entity.getApprovedLimit(), response.approvedLimit());
        assertEquals(entity.getWithdrawlLimitValue(), response.withdrawalLimitValue());
    }

    public static CreditAnalysisResponse creditAnalysisResponseFactory() {
        return CreditAnalysisResponse.builder()
                .id(UUID.randomUUID())
                .date(LocalDateTime.now())
                .approved(true).approvedLimit(new BigDecimal("30.00"))
                .withdrawalLimitValue(new BigDecimal("3.00")).annualInterest(15.0)
                .clientId(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560"))
                .build();
    }
    public static CreditAnalysisRequest creditAnalysisRequestFactory(){
        return CreditAnalysisRequest.builder()
                .requestedAmount(new BigDecimal("5.00"))
                .monthlyIncome(new BigDecimal("100.0"))
                .clientId(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560"))
                .build();
    }
    public static CreditAnalysisEntity creditAnalysisEntityFactory() {
        return CreditAnalysisEntity.builder()
                .date(LocalDateTime.now())
                .approved(true).approvedLimit(new BigDecimal("30.00"))
                .withdrawalLimitValue(new BigDecimal("3.00")).annualInterest(15.0)
                .clientId(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560"))
                .monthlyIncome(new BigDecimal("100.00"))
                .requestedAmount(new BigDecimal("5.00"))
                .annualInterest(15.0)
                .build();
    }
    public static ClientDto clientFactory() {
        return ClientDto.builder().cpf("53887957806").
                id(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560"))
                .build();
    }
}
