package pl.uniq.utils;

import org.junit.jupiter.api.Test;
import pl.uniq.dto.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageTest {

	@Test
	void test1() {
		Message message = new Message("TEST MESSAGE");
		assertEquals("TEST MESSAGE", message.getMessage());
	}
}
