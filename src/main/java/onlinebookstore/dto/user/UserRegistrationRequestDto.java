package onlinebookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import onlinebookstore.validation.field.match.FieldMatch;

@FieldMatch
public record UserRegistrationRequestDto(
        @Email(regexp = "^[\\w-]+@[a-zA-Z\\d-]+\\.[a-zA-Z]{2,}$")
        String email,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String shippingAddress,
        @NotBlank @Size(min = 5, max = 20) String password,
        @NotBlank @Size(min = 5, max = 20) String repeatPassword) {
}
