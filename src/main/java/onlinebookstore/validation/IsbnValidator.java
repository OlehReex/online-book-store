package onlinebookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {
    private static final Pattern ISBN_PATTERN = Pattern.compile("^\\d{2}-\\d{3}-\\d{3}-\\d{2}$");

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext constraintValidatorContext) {
        return isbn != null && ISBN_PATTERN.matcher(isbn).matches();
    }
}
