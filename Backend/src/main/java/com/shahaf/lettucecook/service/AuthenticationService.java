package com.shahaf.lettucecook.service;

import com.shahaf.lettucecook.dto.AuthenticationDto;
import com.shahaf.lettucecook.dto.RegisterDto;
import com.shahaf.lettucecook.dto.response.AuthenticationResponse;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.enums.users.Role;
import com.shahaf.lettucecook.exceptions.AuthenticationException;
import com.shahaf.lettucecook.exceptions.ResourceAlreadyExistsException;
import com.shahaf.lettucecook.reposetory.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.shahaf.lettucecook.constants.MessagesConstants.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterDto registerDto) {
        usernameOrEmailExistsValidations(registerDto.getUsername(), registerDto.getEmail());
        var user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void usernameOrEmailExistsValidations(String username, String email) {
        Map<String, String> errorMap = new HashMap<>();
        if (userRepository.findByUsername(username).isPresent()) {
            errorMap.put("username", String.format("Username '%s' is already in use.", username));
        }
        if (userRepository.findByEmail(email).isPresent()) {
            errorMap.put("email", String.format("Email '%s' is already in use.", email));
        }
        if (!errorMap.isEmpty()) {
            throw new ResourceAlreadyExistsException(errorMap);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationDto authenticationDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationDto.getEmail(), authenticationDto.getPassword()
            ));
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Username does not exists or password is incorrect");
        }
        Optional<User> optionalUser = userRepository.findByEmail(authenticationDto.getEmail());
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND_MESSAGE));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
