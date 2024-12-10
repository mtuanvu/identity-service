package com.mtuanvu.identityservice.controllers;

import java.util.List;

import com.mtuanvu.identityservice.dto.request.UserCreateRequest;
import com.mtuanvu.identityservice.dto.request.UserUpdateRequest;
import com.mtuanvu.identityservice.dto.response.ApiResponse;
import com.mtuanvu.identityservice.dto.response.UserResponse;
import com.mtuanvu.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        // @Valid khai báo cho Spring biết cần phải validate cho cái obj UserCreateRequest
        // dựa theo các rule đã được define trong obj
        log.info("Create: User Controller");

        ApiResponse<UserResponse> userApiResponse = new ApiResponse<>();

        userApiResponse.setCode(201);
        userApiResponse.setResult(userService.createUser(request));

        return userApiResponse;
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo() {
        ApiResponse<UserResponse> userApiResponse = new ApiResponse<>();
        userApiResponse.setCode(200);
        userApiResponse.setResult(userService.getMyInfo());
        return userApiResponse;
    }

    @GetMapping("/get/all")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/get/{id}")
    public UserResponse getUserById(@PathVariable("id") Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/update/{id}")
    public UserResponse updateUser(@PathVariable("id") Long userid, @RequestBody @Valid UserUpdateRequest request) {
        return userService.updateUser(userid, request);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return "User deleted successfully!";
    }
}
