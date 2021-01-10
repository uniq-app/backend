package pl.uniq.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.uniq.auth.dto.AuthenticationResponse;
import pl.uniq.auth.security.authorizartion.AuthorizationService;
import pl.uniq.auth.user.dto.ChangePasswordDto;
import pl.uniq.auth.user.dto.ResetPasswordDto;
import pl.uniq.exceptions.CodeException;
import pl.uniq.exceptions.UserOperationException;
import pl.uniq.utils.Message;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	private final UserService userService;
	private final AuthorizationService authorizationService;

	@Autowired
	public UserController(UserService userService, AuthorizationService authorizationService) {
		this.userService = userService;
		this.authorizationService = authorizationService;
	}

	@PutMapping(value = "/update/{username}")
	public ResponseEntity<AuthenticationResponse> updateUsername(@RequestHeader("Authorization") String authHeader, @PathVariable String username) {
		try {
			AuthenticationResponse response = userService.updateUsername(authHeader, authorizationService.getCurrentUser(), username);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (UserOperationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PutMapping(value = "/password")
	public ResponseEntity<Message> updatePassword(@RequestBody ChangePasswordDto changePasswordDto) {
		try {
			return new ResponseEntity<>(userService.updatePassword(authorizationService.getCurrentUser(), changePasswordDto), HttpStatus.OK);
		} catch (UserOperationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PutMapping(value = "/activation/{codeValue}")
	public ResponseEntity<Message> activateAccount(@PathVariable int codeValue) {
		try {
			Message message = userService.activateAccount(codeValue);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (UserOperationException | CodeException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PostMapping(value = "/resend/{email}")
	public ResponseEntity<Message> resendCode(@PathVariable String email) {
		try {
			Message message = userService.resendActivationCode(email);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (UserOperationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PostMapping(value = "/forgot/{email}")
	public ResponseEntity<Message> forgotPassword(@PathVariable String email) {
		try {
			return new ResponseEntity<>(userService.sendCodeToResetPassword(email), HttpStatus.OK);
		} catch (UserOperationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PostMapping(value = "/valid/{codeValue}")
	public ResponseEntity<Message> validCode(@PathVariable int codeValue) {
		try {
			return new ResponseEntity<>(userService.validCode(codeValue), HttpStatus.OK);
		} catch (UserOperationException | CodeException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PutMapping(value = "/reset")
	public ResponseEntity<Message> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
		return new ResponseEntity<>(userService.resetPassword(resetPasswordDto), HttpStatus.OK);
	}

	@PutMapping(value = "/update_email/{email}")
	public ResponseEntity<Message> updateEmail(@PathVariable String email) {
		return new ResponseEntity<>(userService.updateEmail(authorizationService.getCurrentUser(), email), HttpStatus.OK);
	}
}
