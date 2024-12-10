package com.mtuanvu.identityservice.mapper;

import com.mtuanvu.identityservice.dto.request.PermissionRequest;
import com.mtuanvu.identityservice.dto.response.PermissionResponse;
import com.mtuanvu.identityservice.entities.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
