package com.mtuanvu.identityservice.service;

import com.mtuanvu.identityservice.dto.request.RoleRequest;
import com.mtuanvu.identityservice.dto.response.ApiResponse;
import com.mtuanvu.identityservice.dto.response.RoleResponse;
import com.mtuanvu.identityservice.entities.Permission;
import com.mtuanvu.identityservice.entities.Role;
import com.mtuanvu.identityservice.mapper.RoleMapper;
import com.mtuanvu.identityservice.repository.PermissionRepository;
import com.mtuanvu.identityservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.toRole(request);

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        Role saveRole = roleRepository.save(role);
        return roleMapper.toRoleResponse(saveRole);

    }

    public List<RoleResponse> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }
}
