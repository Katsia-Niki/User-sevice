package by.nikiforova.crud.exception;

public class UserPersistenceException extends RuntimeException {
    public UserPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserPersistenceException(String message) {
        super(message);
    }
}
