package onlinebookstore.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @Pattern(regexp = "^[\\w-]+@[a-zA-Z\\d-]+\\.[a-zA-Z]{2,}$",
                message = "Incorrect Email format")
        String email,
        @NotBlank @Size(min = 5, max = 255) String password) {
}
