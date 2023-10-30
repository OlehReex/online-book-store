package onlinebookstore.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.user.UserLoginRequestDto;
import onlinebookstore.dto.user.UserLoginResponseDto;
import onlinebookstore.dto.user.UserRegistrationRequestDto;
import onlinebookstore.dto.user.UserResponseDto;
import onlinebookstore.exception.RegistrationException;
import onlinebookstore.mapper.UserMapper;
import onlinebookstore.model.RoleName;
import onlinebookstore.model.User;
import onlinebookstore.repository.RoleRepository;
import onlinebookstore.repository.UserRepository;
import onlinebookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registrationRequest)
            throws RegistrationException {
        if (userRepository.findByEmail(registrationRequest.email()).isPresent()) {
            throw new RegistrationException("Unable to complete registration.");
        }
        User userToSave = new User();
        userToSave.setEmail(registrationRequest.email());
        userToSave.setPassword(passwordEncoder.encode(registrationRequest.password()));
        userToSave.setFirstName(registrationRequest.firstName());
        userToSave.setLastName(registrationRequest.lastName());
        userToSave.setShippingAddress(registrationRequest.shippingAddress());
        userToSave.setRoles(Set.of(roleRepository.findRoleByName(RoleName.ROLE_USER)));
        userRepository.save(userToSave);
        return userMapper.toUserResponseDto(userToSave);
    }

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto loginRequest) {
        return null;
    }
}
