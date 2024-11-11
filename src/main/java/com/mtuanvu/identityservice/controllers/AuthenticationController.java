package com.mtuanvu.identityservice.controllers;

import com.mtuanvu.identityservice.dto.request.AuthenticationRequest;
import com.mtuanvu.identityservice.dto.response.ApiResponse;
import com.mtuanvu.identityservice.dto.response.AuthenticationResponse;
import com.mtuanvu.identityservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        //gọi authenticationService để kiểm tra thông tin đăng nhập
        boolean result = authenticationService.authenticate(request);

        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setAuthenticated(result);

        ApiResponse<AuthenticationResponse> response = new ApiResponse<>();
        response.setResult(authResponse);

        return response;
    }
}
