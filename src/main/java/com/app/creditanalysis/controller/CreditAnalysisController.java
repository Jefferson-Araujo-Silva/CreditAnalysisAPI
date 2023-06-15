package com.app.creditanalysis.controller;

import com.app.creditanalysis.controller.request.CreditAnalysisRequest;
import com.app.creditanalysis.controller.response.CreditAnalysisResponse;
import com.app.creditanalysis.exceptionhandler.CreditAnalysisExceptionHandler;
import com.app.creditanalysis.service.CreditAnalysisService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1.0/credit/analysis")
@RequiredArgsConstructor
public class CreditAnalysisController {
    @Autowired
    private final CreditAnalysisService creditAnalysisService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CreditAnalysisExceptionHandler.class);
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreditAnalysisResponse createCredit(@RequestBody CreditAnalysisRequest request) {
        MDC.put("correlationId", UUID.randomUUID().toString());
        LOGGER.info("accessed endpoint method post");
        return creditAnalysisService.creditAnalysing(request);
    }

    @GetMapping
    public List<CreditAnalysisResponse> getAllAnalysisByClient(
            @RequestParam(value = "idClient",required = false) UUID idClient,
            @RequestParam(value = "cpf", required = false) String cpf
    ) {
        if(cpf != null){
            MDC.put("correlationId", UUID.randomUUID().toString());
            LOGGER.info("accessed endpoint method get with parameter cpf /%s".formatted(cpf));
            return creditAnalysisService.findAnalysisByCpfClient(cpf);
        } else if (idClient != null) {
            MDC.put("correlationId", UUID.randomUUID().toString());
            LOGGER.info("accessed endpoint method get with id parameter /%s".formatted(idClient));
            return creditAnalysisService.findAnalysisByIdClient(idClient);
        }
        MDC.put("correlationId", UUID.randomUUID().toString());
        LOGGER.info("accessed endpoint method get without parameter");
        return creditAnalysisService.getAllCreditAnalysis();
    }

    @GetMapping(path = "/{id}")
    public List<CreditAnalysisResponse> getAnalysisById(@PathVariable(value = "id") UUID id) {
        MDC.put("correlationId", UUID.randomUUID().toString());
        LOGGER.info("accessed endpoint method get /%s".formatted(id));
        return creditAnalysisService.findAnalysisById(id);
    }

}