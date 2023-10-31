package onlinebookstore.validation.field.match;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import onlinebookstore.dto.user.UserRegistrationRequestDto;

public class FieldMatchValidator
        implements ConstraintValidator<FieldMatch, UserRegistrationRequestDto> {
    @Override
    public boolean isValid(UserRegistrationRequestDto registrationRequest,
                           ConstraintValidatorContext constraintValidatorContext) {
        return registrationRequest != null
                && registrationRequest.password().equals(registrationRequest.repeatPassword());
    }
}
