package com.mtuanvu.identityservice.service;

import com.mtuanvu.identityservice.dto.request.AuthenticationRequest;
import com.mtuanvu.identityservice.entities.User;
import com.mtuanvu.identityservice.exception.AppException;
import com.mtuanvu.identityservice.exception.ErrorCode;
import com.mtuanvu.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    public boolean authenticate(AuthenticationRequest request){
        //Kiểm tra xem username có tồn tại không
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.USER_EXISTED));

        //Kiểm tra mk user có khớp với trong db không
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }
}
