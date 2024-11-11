package com.mtuanvu.identityservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_EXISTED(1001, "Username is already taken"),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    INVALID_KEY(1111, "Invalid message key"),
    USERNAME_INVALID(1003, "Username must be at least 5 character!"),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters and no more than 20 characters"),
    USER_NOT_EXISTED(1005, "User not existed"),
    ;
    private int code;
    private String message;


}
