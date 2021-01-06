package pl.uniq.exceptions;

public class UserOperationException extends RuntimeException {
	public UserOperationException() {
	}

	public UserOperationException(String message) {
		super(message);
	}

	public UserOperationException(String message, Throwable cause) {
		super(message, cause);
	}
}
