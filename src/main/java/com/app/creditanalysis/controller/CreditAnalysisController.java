package com.app.creditanalysis.controller;

import com.app.creditanalysis.controller.request.CreditAnalysisRequest;
import com.app.creditanalysis.controller.response.CreditAnalysisResponse;
import com.app.creditanalysis.service.CreditAnalysisService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1.0/credit/analysis")
@RequiredArgsConstructor
public class CreditAnalysisController {
    @Autowired
    private final CreditAnalysisService creditAnalysisService;

    @PostMapping
    public CreditAnalysisResponse createCredit(@RequestBody CreditAnalysisRequest request) {
        return creditAnalysisService.creditAnalysing(request);
    }

    @GetMapping(path = "/client/cpf/{cpf}")
    public List<CreditAnalysisResponse> getAnalysisByCpf(@PathVariable(value = "cpf") String cpf) {
        return creditAnalysisService.findAnalysisByCpfClient(cpf);
    }

    @GetMapping
    public List<CreditAnalysisResponse> getAllAnalysis() {
        return creditAnalysisService.getAllCreditAnalysis();
    }

    @GetMapping(path = "/{id}")
    public List<CreditAnalysisResponse> getAnalysisById(@PathVariable(value = "id") UUID id) {
        return creditAnalysisService.findAnalysisById(id);
    }

    @GetMapping(path = "/client/{id}")
    public List<CreditAnalysisResponse> getAnalysisByIdClient(@PathVariable(value = "id") UUID id) {
        return creditAnalysisService.findAnalysisByIdClient(id);
    }
}