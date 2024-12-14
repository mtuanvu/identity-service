package com.mtuanvu.identityservice.service;

import java.util.HashSet;
import java.util.List;

import com.mtuanvu.identityservice.dto.request.UserCreateRequest;
import com.mtuanvu.identityservice.dto.request.UserUpdateRequest;
import com.mtuanvu.identityservice.dto.response.UserResponse;
import com.mtuanvu.identityservice.entities.Role;
import com.mtuanvu.identityservice.entities.User;
import com.mtuanvu.identityservice.exception.AppException;
import com.mtuanvu.identityservice.exception.ErrorCode;
import com.mtuanvu.identityservice.mapper.UserMapper;
import com.mtuanvu.identityservice.repository.RoleRepository;
import com.mtuanvu.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    public UserResponse createUser(UserCreateRequest request) {
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<String> roles = new HashSet<>();
        //        roles.add(Role.USER.name());

        //        user.setRoles(roles);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    //    @PreAuthorize("hasRole('ADMIN')")  khi sử dụng role còn sử dụng permissions thì làm theo cách dưới
    @PreAuthorize("hasAnyAuthority('CREATE_DATA')")
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toUsersResponse(users);
    }

    public UserResponse getUserById(Long id) {
        User user =
                userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user =
                userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
