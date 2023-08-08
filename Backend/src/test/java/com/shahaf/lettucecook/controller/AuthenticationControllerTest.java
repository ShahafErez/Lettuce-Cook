package com.shahaf.lettucecook.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shahaf.lettucecook.dto.AuthenticationDto;
import com.shahaf.lettucecook.dto.RegisterDto;
import com.shahaf.lettucecook.dto.response.AuthenticationResponse;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.enums.Role;
import com.shahaf.lettucecook.reposetory.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void register() throws Exception {
        RegisterDto mockRegisterDto = new RegisterDto("user", "user@gmail.com", "abc");

        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockRegisterDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        AuthenticationResponse authenticationResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        assertNotNull(authenticationResponse.getToken());

        verify(userRepository, times(1)).findByUsername("user");
        verify(userRepository, times(1)).findByEmail("user@gmail.com");
    }

    @Test
    void registerUsernameAndEmailAlreadyExists() throws Exception {
        RegisterDto mockRegisterDto = new RegisterDto("user", "user@gmail.com", "abc");
        User mockUser = new User(1L, "user", "user@gmail.com", "abc", Role.USER);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(mockUser));
        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(mockUser));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockRegisterDto)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Map<String, String> errorsResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        assertEquals("Email 'user@gmail.com' is already in use.", errorsResponse.get("email"));
        assertEquals("Username 'user' is already in use.", errorsResponse.get("username"));

        verify(userRepository, times(1)).findByUsername("user");
        verify(userRepository, times(1)).findByEmail("user@gmail.com");
    }


    @Test
    void authenticate() throws Exception {
        AuthenticationDto mockAuthenticationDto = new AuthenticationDto("user@gmail.com", "abc");
        User mockUser = new User(1L, "user", "user@gmail.com", "abc", Role.USER);
        UsernamePasswordAuthenticationToken MockAuthentication = new UsernamePasswordAuthenticationToken(mockUser, null);

        when(authenticationManager.authenticate(any())).thenReturn(MockAuthentication);
        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(mockUser));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockAuthenticationDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        AuthenticationResponse authenticationResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        assertNotNull(authenticationResponse.getToken());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByEmail("user@gmail.com");
    }

    @Test
    void authenticateFailedAuthenticationManager() throws Exception {
        AuthenticationDto mockAuthenticationDto = new AuthenticationDto("user@gmail.com", "abc");
        User mockUser = new User(1L, "user", "user@gmail.com", "abc", Role.USER);

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockAuthenticationDto)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Username does not exists or password is incorrect")));

        verify(authenticationManager, times(1)).authenticate(any());

    }

}
