package pl.uniq.utils;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import pl.uniq.model.CustomUserDetails;
import pl.uniq.model.User;
import pl.uniq.utils.JwtUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtUtilTest {

	@Test
	void test1() {
		User user = User.builder()
				.username("johny")
				.build();
		UserDetails userDetails = new CustomUserDetails(user);
		JwtUtil jwtUtil = new JwtUtil();
		String jwt = jwtUtil.generateToken(userDetails);
		System.out.println(jwt);
		assertEquals(user.getUsername(), jwtUtil.extractUsername(jwt));
	}
}
