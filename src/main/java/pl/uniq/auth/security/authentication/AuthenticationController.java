package pl.uniq.auth.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.uniq.auth.dto.AuthenticationResponse;
import pl.uniq.auth.dto.AuthenticationRequest;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@Autowired
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping(value = "/register")
	ResponseEntity<String> signUp(@RequestBody AuthenticationRequest authenticationRequest) {
		return new ResponseEntity<>(authenticationService.register(authenticationRequest), HttpStatus.OK);
	}

	@PostMapping(value = "/login")
	ResponseEntity<AuthenticationResponse> signIn(@RequestBody AuthenticationRequest authenticationRequest) {
		try
		{
			return new ResponseEntity<>(authenticationService.login(authenticationRequest), HttpStatus.OK);
		}
		catch (BadCredentialsException e)
		{
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PostMapping(value = "/logout")
	ResponseEntity<String> singOut(@RequestHeader("Authorization") String authHeader) {
		return new ResponseEntity<>(authenticationService.logout(authHeader), HttpStatus.OK);
	}
}
