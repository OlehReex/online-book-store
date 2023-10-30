package onlinebookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import onlinebookstore.validation.field.match.FieldMatch;

@FieldMatch
public record UserRegistrationRequestDto(
        Long id,
        @NotBlank @Email String email,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String shippingAddress,
        @NotBlank @Size(min = 5, max = 50) String password,
        @NotBlank @Size(min = 5, max = 50) String repeatPassword) {
}
