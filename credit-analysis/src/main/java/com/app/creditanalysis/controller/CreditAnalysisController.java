package com.app.creditanalysis.controller;

import com.app.creditanalysis.apicreditanalysis.ClientApiCreditAnalysis;
import com.app.creditanalysis.apicreditanalysis.clientdto.ClientDto;
import com.app.creditanalysis.controller.request.CreditAnalysisRequest;
import com.app.creditanalysis.controller.response.CreditAnalysisResponse;
import com.app.creditanalysis.repository.entity.CreditAnalysisEntity;
import com.app.creditanalysis.service.CreditAnalysisService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
    private final ClientApiCreditAnalysis creditAnalysisApi;

    @PostMapping
    public CreditAnalysisResponse createCredit(@RequestBody CreditAnalysisRequest request) {
        return creditAnalysisService.creditAnalising(request);
    }

    @GetMapping(path = "/search-by-cpf/{cpf}")
    public ClientDto getClient(@RequestParam String cpf) {
        return creditAnalysisApi.getClientbyCpf(cpf);
    }

    @GetMapping
    public List<CreditAnalysisResponse> getAllAnalysis() {
        return creditAnalysisService.getAllCreditAnalysis();
    }

    @GetMapping(path = "/find-by-id/{id}")
    public Optional<CreditAnalysisEntity> findAnalysisById(@RequestParam UUID id) {
        return creditAnalysisService.findAnalysisById(id);
    }

    @GetMapping(path = "/find-by-id-client/{id}")
    public CreditAnalysisResponse findAnalysisByIdClient(@RequestParam UUID id) {
        return creditAnalysisService.findAnalysisByIdClient(id);
    }
}
