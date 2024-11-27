package com.mtuanvu.identityservice.controllers;

import com.mtuanvu.identityservice.dto.request.RoleRequest;
import com.mtuanvu.identityservice.dto.response.ApiResponse;
import com.mtuanvu.identityservice.dto.response.RoleResponse;
import com.mtuanvu.identityservice.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/get/all")
    public ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .code(200)
                .message("Get all roles")
                .result(roleService.getAll())
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<RoleResponse> createRole(@RequestBody @Valid RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .code(201)
                .message("Create role")
                .result(roleService.createRole(request))
                .build();
    }

    @DeleteMapping("/delete/{role}")
    public ApiResponse<Void> deleteRole(@PathVariable("role") String role){
        roleService.deleteRole(role);
        return ApiResponse.<Void>builder().build();
    }
}
