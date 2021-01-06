package pl.uniq.auth.security.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailManager {

	private static final Logger logger = LoggerFactory.getLogger(EmailManager.class);

	//@Value("${EMAIL_ACCOUNT}")
	private static final String account = "uniq.app.auth@gmail.com";

	//@Value("${EMAIL_PASSWORD}")
	private static final String password = "bezhasla1";

	public static void sendEmail(String recipient, String subject, String text) {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(account, password);
			}
		});

		Message message = prepareMessage(session, account, recipient, subject, text);
		if (message != null) {
			try {
				Transport.send(message);
			} catch (MessagingException e) {
				logger.error(e.getMessage());
			}
		}
	}

	private static Message prepareMessage(Session session, String myAccountEmail, String recipient, String subject, String text) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject(subject);
			message.setText(text);
			return message;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null;
	}
}