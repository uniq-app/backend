package pl.uniq.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.uniq.model.Code;
import pl.uniq.dto.CodeDto;
import pl.uniq.service.CodeService;
import pl.uniq.dto.AuthenticationResponse;
import pl.uniq.service.JwtTokenService;
import pl.uniq.utils.JwtUtil;
import pl.uniq.service.CustomUserDetailsService;
import pl.uniq.model.User;
import pl.uniq.repository.UserRepository;
import pl.uniq.service.UserService;
import pl.uniq.dto.ChangePasswordDto;
import pl.uniq.dto.EmailDto;
import pl.uniq.dto.ResetPasswordDto;
import pl.uniq.exceptions.CodeException;
import pl.uniq.exceptions.UserOperationException;
import pl.uniq.dto.Message;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class UserServiceTest {

	String JOHNY_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2hueSIsImlhdCI6MTYxMDc0MTgxNSwiZXhwIjoxNjExMzQ2NjE1fQ.51SasCIppoJzT8Q5N8T_a-xUXjixqKHDGM_iwEimQg31vzSNMHQKMcbEb1szdRsHxCB_7W3kRulPr7Mk1lppbg";

	@Test
	void test1() {
		User user = User.builder()
				.username("johny")
				.build();
		User userData = User.builder()
				.username("joe")
				.build();
		UserRepository userRepository = mock(UserRepository.class);
		JwtTokenService jwtTokenService = mock(JwtTokenService.class);
		JwtUtil jwtUtil = new JwtUtil();
		doReturn(user).when(userRepository)
				.findByUsername("johny");
		doReturn(user).when(userRepository)
				.findByUsername("joe");
		CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService(userRepository);
		doReturn(true).when(userRepository)
				.existsByUsername("johny");
		UserService userService = new UserService(jwtUtil, userRepository, jwtTokenService, mock(PasswordEncoder.class), mock(CodeService.class), customUserDetailsService);
		String authHeader = "Bearer " + JOHNY_TOKEN;
		AuthenticationResponse result = userService.updateUsername(authHeader, user, userData);
		assertEquals("joe", jwtUtil.extractUsername(result.getJwt()));
	}

	@Test
	void test2() {
		User user = User.builder()
				.username("johny")
				.build();
		User userData = User.builder()
				.username("joe")
				.build();
		UserRepository userRepository = mock(UserRepository.class);
		JwtTokenService jwtTokenService = mock(JwtTokenService.class);
		JwtUtil jwtUtil = new JwtUtil();
		CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService(userRepository);
		doReturn(user).when(userRepository)
				.findByUsername("joe");
		doReturn(true).when(userRepository)
				.existsByUsername("joe");
		UserService userService = new UserService(jwtUtil, userRepository, jwtTokenService, mock(PasswordEncoder.class), mock(CodeService.class), customUserDetailsService);
		String authHeader = "Bearer " + JOHNY_TOKEN;
		assertThrows(UserOperationException.class, () -> userService.updateUsername(authHeader, user, userData), userData.getUsername() + " is used by other user");
	}

	@Test
	void test3() {
		User user = User.builder()
				.username("johny")
				.password("$2a$10$WX5M1WGFRc3VmzqpAAT5ouPpHsFMQOf8gRyM7Mybp8/JY88BhQb6y")
				.build();
		ChangePasswordDto passwordDto = ChangePasswordDto.builder()
				.oldPassword("oldPassword")
				.newPassword("newPassword")
				.repeatedNewPassword("newPassword")
				.build();
		UserRepository userRepository = mock(UserRepository.class);
		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
		doReturn(true).when(passwordEncoder)
				.matches(passwordDto.getOldPassword(), user.getPassword());
		UserService userService = new UserService(new JwtUtil(), userRepository, mock(JwtTokenService.class), passwordEncoder, mock(CodeService.class), mock(CustomUserDetailsService.class));
		Message result = userService.updatePassword(user, passwordDto);
		assertEquals("Password has been updated", result.getMessage());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void test4() {
		User user = User.builder()
				.username("johny")
				.password("$2a$10$WX5M1WGFRc3VmzqpAAT5ouPpHsFMQOf8gRyM7Mybp8/JY88BhQb6y")
				.build();
		ChangePasswordDto passwordDto = ChangePasswordDto.builder()
				.oldPassword("oldPassword")
				.newPassword("newPassword")
				.repeatedNewPassword("wrongPassword")
				.build();
		UserRepository userRepository = mock(UserRepository.class);
		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
		doReturn(true).when(passwordEncoder)
				.matches(passwordDto.getOldPassword(), user.getPassword());
		UserService userService = new UserService(new JwtUtil(), userRepository, mock(JwtTokenService.class), passwordEncoder, mock(CodeService.class), mock(CustomUserDetailsService.class));
		assertThrows(UserOperationException.class, () -> userService.updatePassword(user, passwordDto), "Passwords are not equals");
	}

	@Test
	void test5() {
		User user = User.builder()
				.username("johny")
				.password("$2a$10$WX5M1WGFRc3VmzqpAAT5ouPpHsFMQOf8gRyM7Mybp8/JY88BhQb6y")
				.build();
		ChangePasswordDto passwordDto = ChangePasswordDto.builder()
				.oldPassword("veryWrongPassword")
				.newPassword("newPassword")
				.repeatedNewPassword("wrongPassword")
				.build();
		UserRepository userRepository = mock(UserRepository.class);
		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
		doReturn(false).when(passwordEncoder)
				.matches(passwordDto.getOldPassword(), user.getPassword());
		UserService userService = new UserService(new JwtUtil(), userRepository, mock(JwtTokenService.class), passwordEncoder, mock(CodeService.class), mock(CustomUserDetailsService.class));
		assertThrows(UserOperationException.class, () -> userService.updatePassword(user, passwordDto), "Wrong password");
	}

	@Test
	void test6() {
		UUID userId = UUID.randomUUID();
		User user = User.builder()
				.userId(userId)
				.isActive(false)
				.build();
		Code code = Code.builder()
				.value(213769)
				.userId(userId)
				.expiresAt(Date.from(Instant.now()
						.plusSeconds(2137L)))
				.build();
		CodeService codeService = mock(CodeService.class);
		doReturn(code).when(codeService)
				.getCodeByValue(213769);
		doReturn(true).when(codeService)
				.validToken(eq(code));
		UserRepository userRepository = mock(UserRepository.class);
		doReturn(Optional.of(user)).when(userRepository)
				.findUserByUserId(code.getUserId());
		UserService userService = new UserService(new JwtUtil(), userRepository, mock(JwtTokenService.class), mock(PasswordEncoder.class), codeService, mock(CustomUserDetailsService.class));
		Message result = userService.activateAccount(new CodeDto(213769));
		assertEquals("Your account has been activated", result.getMessage());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void test7() {
		UUID userId = UUID.randomUUID();
		User user = User.builder()
				.userId(userId)
				.isActive(false)
				.build();
		Code code = Code.builder()
				.value(213769)
				.userId(userId)
				.build();
		CodeService codeService = mock(CodeService.class);
		doReturn(code).when(codeService)
				.getCodeByValue(213769);
		UserRepository userRepository = mock(UserRepository.class);
		doReturn(Optional.of(user)).when(userRepository)
				.findUserByUserId(code.getUserId());
		UserService userService = new UserService(new JwtUtil(), userRepository, mock(JwtTokenService.class), mock(PasswordEncoder.class), codeService, mock(CustomUserDetailsService.class));
		assertThrows(UserOperationException.class, () -> userService.activateAccount(new CodeDto(213769)), "Cannot activate user. Code is expired");
	}

	@Test
	void test8() {
		UUID userId = UUID.randomUUID();
		User user = User.builder()
				.userId(userId)
				.build();
		EmailDto emailDto = new EmailDto("new.email@mail.com");
		UserRepository userRepository = mock(UserRepository.class);
		UserService userService = new UserService(new JwtUtil(), userRepository, mock(JwtTokenService.class), mock(PasswordEncoder.class), mock(CodeService.class), mock(CustomUserDetailsService.class));
		userService.updateEmail(user, emailDto);
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void test9() {
		UUID userId = UUID.randomUUID();
		User user = User.builder()
				.userId(userId)
				.build();
		Code code = Code.builder()
				.value(213769)
				.userId(userId)
				.build();
		CodeService codeService = mock(CodeService.class);
		doReturn(code).when(codeService)
				.getCodeByValue(213769);
		UserRepository userRepository = mock(UserRepository.class);
		doReturn(Optional.of(user)).when(userRepository)
				.findUserByUserId(code.getUserId());
		doReturn(true).when(codeService)
				.validToken(eq(code));
		UserService userService = new UserService(new JwtUtil(), userRepository, mock(JwtTokenService.class), mock(PasswordEncoder.class), codeService, mock(CustomUserDetailsService.class));
		Message result = userService.validCode(new CodeDto(213769));
		assertEquals("Code is correct", result.getMessage());
	}

	@Test
	void test10() {
		UUID userId = UUID.randomUUID();
		Code code = Code.builder()
				.value(213769)
				.userId(userId)
				.build();
		CodeService codeService = mock(CodeService.class);
		doReturn(code).when(codeService)
				.getCodeByValue(213769);
		UserRepository userRepository = mock(UserRepository.class);
		doReturn(Optional.empty()).when(userRepository)
				.findUserByUserId(code.getUserId());
		doReturn(true).when(codeService)
				.validToken(eq(code));
		UserService userService = new UserService(new JwtUtil(), userRepository, mock(JwtTokenService.class), mock(PasswordEncoder.class), codeService, mock(CustomUserDetailsService.class));
		assertThrows(UserOperationException.class, () -> userService.validCode(new CodeDto(213769)), "Code is incorrect");
	}

	@Test
	void test11() {
		UUID userId = UUID.randomUUID();
		User user = User.builder()
				.userId(userId)
				.build();
		Code code = Code.builder()
				.value(213769)
				.userId(userId)
				.build();
		CodeService codeService = mock(CodeService.class);
		doReturn(code).when(codeService)
				.getCodeByValue(213769);
		UserRepository userRepository = mock(UserRepository.class);
		doReturn(Optional.of(user)).when(userRepository)
				.findUserByUserId(code.getUserId());
		doReturn(false).when(codeService)
				.validToken(eq(code));
		UserService userService = new UserService(new JwtUtil(), userRepository, mock(JwtTokenService.class), mock(PasswordEncoder.class), codeService, mock(CustomUserDetailsService.class));
		assertThrows(CodeException.class, () -> userService.validCode(new CodeDto(213769)), "Code is expired");
	}

	@Test
	void test12() {
		UUID userId = UUID.randomUUID();
		User user = User.builder()
				.userId(userId)
				.email("email.email@email.com")
				.build();
		ResetPasswordDto resetPasswordDto = ResetPasswordDto.builder()
				.email("email.email@email.com")
				.password("password")
				.build();
		UserRepository userRepository = mock(UserRepository.class);
		doReturn(Optional.of(user)).when(userRepository)
				.findUserByEmail("email.email@email.com");
		UserService userService = new UserService(new JwtUtil(), userRepository, mock(JwtTokenService.class), mock(PasswordEncoder.class), mock(CodeService.class), mock(CustomUserDetailsService.class));
		Message result = userService.resetPassword(resetPasswordDto);
		assertEquals("Your password has been reset successfully", result.getMessage());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void test13() {
		UUID userId = UUID.randomUUID();
		ResetPasswordDto resetPasswordDto = ResetPasswordDto.builder()
				.email("email.email@email.com")
				.password("password")
				.build();
		UserRepository userRepository = mock(UserRepository.class);
		doReturn(Optional.empty()).when(userRepository)
				.findUserByEmail("email.email@email.com");
		UserService userService = new UserService(new JwtUtil(), userRepository, mock(JwtTokenService.class), mock(PasswordEncoder.class), mock(CodeService.class), mock(CustomUserDetailsService.class));
		Message result = userService.resetPassword(resetPasswordDto);
		assertEquals("Cannot find user with given email. Check email", result.getMessage());
	}


	@Test
	void test14() {
		UUID userId = UUID.randomUUID();
		User user = User.builder()
				.userId(userId)
				.isActive(false)
				.build();
		UserRepository userRepository = mock(UserRepository.class);
		UserService userService = new UserService(new JwtUtil(), userRepository, mock(JwtTokenService.class), mock(PasswordEncoder.class), mock(CodeService.class), mock(CustomUserDetailsService.class));
		userService.deleteUser(user);
		verify(userRepository, times(1)).delete(any(User.class));
	}
}
