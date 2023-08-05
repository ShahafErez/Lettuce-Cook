package com.shahaf.lettucecook.controller;

import com.shahaf.lettucecook.dto.AuthenticationDto;
import com.shahaf.lettucecook.dto.RegisterDto;
import com.shahaf.lettucecook.dto.response.AuthenticationResponse;
import com.shahaf.lettucecook.dto.response.ErrorResponse;
import com.shahaf.lettucecook.exceptions.UserDetailsAlreadyExistsException;
import com.shahaf.lettucecook.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(authenticationService.register(registerDto));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationDto authenticationDto) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationDto));

    }

    @ExceptionHandler(value = UserDetailsAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCustomerAlreadyExistsException(UserDetailsAlreadyExistsException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
    }
}
