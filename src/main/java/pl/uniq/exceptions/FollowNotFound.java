package pl.uniq.exceptions;


public class FollowNotFound extends RuntimeException {
	public FollowNotFound() {}

	public FollowNotFound(String message) {
		super(message);
	}

	public FollowNotFound(String message, Throwable cause) {
		super(message, cause);
	}
}
