package com.app.creditanalysis.exception;

public class MaximumMonthlyIncomeExceededException extends RuntimeException {
    public MaximumMonthlyIncomeExceededException(String message) {
        super(message);
    }
}
