package pl.uniq.notifications.service;

import com.google.auth.oauth2.GoogleCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
public class NotificationInitializer {

	@Value(value = "${FIREBASE_CREDENTIALS_PATH}")
	private String configPath;

	private final Logger logger = LoggerFactory.getLogger(NotificationInitializer.class);

	@PostConstruct
	public void init() {
		try {
			InputStream config = new PathResource(configPath).getInputStream();
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(config))
					.build();
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				logger.info("Firebase Admin SDK initialized from config file");
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}
