package com.shahaf.lettucecook.controller;

import com.shahaf.lettucecook.dto.AuthenticationDto;
import com.shahaf.lettucecook.dto.RegisterDto;
import com.shahaf.lettucecook.dto.response.AuthenticationResponseDto;
import com.shahaf.lettucecook.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(authenticationService.register(registerDto));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationDto authenticationDto) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationDto));

    }

}
