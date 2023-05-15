package com.app.creditanalysis.controller;

import com.app.creditanalysis.controller.request.CreditAnalysisRequest;
import com.app.creditanalysis.controller.response.CreditAnalysisResponse;
import com.app.creditanalysis.service.CreditAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1.0/credit-analysis")
@RequiredArgsConstructor
public class CreditAnalysisController {
    private CreditAnalysisService creditAnalysisService;
    @PostMapping
    public CreditAnalysisResponse createCredit(@RequestBody CreditAnalysisRequest request){
        return creditAnalysisService.creditAnalising(request);
    }
}
