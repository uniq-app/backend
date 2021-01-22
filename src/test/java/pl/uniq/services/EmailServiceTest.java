package pl.uniq.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pl.uniq.service.EmailService;

import javax.mail.MessagingException;

class EmailServiceTest {

	@Test
	@Disabled
	void test() throws MessagingException {
		EmailService.sendEmail("darullef4@gmail.com", "test", "test");
	}
}
