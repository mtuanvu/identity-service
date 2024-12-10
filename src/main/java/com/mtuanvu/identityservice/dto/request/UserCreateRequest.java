package com.mtuanvu.identityservice.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateRequest {
    @Size(min = 5, message = "USERNAME_INVALID")
    private String username;

    @Size(min = 8, max = 20, message = "PASSWORD_INVALID")
    private String password;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
}
