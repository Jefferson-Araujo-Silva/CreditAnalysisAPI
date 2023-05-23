package com.app.creditanalysis.repository;


import com.app.creditanalysis.repository.entity.CreditAnalysisEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditAnalysisRepository extends JpaRepository<CreditAnalysisEntity, UUID> {
    List<CreditAnalysisEntity> findAllByClientId(UUID clientId);
}
