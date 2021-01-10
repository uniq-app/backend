package pl.uniq.notifications.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.uniq.auth.user.User;

@Service
public class NotificationService {

	private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	@Async
	public void sendNotification(User user, String topic, String body) {
		String token = user.getFCMToken();
		if (token == null) {
			return;
		}
		Message message = Message.builder().
			setNotification(Notification.builder()
					.setTitle(topic)
					.setBody(body)
					.build()).
			setToken(token).
			build();

		try {
			String response = FirebaseMessaging.getInstance().send(message);
			logger.info("Successfully sent message: " + response);
		} catch (FirebaseMessagingException e) {
			logger.error(e.getMessage());
		}
	}
}
