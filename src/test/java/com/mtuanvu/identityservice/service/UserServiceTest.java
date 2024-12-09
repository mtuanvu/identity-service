package com.mtuanvu.identityservice.service;

import com.mtuanvu.identityservice.dto.request.UserCreateRequest;
import com.mtuanvu.identityservice.dto.response.UserResponse;
import com.mtuanvu.identityservice.entities.User;
import com.mtuanvu.identityservice.exception.AppException;
import com.mtuanvu.identityservice.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {
    @Autowired UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreateRequest request;
    private UserResponse response;
    private User user;
    private LocalDate dob;

    @BeforeEach
    void initData(){
        dob = LocalDate.of(1990, 1, 1);
        request = UserCreateRequest.builder()
                .username("mtuanvu1234")
                .firstName("John")
                .lastName("Doe")
                .password("mtuanvu1234")
                .dateOfBirth(dob)
                .build();

        response = UserResponse.builder()
                .id(1L)
                .username("mtuanvu1234")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(dob)
                .build();

        user = User.builder()
                .id(1L)
                .username("mtuanvu1234")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        //GIVEN
        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        //WHEN
        UserResponse response1 = userService.createUser(request);

        //THEN
        Assertions.assertThat(response1.getId()).isEqualTo(1L);
        Assertions.assertThat(response1.getUsername()).isEqualTo("mtuanvu1234");
    }

    @Test
    void createUser_userExisted_false() {
        //GIVEN
        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(true);

        //WHEN
        AppException appException = assertThrows(AppException.class,
                () -> userService.createUser(request));

        //THEN
        assertThat(appException.getErrorCode().getCode())
                .isEqualTo(1001);
    }
}
