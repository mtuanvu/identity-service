package com.mtuanvu.identityservice.controllers;

import com.mtuanvu.identityservice.dto.request.PermissionRequest;
import com.mtuanvu.identityservice.dto.response.ApiResponse;
import com.mtuanvu.identityservice.dto.response.PermissionResponse;
import com.mtuanvu.identityservice.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping("/create")
    public ApiResponse<PermissionResponse> createPermission(@RequestBody @Valid PermissionRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .code(200)
                .message("Permission created")
                .result(permissionService.createPermission(request))
                .build();
    }

    @GetMapping("/get/all")
    public ApiResponse<List<PermissionResponse>> getAllPermission(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .code(201)
                .message("Permissions found")
                .result(permissionService.getAllPermissions())
                .build();
    }

    @DeleteMapping("/delete/{permission}")
    public ApiResponse<Void> deletePermission(@PathVariable("permission") String permission){
        permissionService.deletePermission(permission);
        return ApiResponse.<Void>builder().build();

    }

}
