package pl.uniq.exceptions;

public class AuthorizationHeaderException extends RuntimeException {
	public AuthorizationHeaderException() {
	}

	public AuthorizationHeaderException(String message) {
		super(message);
	}

	public AuthorizationHeaderException(String message, Throwable cause) {
		super(message, cause);
	}
}
