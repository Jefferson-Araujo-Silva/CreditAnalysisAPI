package com.app.creditanalysis.repository;


import com.app.creditanalysis.repository.entity.CreditAnalysisEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditAnalysisRepository extends JpaRepository<CreditAnalysisEntity, UUID> {
}
