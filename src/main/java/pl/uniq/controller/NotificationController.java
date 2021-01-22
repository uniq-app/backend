package pl.uniq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.uniq.service.AuthorizationService;
import pl.uniq.dto.NotificationDto;
import pl.uniq.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

	private final NotificationService notificationService;
	private final AuthorizationService authorizationService;

	@Autowired
	public NotificationController(NotificationService notificationService, AuthorizationService authorizationService) {
		this.notificationService = notificationService;
		this.authorizationService = authorizationService;
	}

	@PutMapping(value = "/token")
	public ResponseEntity<NotificationDto> updateFCMToken(@RequestBody NotificationDto notificationDto) {
		return new ResponseEntity<NotificationDto>(notificationService.updateToken(notificationDto, authorizationService.getCurrentUser()), HttpStatus.OK);
	}
}
