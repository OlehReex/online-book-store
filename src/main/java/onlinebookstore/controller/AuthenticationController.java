package onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.user.UserLoginRequestDto;
import onlinebookstore.dto.user.UserLoginResponseDto;
import onlinebookstore.dto.user.UserRegistrationRequestDto;
import onlinebookstore.dto.user.UserResponseDto;
import onlinebookstore.exception.RegistrationException;
import onlinebookstore.security.AuthenticationService;
import onlinebookstore.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User authorization/registration",
        description = "Endpoints for user authorization and registration")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "User authentication",
            description = "Getting user validation and return JWT token")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto loginRequestDto) {
        return authenticationService.authenticate(loginRequestDto);
    }

    @PostMapping("/register")
    @Operation(summary = "User registration",
            description = "Validate data for registration and save new user in DB")
    public UserResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto registrationRequest)
            throws RegistrationException {
        return userService.register(registrationRequest);
    }
}
