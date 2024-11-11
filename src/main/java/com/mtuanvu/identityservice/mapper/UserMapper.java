package com.mtuanvu.identityservice.mapper;


import com.mtuanvu.identityservice.dto.request.UserCreateRequest;
import com.mtuanvu.identityservice.dto.request.UserUpdateRequest;
import com.mtuanvu.identityservice.dto.response.UserResponse;
import com.mtuanvu.identityservice.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") //Sử dụng Mapstruct để ánh xạ giữa DTO và Entity
// componentModel = "spring" cho phép quản lý mapper như một String Bean
public interface UserMapper {
    User toUser(UserCreateRequest request);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    UserResponse toUserResponse(User user);
}
