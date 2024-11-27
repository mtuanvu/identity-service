package com.mtuanvu.identityservice.service;

import com.mtuanvu.identityservice.dto.request.UserCreateRequest;
import com.mtuanvu.identityservice.dto.request.UserUpdateRequest;
import com.mtuanvu.identityservice.dto.response.UserResponse;
import com.mtuanvu.identityservice.entities.User;
import com.mtuanvu.identityservice.enums.Role;
import com.mtuanvu.identityservice.exception.AppException;
import com.mtuanvu.identityservice.exception.ErrorCode;
import com.mtuanvu.identityservice.mapper.UserMapper;
import com.mtuanvu.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public User craeteUser(UserCreateRequest request){

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        user.setRoles(roles);

        return userRepository.save(user);
    }

    public UserResponse getMyInfo(){
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers(){
        List<User> users= userRepository.findAll();
        return userMapper.toUsersResponse(users);
    }

    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found with id: " + id));

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request){
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found with id: " + id));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }


    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

}
