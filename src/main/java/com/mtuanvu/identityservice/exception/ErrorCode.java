package com.mtuanvu.identityservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {
    USER_EXISTED(1001, "Username is already taken", BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", INTERNAL_SERVER_ERROR),
    INVALID_KEY(1111, "Invalid message key", BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 5 character!", BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters and no more than 20 characters", BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", FORBIDDEN),

    ;
    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
