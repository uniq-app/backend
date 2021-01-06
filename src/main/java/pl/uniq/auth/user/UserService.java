package pl.uniq.auth.user;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.uniq.auth.code.Code;
import pl.uniq.auth.code.CodeService;
import pl.uniq.auth.dto.AuthenticationResponse;
import pl.uniq.auth.security.email.EmailManager;
import pl.uniq.auth.security.jwt.JwtTokenService;
import pl.uniq.auth.security.jwt.JwtUtil;
import pl.uniq.auth.security.userdetails.CustomUserDetailsService;
import pl.uniq.auth.user.dto.ChangePasswordDto;
import pl.uniq.exceptions.UserOperationException;
import pl.uniq.utils.Message;

import javax.transaction.Transactional;

@Service
public class UserService {
	private final JwtUtil jwtUtil;
	private final CodeService codeService;
	private final UserRepository userRepository;
	private final JwtTokenService jwtTokenService;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService customUserDetailsService;


	@Autowired
	public UserService(JwtUtil jwtUtil, UserRepository userRepository, JwtTokenService jwtTokenService, PasswordEncoder passwordEncoder, CodeService codeService, CustomUserDetailsService customUserDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
		this.jwtTokenService = jwtTokenService;
		this.passwordEncoder = passwordEncoder;
		this.codeService = codeService;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Transactional
	public AuthenticationResponse updateUsername(String authHeader, User user, String newUsername) {
		if (!userRepository.existsByUsername(newUsername)) {
			user.setUsername(newUsername);
			userRepository.save(user);

			String token = jwtUtil.parseHeader(authHeader);
			jwtTokenService.deleteToken(token);
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(newUsername);
			String newToken = jwtUtil.generateToken(userDetails);
			jwtTokenService.addToken(newToken);
			return new AuthenticationResponse(newToken);
		}
		throw new UserOperationException(newUsername + " is used by other user");
	}

	public Message updatePassword(User user, ChangePasswordDto changePasswordDto) {
		String password = user.getPassword();
		boolean checkOldPassword = passwordEncoder.matches(changePasswordDto.getOldPassword(), password);
		boolean checkNewPassword = changePasswordDto.getNewPassword().equals(changePasswordDto.getRepeatedNewPassword());
		if (checkOldPassword) {
			if (checkNewPassword) {
				user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
				userRepository.save(user);
			} else {
				throw new UserOperationException("Passwords are not equals");
			}
		} else {
			throw new UserOperationException("Wrong password");
		}
		return new Message("Password has been updated");
	}

	public Message activateAccount(int codeValue) {
		Code code = codeService.getCodeByValue(codeValue);
		User user = userRepository.findUserByUserId(code.getUserId());
		if (codeService.validToken(code)) {
			user.setActive(true);
			userRepository.save(user);
			codeService.deleteCode(code);
			return new Message("Your account has been activated");
		} else {
			throw new UserOperationException("Cannot activate user. Code is expired");
		}
	}

	public Message resendActivationCode(String email) {
		User user = userRepository.findUserByEmail(email);
		if (user != null) {
			Code code = codeService.getCodeByUser(user);
			codeService.deleteCode(code);
			codeService.generateCode(user.getUserId());
			EmailManager.sendEmail(user.getEmail(), "Activation mail", "Activate your UNIQ account using this code: " + code.getValue());
			return new Message("Activation code has been send on your email");
		} else {
			throw new UserOperationException("Cannot find user with given email");
		}
	}

	public Message updateEmail(User user, String newEmail) {
		String oldEmail = user.getEmail();
		user.setEmail(newEmail);
		user.setActive(false);
		userRepository.save(user);
		Code code = codeService.generateCode(user.getUserId());
		EmailManager.sendEmail(oldEmail, "Activation mail", "Activate your UNIQ account using this code: " + code.getValue());
		return new Message("Activation code has been send on your previous email");
	}

	public Message forgotPassword(String email) {
		User user = userRepository.findUserByEmail(email);
		if (user != null) {
			if (user.isActive()) {
				RandomStringGenerator generator = new RandomStringGenerator.Builder().
						withinRange(48, 122).build();
				String password = generator.generate(20).replaceAll("[^a-zA-Z0-9]", "");
				user.setPassword(passwordEncoder.encode(password));
				userRepository.save(user);
				EmailManager.sendEmail(email, "Reset password", "There is your temporary password: " + password + ". Login and change password.");
				return new Message("Email has been sent, check your email");
			} else {
				throw new UserOperationException("Your email is not confirmed, we cannot reset your password");
			}
		} else {
			throw new UserOperationException("User with given email does not exist");
		}
	}
}
