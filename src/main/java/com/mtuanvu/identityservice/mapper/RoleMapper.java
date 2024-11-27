package com.mtuanvu.identityservice.mapper;

import com.mtuanvu.identityservice.dto.request.RoleRequest;
import com.mtuanvu.identityservice.dto.response.RoleResponse;
import com.mtuanvu.identityservice.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role permission);
}
