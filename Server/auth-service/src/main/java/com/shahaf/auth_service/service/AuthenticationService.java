package com.shahaf.auth_service.service;


import com.shahaf.auth_service.dto.AuthenticationDto;
import com.shahaf.auth_service.dto.AuthenticationResponse;
import com.shahaf.auth_service.dto.RegisterDto;
import com.shahaf.auth_service.entity.User;
import com.shahaf.auth_service.enums.Role;
import com.shahaf.auth_service.exceptions.AuthenticationException;
import com.shahaf.auth_service.exceptions.ResourceAlreadyExistsException;
import com.shahaf.auth_service.exceptions.ResourceNotFound;
import com.shahaf.auth_service.reposetory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse register(RegisterDto registerDto) {
        usernameOrEmailExistsValidations(registerDto.getUsername(), registerDto.getEmail());
        String username = registerDto.getUsername();
        String email = registerDto.getEmail();

        var user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(Role.USER)
                .build();

        logger.info("Saving user. username: {}, email: {}", username, email);
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(username)
                .build();
    }

    private void usernameOrEmailExistsValidations(String username, String email) {
        Map<String, String> errorMap = new HashMap<>();
        if (userRepository.findByUsername(username).isPresent()) {
            String errorMessage = String.format("Username '%s' is already in use.", username);
            logger.error(errorMessage);
            errorMap.put("username", errorMessage);
        }
        if (userRepository.findByEmail(email).isPresent()) {
            String errorMessage = String.format("Email '%s' is already in use.", email);
            logger.error(errorMessage);
            errorMap.put("email", errorMessage);
        }
        if (!errorMap.isEmpty()) {
            throw new ResourceAlreadyExistsException(errorMap);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationDto authenticationDto) {
        String email = authenticationDto.getEmail();
        validateAuthentication(email, authenticationDto.getPassword());
        User user = getUserByEmail(email);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getActualUsername())
                .build();
    }

    private void validateAuthentication(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            String errorMessage = "Username does not exists or password is incorrect";
            logger.error(errorMessage);
            throw new AuthenticationException(errorMessage);
        }
    }

    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            String errorMessage = "User not found by email " + email;
            logger.error(errorMessage);
            throw new ResourceNotFound(errorMessage);
        }
        return user.get();
    }

}
