package com.mtuanvu.identityservice.controllers;

import com.mtuanvu.identityservice.dto.request.AuthenticationRequest;
import com.mtuanvu.identityservice.dto.request.IntrospectRequest;
import com.mtuanvu.identityservice.dto.request.LogoutRequest;
import com.mtuanvu.identityservice.dto.request.RefreshRequest;
import com.mtuanvu.identityservice.dto.response.ApiResponse;
import com.mtuanvu.identityservice.dto.response.AuthenticationResponse;
import com.mtuanvu.identityservice.dto.response.IntrospectResponse;
import com.mtuanvu.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        //gọi authenticationService để kiểm tra thông tin đăng nhập
        AuthenticationResponse result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> login(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        //gọi authenticationService để kiểm tra thông tin đăng nhập
        AuthenticationResponse result = authenticationService.refreshToken(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder().build();
    }

    //verify-token
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> login(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
