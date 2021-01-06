package pl.uniq.profile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.uniq.auth.security.authorizartion.AuthorizationService;
import pl.uniq.auth.user.User;
import pl.uniq.auth.user.dto.UserDto;
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
	public ResponseEntity<UserDto> getUserProfileDetails() {
		User currentUser = authorizationService.getCurrentUser();
		return new ResponseEntity<>(new UserDto(currentUser), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<UserDto> updateUserProfileDetails(@RequestBody UserDto requestBodyUser) {
		User currentUser = authorizationService.getCurrentUser();
		return new ResponseEntity<>(new UserDto(profileService.update(currentUser, requestBodyUser)), HttpStatus.OK);
	}
}
