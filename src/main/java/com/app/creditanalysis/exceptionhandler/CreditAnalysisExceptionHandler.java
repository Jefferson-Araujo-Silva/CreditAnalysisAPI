package com.app.creditanalysis.exceptionhandler;

import com.app.creditanalysis.exception.ClientNotFoundException;
import com.app.creditanalysis.exception.CreditAnalysisNotFound;
import com.app.creditanalysis.exception.InvalidValueException;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CreditAnalysisExceptionHandler {
    public static final String TIMESTAMP = "timestamp";
    private static final Logger logger = LoggerFactory.getLogger(CreditAnalysisExceptionHandler.class);

    @ExceptionHandler(ClientNotFoundException.class)
    public ProblemDetail clientNotFoundException(ClientNotFoundException exception) {
        logger.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/400"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(CreditAnalysisNotFound.class)
    public ProblemDetail creditAnalysisExceptionHandler(CreditAnalysisNotFound exception) {
        logger.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/424"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail constraintViolationException(ConstraintViolationException exception) {
        logger.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/424"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }
    @ExceptionHandler({HttpMessageNotReadableException.class, InvalidValueException.class})
    public ProblemDetail httpMessageNotReadableException(HttpMessageNotReadableException exception) {
        logger.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        if (exception.getMessage().contains("UUID")){
            problemDetail.setDetail(
                    "id value in json is invalid, id must be a 36 character string with numbers, letters and dashes");
        }
        else if(exception.getMessage().contains("BigDecimal")){
            problemDetail.setDetail("monthly income and requested amount value cannot contain a string character");
        }
        else {
            problemDetail.setDetail("Values in credit analysis JSON must not be null");
        }
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/424"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(InvalidValueException.class)
    public ProblemDetail invalidValueException(InvalidValueException exception) {
        logger.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/424"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }
}
