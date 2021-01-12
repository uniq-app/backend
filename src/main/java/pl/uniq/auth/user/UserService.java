package pl.uniq.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.uniq.auth.code.Code;
import pl.uniq.auth.code.CodeDto;
import pl.uniq.auth.code.CodeService;
import pl.uniq.auth.dto.AuthenticationResponse;
import pl.uniq.auth.security.email.EmailManager;
import pl.uniq.auth.security.jwt.JwtTokenService;
import pl.uniq.auth.security.jwt.JwtUtil;
import pl.uniq.auth.security.userdetails.CustomUserDetailsService;
import pl.uniq.auth.user.dto.ChangeEmailDto;
import pl.uniq.auth.user.dto.ChangePasswordDto;
import pl.uniq.auth.user.dto.EmailDto;
import pl.uniq.auth.user.dto.ResetPasswordDto;
import pl.uniq.exceptions.CodeException;
import pl.uniq.exceptions.UserOperationException;
import pl.uniq.utils.Message;

import javax.transaction.Transactional;
import java.util.Optional;

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
	public AuthenticationResponse updateUsername(String authHeader, User currentUser, User user) {
		if (!userRepository.existsByUsername(user.getUsername())) {
			currentUser.setUsername(user.getUsername());
			userRepository.save(currentUser);

			String token = jwtUtil.parseHeader(authHeader);
			jwtTokenService.deleteToken(token);
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
			String newToken = jwtUtil.generateToken(userDetails);
			jwtTokenService.addToken(newToken);
			return new AuthenticationResponse(newToken);
		}
		throw new UserOperationException(user.getUsername() + " is used by other user");
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

	public Message activateAccount(CodeDto codeDto) {
		Code code = codeService.getCodeByValue(codeDto.getCodeValue());
		Optional<User> userOptional = userRepository.findUserByUserId(code.getUserId());
		if (codeService.validToken(code) && userOptional.isPresent()) {
			User user = userOptional.get();
			user.setActive(true);
			userRepository.save(user);
			codeService.deleteCode(code);
			return new Message("Your account has been activated");
		} else {
			throw new UserOperationException("Cannot activate user. Code is expired");
		}
	}

	public Message resendActivationCode(EmailDto emailDto) {
		Optional<User> userOptional = userRepository.findUserByEmail(emailDto.getEmail());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			Code code = codeService.getCodeByUser(user);
			if (code != null)
			{
				codeService.deleteCode(code);
			}
			Code newCode = codeService.generateCode(user.getUserId());
			EmailManager.sendEmail(user.getEmail(), "Activation mail", "Activate your UNIQ account using this code: " + newCode.getValue());
			return new Message("Activation code has been send on your email");
		} else {
			throw new UserOperationException("Cannot find user with given email");
		}
	}

	public Message sendCodeToChangeEmail(User user)
	{
		Code code = codeService.generateCode(user.getUserId());
		EmailManager.sendEmail(user.getEmail(), "Change mail code", "Change your email in UNIQ account using this code: " + code.getValue());
		return new Message("Code to change your email has been sent on your email");
	}

	public Message updateEmail(User user, ChangeEmailDto changeEmailDto) {
		Code code = codeService.getCodeByValue(changeEmailDto.getCodeValue());
		if (codeService.validToken(code)) {
			user.setEmail(changeEmailDto.getNewEmail());
			userRepository.save(user);
			codeService.deleteCode(code);
			return new Message("Your email has been updated");
		}
		throw new CodeException("Code is expired");
	}

	public Message sendCodeToResetPassword(EmailDto email) {
		Optional<User> userOptional = userRepository.findUserByEmail(email.getEmail());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			Code code = codeService.generateCode(user.getUserId());
			EmailManager.sendEmail(email.getEmail(), "Activation mail", "Activate your UNIQ account using this code: " + code.getValue());
			return new Message("The email with recovery code has been sent on your email");
		}
		return new Message("The email with recovery code has been sent on your email");
	}

	public Message validCode(CodeDto codeDto) {
		Code code = codeService.getCodeByValue(codeDto.getCodeValue());
		Optional<User> userOptional = userRepository.findUserByUserId(code.getUserId());
		if (codeService.validToken(code)) {
			if (userOptional.isPresent()) {
				codeService.deleteCode(code);
				return new Message("Code is correct");
			}
			throw new UserOperationException("Code is incorrect");
		}
		throw new CodeException("Code is expired");
	}

	public Message resetPassword(ResetPasswordDto resetPasswordDto) {
		Optional<User> userOptional = userRepository.findUserByEmail(resetPasswordDto.getEmail());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			user.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
			userRepository.save(user);
			return new Message("Your password has been reset successfully");
		}
		return new Message("Cannot find user with given email. Check email");
	}
}
