package com.app.creditanalysis.mapper;

import com.app.creditanalysis.controller.request.CreditAnalysisRequest;
import com.app.creditanalysis.model.CreditAnalysis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CreditAnalysisMapper {
    @Mapping(target = "monthlyIncome", source = "monthlyIncome")
    CreditAnalysis from(CreditAnalysisRequest creditAnalysisRequest);
}