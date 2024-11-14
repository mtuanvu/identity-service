package com.mtuanvu.identityservice.service;

import com.mtuanvu.identityservice.dto.request.UserCreateRequest;
import com.mtuanvu.identityservice.dto.request.UserUpdateRequest;
import com.mtuanvu.identityservice.dto.response.UserResponse;
import com.mtuanvu.identityservice.entities.User;
import com.mtuanvu.identityservice.exception.AppException;
import com.mtuanvu.identityservice.exception.ErrorCode;
import com.mtuanvu.identityservice.mapper.UserMapper;
import com.mtuanvu.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;

    public User craeteUser(UserCreateRequest request){

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10); //strength ở đây nếu càng lớn thì mật khẩu cũng được mã hóa khó lên
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

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
