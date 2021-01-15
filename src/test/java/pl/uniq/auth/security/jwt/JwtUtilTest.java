package pl.uniq.auth.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import pl.uniq.auth.security.userdetails.CustomUserDetails;
import pl.uniq.auth.user.User;

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
