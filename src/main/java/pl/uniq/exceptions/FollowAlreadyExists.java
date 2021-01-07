package pl.uniq.exceptions;

public class FollowAlreadyExists extends RuntimeException {
	public FollowAlreadyExists() {}

	public FollowAlreadyExists(String message) {
		super(message);
	}

	public FollowAlreadyExists(String message, Throwable cause) {
		super(message, cause);
	}
}
