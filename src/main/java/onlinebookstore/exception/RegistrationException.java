package onlinebookstore.exception;

public class RegistrationException extends RuntimeException {

    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException(String message, Throwable ex) {
        super(message, ex);
    }
}


