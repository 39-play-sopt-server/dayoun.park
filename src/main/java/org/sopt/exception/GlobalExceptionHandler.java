package org.sopt.exception;

import org.sopt.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class) // 공통 실패 응답
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            NoSuchElementException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure(
                        "POST_NOT_FOUND",
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class) // 제목, 본문 검증 실패 응답
    public ResponseEntity<ApiResponse<Void>> handleInvalidInput(
            IllegalArgumentException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(
                        "INVALID_INPUT",
                        exception.getMessage()
                ));
    }

    @Override // 스프링이 발생시키는 요청 오류 통일
    protected ResponseEntity<Object> createResponseEntity(
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        HttpStatus status = HttpStatus.resolve(statusCode.value());

        String message = status == null
                ? "요청을 처리할 수 없습니다."
                : status.getReasonPhrase();

        ApiResponse<Void> response = ApiResponse.failure(
                "HTTP_" + statusCode.value(),
                message
        );

        return new ResponseEntity<>(response, headers, statusCode);
    }

    @ExceptionHandler(Exception.class) // 서버 오류 통일
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(
            Exception exception
    ) {
        logger.error("처리하지 못한 예외가 발생했습니다.", exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(
                        "INTERNAL_SERVER_ERROR",
                        "서버 내부 오류가 발생했습니다."
                ));
    }
}