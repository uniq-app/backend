package pl.uniq.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.uniq.auth.security.authorizartion.AuthorizationService;
import pl.uniq.auth.user.dto.ChangePasswordDto;
import pl.uniq.exceptions.UserOperationException;

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
	public ResponseEntity<String> updateUsername(@RequestHeader("Authorization") String authHeader, @PathVariable String username) {
		try {
			String jwt = userService.updateUsername(authHeader, authorizationService.getCurrentUser(), username);
			return new ResponseEntity<>(jwt, HttpStatus.OK);
		} catch (UserOperationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PutMapping(value = "/password")
	public ResponseEntity<String> updatePassword(@RequestBody ChangePasswordDto changePasswordDto) {
		try {
			return new ResponseEntity<>(userService.updatePassword(authorizationService.getCurrentUser(), changePasswordDto), HttpStatus.OK);
		} catch (UserOperationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
}
