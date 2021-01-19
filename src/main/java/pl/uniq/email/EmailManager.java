package pl.uniq.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Component
public class EmailManager {

	private static final Logger logger = LoggerFactory.getLogger(EmailManager.class);

	private static final String name = System.getenv("EMAIL_NAME");
	private static final String account = System.getenv("EMAIL_ACCOUNT");
	private static final String password = System.getenv("EMAIL_PASSWORD");
	private static final String host = System.getenv("EMAIL_HOST");
	private static final String port = System.getenv("EMAIL_PORT");


	public static void sendEmail(String recipient, String subject, String text) {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(account, password);
			}
		});

		Message message = prepareMessage(session, recipient, subject, text);
		if (message != null) {
			try {
				Transport.send(message);
			} catch (MessagingException e) {
				logger.error(e.getMessage());
			}
		}
	}

	private static Message prepareMessage(Session session, String recipient, String subject, String text) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(account, name));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject(subject);
			message.setText(text);
			return message;
		} catch (MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
