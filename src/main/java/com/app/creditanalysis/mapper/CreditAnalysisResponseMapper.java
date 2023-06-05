package com.app.creditanalysis.mapper;

import com.app.creditanalysis.controller.response.CreditAnalysisResponse;
import com.app.creditanalysis.repository.entity.CreditAnalysisEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CreditAnalysisResponseMapper {
    @Mapping(source = "withdrawlLimitValue", target = "withdrawalLimitValue")
    CreditAnalysisResponse from(CreditAnalysisEntity creditAnalysis);
}
