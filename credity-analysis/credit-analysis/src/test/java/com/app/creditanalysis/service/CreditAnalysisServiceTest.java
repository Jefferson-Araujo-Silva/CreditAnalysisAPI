package com.app.creditanalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.app.creditanalysis.apicreditanalysis.ClientApiCreditAnalysis;
import com.app.creditanalysis.apicreditanalysis.clientdto.ClientDto;
import com.app.creditanalysis.controller.request.CreditAnalysisRequest;
import com.app.creditanalysis.controller.response.CreditAnalysisResponse;
import com.app.creditanalysis.exception.ClientNotFoundException;
import com.app.creditanalysis.exception.MaximumMonthlyIncomeExceededException;
import com.app.creditanalysis.exception.RequestedAmountExceedsMonthlyIncome;
import com.app.creditanalysis.mapper.CreditAnalysisEntityMapper;
import com.app.creditanalysis.mapper.CreditAnalysisEntityMapperImpl;
import com.app.creditanalysis.mapper.CreditAnalysisMapper;
import com.app.creditanalysis.mapper.CreditAnalysisMapperImpl;
import com.app.creditanalysis.mapper.CreditAnalysisResponseMapper;
import com.app.creditanalysis.mapper.CreditAnalysisResponseMapperImpl;
import com.app.creditanalysis.model.CreditAnalysis;
import com.app.creditanalysis.repository.CreditAnalysisRepository;
import com.app.creditanalysis.repository.entity.CreditAnalysisEntity;
import feign.FeignException;
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
    @Captor
    ArgumentCaptor<CreditAnalysis> creditAnalysisArgumentCaptor;
    @Spy
    CreditAnalysisMapper creditAnalysisMapper = new CreditAnalysisMapperImpl();
    @Mock
    private ClientApiCreditAnalysis creditAnalysisApi;
    @Mock
    private CreditAnalysisRepository creditAnalysisRepository;
    @Spy
    private CreditAnalysisEntityMapper creditAnalysisEntityMapper = new CreditAnalysisEntityMapperImpl();
    @Spy
    private CreditAnalysisResponseMapper creditAnalysisResponseMapper = new CreditAnalysisResponseMapperImpl();

    public static CreditAnalysisResponse creditAnalysisResponseFactory() {
        return CreditAnalysisResponse.builder().id(UUID.randomUUID()).date(LocalDateTime.now()).approved(true).approvedLimit(new BigDecimal("30.00"))
                .withdrawalLimitValue(new BigDecimal("3.00")).annualInterest(15.0).clientId(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560"))
                .build();
    }

    public static CreditAnalysisRequest creditAnalysisRequestFactory() {
        return CreditAnalysisRequest.builder().requestedAmount(new BigDecimal("5.00")).monthlyIncome(new BigDecimal("100.0"))
                .clientId(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560")).build();
    }

    public static CreditAnalysisEntity creditAnalysisEntityFactory() {
        return CreditAnalysisEntity.builder().date(LocalDateTime.now()).approved(true).approvedLimit(new BigDecimal("30.00"))
                .withdrawalLimitValue(new BigDecimal("3.00")).annualInterest(15.0).clientId(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560"))
                .monthlyIncome(new BigDecimal("100.00")).requestedAmount(new BigDecimal("5.00")).annualInterest(15.0).build();
    }

    public static ClientDto clientFactory() {
        return ClientDto.builder().cpf("53887957806").id(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560")).build();
    }

    @Test
    public void should_create_new_credit_analysis_response() {
        ClientDto clientDto = clientFactory();
        when(creditAnalysisApi.getClientById(idClientArgumentCaptor.capture())).thenReturn(clientDto);
        when(creditAnalysisRepository.save(creditAnalysisEntityArgumentCaptor.capture())).thenReturn(creditAnalysisEntityFactory());

        final CreditAnalysisRequest request = creditAnalysisRequestFactory();
        final CreditAnalysisResponse response = creditAnalysisService.creditAnalysing(request);

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
    public void should_return_where_client_where_id_client_is_1f017304_c7cf_45cb_e2c_5f6ce1f22560() {
        when(creditAnalysisRepository.findFirstByClientId(idClientArgumentCaptor.capture())).thenReturn(creditAnalysisEntityFactory());

        final CreditAnalysisEntity entity = creditAnalysisEntityFactory();
        final CreditAnalysisResponse response = creditAnalysisService.findAnalysisByIdClient(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560"));
        assertEquals(entity.getAnnualInterest(), response.annualInterest());
        assertEquals(entity.getApproved(), response.approved());
        assertEquals(entity.getApprovedLimit(), response.approvedLimit());
        assertEquals(entity.getWithdrawlLimitValue(), response.withdrawalLimitValue());
    }

    @Test
    public void if_the_monthly_income_value_is_greater_than_50000_and_request_value_is_less_then_50_percent_approved_limit_will_be_7500() {
        CreditAnalysis request = CreditAnalysis.builder()
                .monthlyIncome(new BigDecimal("55000.00"))
                .requestedAmount(new BigDecimal("50.0")).build();

        final BigDecimal approvedLimitValueExpected = new BigDecimal("15000.00");
        BigDecimal approvedLimitValueResponse = creditAnalysisService.performCreditAnalysis(request).approvedLimit();

        assertEquals(approvedLimitValueExpected, approvedLimitValueResponse);
    }

    @Test
    public void should_throws_requested_amount_exceeds_monthly_income(){
        CreditAnalysisRequest request = CreditAnalysisRequest.builder()
                .clientId(UUID.randomUUID())
                .monthlyIncome(new BigDecimal("50.00"))
                .requestedAmount(new BigDecimal("60.0")).build();

        assertThrows(RequestedAmountExceedsMonthlyIncome.class, ()-> creditAnalysisService.creditAnalysing(request));
    }
    @Test
    public void should_throws_client_not_found_exception(){
        when(creditAnalysisApi.getClientById(idClientArgumentCaptor.capture())).thenThrow(FeignException.class);

        final CreditAnalysisRequest creditAnalysisRequest = creditAnalysisRequestFactory();
        ClientNotFoundException clientNotFoundException = assertThrows(ClientNotFoundException.class,
                ()-> creditAnalysisService.creditAnalysing(creditAnalysisRequest));

        assertEquals("Client not found by id 1f017304-c7cf-45cb-8e2c-5f6ce1f22560", clientNotFoundException.getMessage());
    }

    @Test
    public void withdrawal_threshold_must_be_10_percent_of_the_approved_amount(){
        final CreditAnalysis creditAnalysis = CreditAnalysis.builder()
                .monthlyIncome(new BigDecimal("100.00"))
                .requestedAmount(new BigDecimal("40.0"))
                .build();

        final BigDecimal withdrawalLimitValueExpected = new BigDecimal("3.00");
        final BigDecimal withdrawalLimitValueReturned = creditAnalysisService
                .performCreditAnalysis(creditAnalysis).withdrawalLimitValue();
        assertEquals(withdrawalLimitValueExpected, withdrawalLimitValueReturned);
    }

    @Test
    public void should_return_30_percent_of_the_total_amount_of_the_monthly_income_as_credit_if_requested_value_is_less_then_50_percent_of_monthly_income(){
        final CreditAnalysis creditAnalysis = CreditAnalysis.builder()
                .monthlyIncome(new BigDecimal("100.00"))
                .requestedAmount(new BigDecimal("40.0"))
                .build();

        final BigDecimal creditLimitValueExpected = new BigDecimal("30.00");
        final BigDecimal creditLimitValueReturned = creditAnalysisService
                .performCreditAnalysis(creditAnalysis).approvedLimit();
        assertEquals(creditLimitValueExpected, creditLimitValueReturned);
    }

}
