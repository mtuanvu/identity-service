package com.mtuanvu.identityservice.controllers;

import com.mtuanvu.identityservice.dto.request.UserCreateRequest;
import com.mtuanvu.identityservice.dto.request.UserUpdateRequest;
import com.mtuanvu.identityservice.dto.response.ApiResponse;
import com.mtuanvu.identityservice.dto.response.UserResponse;
import com.mtuanvu.identityservice.entities.User;
import com.mtuanvu.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping("/create")
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreateRequest request){
        //@Valid khai báo cho Spring biết cần phải validate cho cái obj UserCreateRequest
        //dựa theo các rule đã được define trong obj

        ApiResponse<User> userApiResponse = new ApiResponse<>();

        userApiResponse.setCode(201);
        userApiResponse.setResult(userService.craeteUser(request));

        return userApiResponse;
    }

    @GetMapping("/get/all")
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/get/{id}")
    public UserResponse getUserById(@PathVariable("id") Long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/update/{id}")
    public UserResponse updateUser(@PathVariable("id") Long userid, @RequestBody @Valid UserUpdateRequest request){
        return userService.updateUser(userid, request);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return "User deleted successfully!";
    }
}
