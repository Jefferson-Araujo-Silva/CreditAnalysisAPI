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
import com.app.creditanalysis.exception.CreditAnalysisNotFound;
import com.app.creditanalysis.exception.InvalidValueException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

@SuppressWarnings("ALL")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CreditAnalysisServiceTest {
    @InjectMocks
    CreditAnalysisService creditAnalysisService;
    @Captor
    ArgumentCaptor<UUID> idClientArgumentCaptor;
    @Captor
    ArgumentCaptor<String> cpfClientArgumentCaptor;
    @Captor
    ArgumentCaptor<CreditAnalysisEntity> creditAnalysisEntityArgumentCaptor;
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

    public static CreditAnalysisRequest creditAnalysisRequestFactory() {
        return CreditAnalysisRequest.builder().requestedAmount(new BigDecimal("5.00")).monthlyIncome(new BigDecimal("100.0"))
                .clientId(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560")).build();
    }

    public static CreditAnalysisEntity creditAnalysisEntityFactory() {
        return CreditAnalysisEntity.builder().approved(true).approvedLimit(new BigDecimal("30.00")).withdrawalLimitValue(new BigDecimal("3.00"))
                .annualInterest(15.0).clientId(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560")).monthlyIncome(new BigDecimal("100.00"))
                .requestedAmount(new BigDecimal("5.00")).annualInterest(15.0).build();
    }

    public static ClientDto clientFactory() {
        return ClientDto.builder().id(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560")).build();
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

        when(creditAnalysisRepository.findAllByClientId(idClientArgumentCaptor.capture())).thenReturn(List.of(creditAnalysisEntityFactory()));

        final List<CreditAnalysisEntity> entity = new ArrayList<>();
        entity.add(creditAnalysisEntityFactory());
        final List<CreditAnalysisResponse> response =
                creditAnalysisService.findAnalysisByIdClient(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560"));
        assertEquals(entity.get(0).getAnnualInterest(), response.get(0).annualInterest());
        assertEquals(entity.get(0).getApproved(), response.get(0).approved());
        assertEquals(entity.get(0).getApprovedLimit(), response.get(0).approvedLimit());
        assertEquals(entity.get(0).getWithdrawlLimitValue(), response.get(0).withdrawalLimitValue());
    }

    @SuppressWarnings("JoinDeclarationAndAssignmentJava")
    @Test
    public void should_return_where_client_where_cpf_client_is_53887957806() {
        List<CreditAnalysisEntity> responseList = new ArrayList<>();
        responseList.add(creditAnalysisEntityFactory());
        when(creditAnalysisApi.getClientByCpf(cpfClientArgumentCaptor.capture())).thenReturn(List.of(clientFactory()));
        when(creditAnalysisRepository.findAllByClientId(idClientArgumentCaptor.capture())).thenReturn(responseList);

        List<CreditAnalysisResponse> response;
        response = creditAnalysisService
                .findAnalysisByCpfClient("53887957806");
        assertEquals(responseList.get(0).getId(), response.get(0).id());
        assertEquals(responseList.get(0).getClientId(), response.get(0).clientId());
        assertEquals(responseList.get(0).getApproved(), response.get(0).approved());
    }

    @Test
    public void should_return_credit_analysis_by_id() {
        CreditAnalysisEntity entity = creditAnalysisEntityFactory();
        Optional<CreditAnalysisEntity> returned = Optional.of(entity);
        when(creditAnalysisRepository.findById(idClientArgumentCaptor.capture())).thenReturn(returned);

        List<CreditAnalysisResponse> response = creditAnalysisService.findAnalysisById(entity.getId());
        assertNotNull(response);
        assertEquals(idClientArgumentCaptor.getValue(), response.get(0).id());
    }

    @Test

    public void should_return_all_credit_analysis() {
        List<CreditAnalysisEntity> returned = List.of(creditAnalysisEntityFactory());
        when(creditAnalysisRepository.findAll()).thenReturn(returned);

        List<CreditAnalysisResponse> response = creditAnalysisService.getAllCreditAnalysis();
        assertNotNull(response);
        assertEquals(returned.get(0).getApproved(), response.get(0).approved());
        assertEquals(returned.get(0).getId(), response.get(0).id());
        assertEquals(returned.get(0).getApprovedLimit(), response.get(0).approvedLimit());
        assertEquals(returned.get(0).getWithdrawlLimitValue(), response.get(0).withdrawalLimitValue());
        assertEquals(returned.get(0).getClientId(), response.get(0).clientId());
    }

    @Test

    public void should_throw_credit_analysis_analysis_not_found_by_id() {
        Optional<CreditAnalysisEntity> returned = Optional.empty();
        when(creditAnalysisRepository.findById(idClientArgumentCaptor.capture())).thenReturn(returned);

        CreditAnalysisNotFound exception =
                assertThrows(CreditAnalysisNotFound.class, () -> creditAnalysisService.findAnalysisById(creditAnalysisEntityFactory().getId()));
        assertEquals("Credit Analysis with id %s not exists".formatted(idClientArgumentCaptor.getValue()), exception.getMessage());

    }

    @Test
    public void should_throws_credit_analysis_not_found_where_id_client_is_1f017304_c7cf_45cb_e2c_5f6ce1f22560() {

        when(creditAnalysisRepository.findAllByClientId(idClientArgumentCaptor.capture())).thenReturn(new ArrayList<>());

        final List<CreditAnalysisEntity> entity = new ArrayList<>();
        entity.add(creditAnalysisEntityFactory());
        final CreditAnalysisNotFound creditAnalysisNotFound = assertThrows(CreditAnalysisNotFound.class,
                () -> creditAnalysisService.findAnalysisByIdClient(UUID.fromString("1f017304-c7cf-45cb-8e2c-5f6ce1f22560")));
        assertEquals("Credit Analysis with id client 1f017304-c7cf-45cb-8e2c-5f6ce1f22560 not exists", creditAnalysisNotFound.getMessage());
    }

    @Test
    public void should_throws_client_not_found_where_id_client_is_1f017304_c7cf_45cb_e2c_5f6ce1f22560() {
        when(creditAnalysisApi.getClientById(idClientArgumentCaptor.capture())).thenReturn(null);

        final ClientNotFoundException clientNotFoundException =
                assertThrows(ClientNotFoundException.class, () -> creditAnalysisService.creditAnalysing(creditAnalysisRequestFactory()));
        assertEquals("Client not found by id 1f017304-c7cf-45cb-8e2c-5f6ce1f22560", clientNotFoundException.getMessage());
    }
    @Test
    public void should_throws_HttpMessageNotReadableException_when_requested_amount_is_null(){
        CreditAnalysisRequest request = creditAnalysisRequestFactory().toBuilder().requestedAmount(null).build();
        InvalidValueException expected = assertThrows(InvalidValueException.class,
                ()-> creditAnalysisService.creditAnalysing(request));
        assertEquals("Values in credit analysis JSON must not be null", expected.getMessage());
    }

    @Test
    public void if_the_monthly_income_value_is_greater_than_50000_and_request_value_is_less_then_50_percent_approved_limit_will_be_15000() {
        CreditAnalysis request =
                CreditAnalysis.builder().clientId(UUID.randomUUID()).monthlyIncome(new BigDecimal("55000.00")).requestedAmount(new BigDecimal("50.0"))
                        .build();

        final BigDecimal approvedLimitValueExpected = new BigDecimal("15000.00");
        BigDecimal approvedLimitValueResponse = creditAnalysisService.performCreditAnalysis(request).approvedLimit();

        assertEquals(approvedLimitValueExpected, approvedLimitValueResponse);
    }

    @Test
    public void if_the_monthly_income_value_is_greater_than_50000_and_request_value_is_more_then_50_percent_approved_limit_will_be_7500() {
        CreditAnalysis request = CreditAnalysis.builder().clientId(UUID.randomUUID()).monthlyIncome(new BigDecimal("55000.00"))
                .requestedAmount(new BigDecimal("50000.00")).build();

        final BigDecimal approvedLimitValueExpected = new BigDecimal("7500.00");
        BigDecimal approvedLimitValueResponse = creditAnalysisService.performCreditAnalysis(request).approvedLimit();

        assertEquals(approvedLimitValueExpected, approvedLimitValueResponse);
    }

    @Test
    public void should_not_approve_credit_if_requested_amount_is_greater_then_monthly_income() {
        CreditAnalysisEntity returned = CreditAnalysisEntity.builder().clientId(UUID.randomUUID()).monthlyIncome(new BigDecimal("50.00"))
                .requestedAmount(new BigDecimal("60.0")).build();
        when(creditAnalysisApi.getClientById(idClientArgumentCaptor.capture())).thenReturn(clientFactory());
        when(creditAnalysisService.saveCreditAnalysis(creditAnalysisEntityArgumentCaptor.capture())).thenReturn(returned);

        CreditAnalysisRequest request =
                creditAnalysisRequestFactory().toBuilder().requestedAmount(new BigDecimal("60.0")).monthlyIncome(new BigDecimal("50.0")).build();
        creditAnalysisService.creditAnalysing(request);
        CreditAnalysisEntity response = creditAnalysisEntityArgumentCaptor.getValue();
        Boolean expectedAproved = false;
        assertEquals(expectedAproved, response.getApproved());
    }

    @Test
    public void should_throws_client_not_found_exception_by_id() {
        when(creditAnalysisApi.getClientById(idClientArgumentCaptor.capture())).thenThrow(FeignException.class);

        final CreditAnalysisRequest creditAnalysisRequest = creditAnalysisRequestFactory();
        ClientNotFoundException clientNotFoundException =
                assertThrows(ClientNotFoundException.class, () -> creditAnalysisService.creditAnalysing(creditAnalysisRequest));

        assertEquals("Client not found by id 1f017304-c7cf-45cb-8e2c-5f6ce1f22560", clientNotFoundException.getMessage());
    }

    @Test
    public void should_throws_client_not_found_exception_by_cpf() {
        when(creditAnalysisApi.getClientByCpf(cpfClientArgumentCaptor.capture())).thenThrow(FeignException.class);

        ClientNotFoundException clientNotFoundException =
                assertThrows(ClientNotFoundException.class, () -> creditAnalysisService.findAnalysisByCpfClient("53887957806"));

        assertEquals("Client not found by cpf %s".formatted(cpfClientArgumentCaptor.getValue()), clientNotFoundException.getMessage());
    }

    @Test
    public void should_throws_InvalidValueException_when_requested_amount_is_negative() {
        CreditAnalysisRequest requestWithInvalidValue = creditAnalysisRequestFactory().toBuilder()
                .clientId(UUID.randomUUID())
                .requestedAmount(new BigDecimal(-1))
                .build();

        InvalidValueException invalidValueException =
                assertThrows(InvalidValueException.class, () -> creditAnalysisService.creditAnalysing(requestWithInvalidValue));

        assertEquals("Requested amount should not be negative", invalidValueException.getMessage());
    }

    @Test
    public void should_throws_InvalidValueException_when_monthly_income_is_negative() {
        CreditAnalysisRequest requestWithInvalidValue = creditAnalysisRequestFactory().toBuilder()
                .clientId(UUID.randomUUID())
                .requestedAmount(new BigDecimal(1))
                .monthlyIncome(new BigDecimal(-1)).build();

        InvalidValueException invalidValueException =
                assertThrows(InvalidValueException.class, () -> creditAnalysisService.creditAnalysing(requestWithInvalidValue));

        assertEquals("Monthly income value should not be negative", invalidValueException.getMessage());
    }
    @Test
    public void withdrawal_threshold_must_be_10_percent_of_the_approved_amount() {
        final CreditAnalysis creditAnalysis =
                CreditAnalysis.builder().clientId(UUID.randomUUID()).monthlyIncome(new BigDecimal("100.00")).requestedAmount(new BigDecimal("40.0"))
                        .build();

        final BigDecimal withdrawalLimitValueExpected = new BigDecimal("3.00");
        final BigDecimal withdrawalLimitValueReturned = creditAnalysisService.performCreditAnalysis(creditAnalysis).withdrawalLimitValue();
        assertEquals(withdrawalLimitValueExpected, withdrawalLimitValueReturned);
    }

    @Test
    public void should_return_30_percent_of_the_total_amount_of_the_monthly_income_as_credit_if_requested_value_is_less_then_50_percent_of_monthly_income() {
        final CreditAnalysis creditAnalysis =
                CreditAnalysis.builder().monthlyIncome(new BigDecimal("100.00")).requestedAmount(new BigDecimal("40.0")).clientId(UUID.randomUUID())
                        .build();

        final BigDecimal creditLimitValueExpected = new BigDecimal("30.00");
        final BigDecimal creditLimitValueReturned = creditAnalysisService.performCreditAnalysis(creditAnalysis).approvedLimit();
        assertEquals(creditLimitValueExpected, creditLimitValueReturned);
    }

    @Test
    public void should_not_aprove_when_requested_amount_is_greater_then_monthly_income() {
        CreditAnalysisEntity entity =
                creditAnalysisEntityFactory().toBuilder().requestedAmount(new BigDecimal("10")).monthlyIncome(new BigDecimal("9")).approved(false)
                        .build();
        when(creditAnalysisApi.getClientById(idClientArgumentCaptor.capture())).thenReturn(clientFactory());
        when(creditAnalysisRepository.save(creditAnalysisEntityArgumentCaptor.capture())).thenReturn(entity);
        final CreditAnalysisRequest request =
                creditAnalysisRequestFactory().toBuilder().requestedAmount(new BigDecimal("10")).monthlyIncome(new BigDecimal("9")).build();
        CreditAnalysisResponse response = creditAnalysisService.creditAnalysing(request);
        assertEquals(false, response.approved());
    }
}
