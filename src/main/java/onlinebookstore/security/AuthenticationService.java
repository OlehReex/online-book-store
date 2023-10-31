package onlinebookstore.security;

import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.user.UserLoginRequestDto;
import onlinebookstore.dto.user.UserLoginResponseDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto loginRequest) {
        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(),
                        loginRequest.password()));
        String token = jwtUtil.generateToken(loginRequest.email());
        return new UserLoginResponseDto(token);
    }
}
