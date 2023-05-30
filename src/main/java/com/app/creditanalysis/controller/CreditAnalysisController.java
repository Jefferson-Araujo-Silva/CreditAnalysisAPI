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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1.0/credit/analysis")
@RequiredArgsConstructor
public class CreditAnalysisController {
    @Autowired
    private final CreditAnalysisService creditAnalysisService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CreditAnalysisController.class);
    private static final String PATH_DEFAULT_ENDPOINT= "v1.0/credit/analysis";
    @PostMapping
    public CreditAnalysisResponse createCredit(@RequestBody CreditAnalysisRequest request) {
        LOGGER.info("Received an post requisition at endpoint v1.0/credit/analysis");
        return creditAnalysisService.creditAnalysing(request);
    }

    @GetMapping(path = "/find-by-cpf/{cpf}")
    public List<CreditAnalysisResponse> findAnalysisByCpfClient(@RequestParam String cpf) {
        LOGGER.info("Received an get requisition at endpoint %s/find-by-cpf/".formatted(PATH_DEFAULT_ENDPOINT));
        return creditAnalysisService.findAnalysisByCpfClient(cpf);
    }

    @GetMapping
    public List<CreditAnalysisResponse> getAllAnalysis() {
        LOGGER.info("Received an get requisition at endpoint %s".formatted(PATH_DEFAULT_ENDPOINT));
        return creditAnalysisService.getAllCreditAnalysis();
    }

    @GetMapping(path = "/find-by-id/{id}")
    public List<CreditAnalysisResponse> findAnalysisById(@RequestParam UUID id) {
        LOGGER.info("Received an post requisition at endpoint %s/find-by-id/".formatted(PATH_DEFAULT_ENDPOINT));
        return creditAnalysisService.findAnalysisById(id);
    }

    @GetMapping(path = "/find-by-id-client/{id}")
    public List<CreditAnalysisResponse> findAnalysisByIdClient(@RequestParam UUID id) {
        LOGGER.info("Received an post requisition at endpoint %s/find-by-id-client/".formatted(PATH_DEFAULT_ENDPOINT));
        return creditAnalysisService.findAnalysisByIdClient(id);
    }
}