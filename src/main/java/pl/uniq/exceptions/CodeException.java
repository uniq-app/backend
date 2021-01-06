package pl.uniq.exceptions;

public class CodeException extends RuntimeException {

	public CodeException() {
	}

	public CodeException(String message) {
		super(message);
	}

	public CodeException(String message, Throwable cause) {
		super(message, cause);
	}
}
