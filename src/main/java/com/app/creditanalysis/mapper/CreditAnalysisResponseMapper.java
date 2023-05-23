package com.app.creditanalysis.mapper;

import com.app.creditanalysis.controller.response.CreditAnalysisResponse;
import com.app.creditanalysis.repository.entity.CreditAnalysisEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreditAnalysisResponseMapper {
    @Mapping(source = "withdrawlLimitValue", target = "withdrawalLimitValue")
    CreditAnalysisResponse from(CreditAnalysisEntity creditAnalysis);
}
