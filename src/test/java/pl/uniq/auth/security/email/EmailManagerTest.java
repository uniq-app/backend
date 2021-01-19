package pl.uniq.auth.security.email;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pl.uniq.email.EmailManager;

import javax.mail.MessagingException;

class EmailManagerTest {

	@Test
	@Disabled
	void test() throws MessagingException {
		EmailManager.sendEmail("darullef4@gmail.com", "test", "test");
	}
}
