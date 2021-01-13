package pl.uniq.notifications.service;

import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.uniq.auth.user.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NotificationService {

	private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	@Async
	public void sendNotification(User user, String title, String body) {
		String token = user.getFCMToken();
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
	public void sendBatchNotification(List<User> users, String title, String body) {
		List<String> tokens = users.stream().map(User::getFCMToken).filter(Objects::nonNull).collect(Collectors.toList());

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
}
