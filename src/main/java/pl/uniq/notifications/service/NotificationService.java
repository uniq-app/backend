package pl.uniq.notifications.service;

import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.uniq.auth.user.User;
import pl.uniq.auth.user.UserRepository;
import pl.uniq.notifications.dto.NotificationDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NotificationService {

	private final UserRepository userRepository;

	private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	@Autowired
	public NotificationService(UserRepository userRepository) {this.userRepository = userRepository;}

	@Async
	public void sendNotification(String token, String title, String body) {
		if (token == null) {
			return;
		}
		Message message = Message.builder().
				setNotification(Notification.builder().setTitle(title).setBody(body).build()).
				setToken(token).
				build();

		logger.info(String.format("New notification: %s", title));

		try {
			String response = FirebaseMessaging.getInstance().send(message);
			logger.info(String.format("Successfully sent notification: %s", response));
		} catch (FirebaseMessagingException e) {
			logger.error(e.getMessage());
		}
	}

	@Async
	public void sendBatchNotification(List<String> tokens, String title, String body) {
		if (tokens.size() == 0 ) {
			return;
		}

		MulticastMessage message = MulticastMessage.builder().
				setNotification(Notification.builder().setTitle(title).setBody(body).build()).
				addAllTokens(tokens).
				build();

		logger.info(String.format("New batch notification: %s", title));

		try {
			BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
			logger.info(String.format("Successfully sent batch message. Successful notifications: %d", response.getSuccessCount()));
		} catch (FirebaseMessagingException e) {
			logger.error(e.getMessage());
		}
	}

	@Transactional
	public NotificationDto updateToken(NotificationDto notificationDto, User currentUser) {
		if (notificationDto.getFCMToken() != null) {
			currentUser.setFCMToken(notificationDto.getFCMToken());
			userRepository.save(currentUser);
		} else {
			userRepository.removeFCMTokenByUserId(currentUser.getUserId());
		}
		return notificationDto;
	}
}
