package com.app.creditanalysis.mapper;

import com.app.creditanalysis.controller.request.CreditAnalysisRequest;
import com.app.creditanalysis.model.CreditAnalysis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreditAnalysisMapper {
    @Mapping(target = "monthlyIncome", source = "monthlyIncome")
    CreditAnalysis from(CreditAnalysisRequest creditAnalysisRequest);
}