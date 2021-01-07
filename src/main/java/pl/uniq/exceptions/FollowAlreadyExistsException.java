package pl.uniq.exceptions;

public class FollowAlreadyExistsException extends RuntimeException {
	public FollowAlreadyExistsException() {}

	public FollowAlreadyExistsException(String message) {
		super(message);
	}

	public FollowAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
