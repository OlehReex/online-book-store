package onlinebookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @Email(regexp = "^[\\w-]+@[a-zA-Z\\d-]+\\.[a-zA-Z]{2,}$")
        String email,
        @NotBlank @Size(min = 5, max = 60) String password) {
}
