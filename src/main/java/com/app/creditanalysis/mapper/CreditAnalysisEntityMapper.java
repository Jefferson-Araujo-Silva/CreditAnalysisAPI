package com.app.creditanalysis.mapper;

import com.app.creditanalysis.model.CreditAnalysis;
import com.app.creditanalysis.repository.entity.CreditAnalysisEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CreditAnalysisEntityMapper {

    @Mapping(source = "approved", target = "approved")
    @Mapping(source = "approvedLimit", target = "approvedLimit")
    @Mapping(source = "requestedAmount", target = "requestedAmount")
    @Mapping(source = "withdrawalLimitValue", target = "withdrawalLimitValue")
    @Mapping(source = "annualInterest", target = "annualInterest")
    @Mapping(source = "monthlyIncome", target = "monthlyIncome")
    CreditAnalysisEntity from(CreditAnalysis creditAnalysis);
}

