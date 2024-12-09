package com.mtuanvu.identityservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mtuanvu.identityservice.dto.request.UserCreateRequest;
import com.mtuanvu.identityservice.dto.response.UserResponse;
import com.mtuanvu.identityservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserCreateRequest request;
    private UserResponse response;
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
    }

    @Test
    //
    void createUser_validRequest_success() throws Exception {
        //GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(response);

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders
                .post("/users/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value("201"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id")
                        .value(1L));
        //THEN
    }

    @Test
        //
    void createUser_usernameInvalid_false() throws Exception {
        //GIVEN
        request.setUsername("mtu");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);


        //WHEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value("1003"))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Username must be at least 5 character!"));
        //THEN
    }
}
