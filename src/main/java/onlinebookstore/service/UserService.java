package onlinebookstore.service;

import onlinebookstore.dto.user.UserRegistrationRequestDto;
import onlinebookstore.dto.user.UserResponseDto;
import onlinebookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto registrationRequest)
            throws RegistrationException;
}
