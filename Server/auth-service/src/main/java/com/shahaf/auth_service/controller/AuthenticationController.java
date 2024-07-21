package com.shahaf.auth_service.controller;


import com.shahaf.auth_service.dto.AuthenticationDto;
import com.shahaf.auth_service.dto.AuthenticationResponse;
import com.shahaf.auth_service.dto.RegisterDto;
import com.shahaf.auth_service.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.shahaf.auth_service.constants.ApplicationConstants.PATH_PREFIX;

@Tag(name = "Authentication", description = "All APIs related to authentication")
@RestController
@RequestMapping(PATH_PREFIX + "/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register",
            description = "Register a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered by the given user details."),
            @ApiResponse(responseCode = "409", description = "Username or email is already in use.")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterDto registerDto) {
        return new ResponseEntity<>(authenticationService.register(registerDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Authenticate",
            description = "Authenticate user by email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticate by the given user details."),
            @ApiResponse(responseCode = "401", description = "Username does not exists or password is incorrect."),
            @ApiResponse(responseCode = "404", description = "User by the given email was not found.")
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationDto authenticationDto) {
        return new ResponseEntity<>(authenticationService.authenticate(authenticationDto), HttpStatus.OK);
    }
}
