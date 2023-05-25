package com.app.creditanalysis.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.hibernate.annotations.Immutable;

@Table(name = "CREDIT_ANALYSIS")
@Entity
@Immutable
public class CreditAnalysisEntity {
    @Id
    UUID id;
    Boolean approved;
    BigDecimal approvedLimit;
    BigDecimal requestedAmount;
    @Column(name = "withdrawal_limit_value")
    BigDecimal withdrawalLimitValue;
    Double annualInterest;
    UUID clientId;
    LocalDateTime date;
    BigDecimal monthlyIncome;

    public CreditAnalysisEntity() {
    }

    @Builder(toBuilder = true)
    public CreditAnalysisEntity(BigDecimal monthlyIncome, Boolean approved, BigDecimal approvedLimit, BigDecimal requestedAmount,
                                BigDecimal withdrawalLimitValue, Double annualInterest, UUID clientId) {
        this.id = UUID.randomUUID();
        this.approved = approved;
        this.approvedLimit = approvedLimit;
        this.requestedAmount = requestedAmount;
        this.withdrawalLimitValue = withdrawalLimitValue;
        this.annualInterest = annualInterest;
        this.clientId = clientId;
        this.monthlyIncome = monthlyIncome;
        this.date = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Boolean getApproved() {
        return approved;
    }

    public BigDecimal getApprovedLimit() {
        return approvedLimit;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public BigDecimal getWithdrawlLimitValue() {
        return withdrawalLimitValue;
    }

    public Double getAnnualInterest() {
        return annualInterest;
    }

    public UUID getClientId() {
        return clientId;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
