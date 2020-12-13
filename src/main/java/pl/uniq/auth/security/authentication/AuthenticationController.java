package pl.uniq.auth.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.uniq.auth.dto.JwtTokenDto;
import pl.uniq.auth.dto.LoginDto;
import pl.uniq.auth.dto.SignUpDto;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@Autowired
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping(value = "/register")
	ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto) {
		return new ResponseEntity<>(authenticationService.register(signUpDto), HttpStatus.OK);
	}

	@PostMapping(value = "/login")
	ResponseEntity<JwtTokenDto> signIn(@RequestBody LoginDto loginDto) {
		return new ResponseEntity<>(authenticationService.login(loginDto), HttpStatus.OK);
	}

	@PostMapping(value = "/logout")
	ResponseEntity<String> singOut(@RequestHeader("Authorization") String authHeader) {
		return new ResponseEntity<>(authenticationService.logout(authHeader), HttpStatus.OK);
	}
}
