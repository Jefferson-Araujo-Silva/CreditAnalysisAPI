package com.app.creditanalysis.exception;

public class RequestedAmountExceedsMonthlyIncome extends RuntimeException {
    public RequestedAmountExceedsMonthlyIncome(String message) {
        super(message);
    }
}
