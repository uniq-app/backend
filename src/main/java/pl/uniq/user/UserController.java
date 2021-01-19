package pl.uniq.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.uniq.auth.code.CodeDto;
import pl.uniq.auth.dto.AuthenticationResponse;
import pl.uniq.auth.security.authorizartion.AuthorizationService;
import pl.uniq.user.dto.ChangePasswordDto;
import pl.uniq.user.dto.EmailDto;
import pl.uniq.user.dto.ResetPasswordDto;
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

	@PutMapping(value = "/update")
	public ResponseEntity<AuthenticationResponse> updateUsername(@RequestHeader("Authorization") String authHeader, @RequestBody User user) {
		try {
			AuthenticationResponse response = userService.updateUsername(authHeader, authorizationService.getCurrentUser(), user);
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

	@PutMapping(value = "/activation")
	public ResponseEntity<Message> activateAccount(@RequestBody CodeDto codeDto) {
		try {
			Message message = userService.activateAccount(codeDto);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (UserOperationException | CodeException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PostMapping(value = "/resend")
	public ResponseEntity<Message> resendCode(@RequestBody EmailDto emailDto) {
		try {
			Message message = userService.resendActivationCode(emailDto);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (UserOperationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PostMapping(value = "/forgot")
	public ResponseEntity<Message> forgotPassword(@RequestBody EmailDto email) {
		try {
			return new ResponseEntity<>(userService.sendCodeToResetPassword(email), HttpStatus.OK);
		} catch (UserOperationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PostMapping(value = "/valid")
	public ResponseEntity<Message> validCode(@RequestBody CodeDto codeDto) {
		try {
			return new ResponseEntity<>(userService.validCode(codeDto), HttpStatus.OK);
		} catch (UserOperationException | CodeException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PutMapping(value = "/reset")
	public ResponseEntity<Message> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
		return new ResponseEntity<>(userService.resetPassword(resetPasswordDto), HttpStatus.OK);
	}

	@PostMapping(value = "/code_email")
	ResponseEntity<Message> getCode() {
		return new ResponseEntity<>(userService.sendCodeToChangeEmail(authorizationService.getCurrentUser()), HttpStatus.OK);
	}

	@PutMapping(value = "/update_email")
	public ResponseEntity<Message> updateEmail(@RequestBody EmailDto emailDto) {
		return new ResponseEntity<>(userService.updateEmail(authorizationService.getCurrentUser(), emailDto), HttpStatus.OK);
	}

	@DeleteMapping(value = "/delete")
	public ResponseEntity<Message> deleteUser() {
		return new ResponseEntity<>(userService.deleteUser(authorizationService.getCurrentUser()), HttpStatus.OK);
	}
}
