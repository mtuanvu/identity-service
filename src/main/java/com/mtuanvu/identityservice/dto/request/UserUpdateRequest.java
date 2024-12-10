package com.mtuanvu.identityservice.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @Size(min = 8, max = 20, message = "PASSWORD_INVALID")
    private String password;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private List<String> roles;
}
