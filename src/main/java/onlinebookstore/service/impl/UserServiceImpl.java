package onlinebookstore.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registrationRequest)
            throws RegistrationException {
        if (userRepository.existsByEmail(registrationRequest.email())) {
            throw new RegistrationException("Unable to complete registration.");
        }
        User userToSave = userMapper.toUser(registrationRequest);
        userToSave.setRoles(Set.of(roleRepository.findRoleByName(RoleName.ROLE_USER)));
        userToSave.setPassword(passwordEncoder.encode(registrationRequest.password()));
        userRepository.save(userToSave);
        return userMapper.toUserResponseDto(userToSave);
    }
}
