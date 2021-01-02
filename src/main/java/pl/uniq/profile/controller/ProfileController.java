package pl.uniq.profile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.uniq.auth.security.authorizartion.AuthorizationService;
import pl.uniq.auth.user.User;
import pl.uniq.profile.service.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

	private final ProfileService profileService;
	private final AuthorizationService authorizationService;

	@Autowired
	public ProfileController(ProfileService profileService, AuthorizationService authorizationService) {
		this.profileService = profileService;
		this.authorizationService = authorizationService;
	}

	@GetMapping
	public ResponseEntity<User> getUserProfileDetails() {
		User currentUser = authorizationService.getCurrentUser();
		return new ResponseEntity<>(currentUser, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<User> updateUserProfileDetails(@RequestBody User requestBodyUser) {
		User currentUser = authorizationService.getCurrentUser();
		return new ResponseEntity<>(profileService.update(currentUser, requestBodyUser), HttpStatus.OK);
	}
}
