package com.app.creditanalysis.exceptionhandler;

import com.app.creditanalysis.exception.ClientNotFoundException;
import com.app.creditanalysis.exception.CreditAnalysisNotFound;
import com.app.creditanalysis.exception.InvalidValueException;
import com.app.creditanalysis.exception.MaximumMonthlyIncomeExceededException;
import com.app.creditanalysis.exception.RequestedAmountExceedsMonthlyIncome;
import java.net.URI;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CreditAnalysisExceptionHandler {
    public static final String TIMESTAMP = "timestamp";

    @ExceptionHandler(ClientNotFoundException.class)
    public ProblemDetail clientNotFoundException(ClientNotFoundException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/400"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(MaximumMonthlyIncomeExceededException.class)
    public ProblemDetail maximumMonthlyIncomeExceededException(MaximumMonthlyIncomeExceededException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/422"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(RequestedAmountExceedsMonthlyIncome.class)
    public ProblemDetail requestedAmountExceedsMonthlyIncome(RequestedAmountExceedsMonthlyIncome exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/424"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(CreditAnalysisNotFound.class)
    public ProblemDetail creditAnalysisExceptionHandler(CreditAnalysisNotFound exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/424"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(InvalidValueException.class)
    public ProblemDetail invalidValueException(InvalidValueException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/424"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }
}
