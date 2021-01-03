package pl.uniq.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.uniq.auth.security.jwt.JwtTokenService;
import pl.uniq.auth.security.jwt.JwtUtil;
import pl.uniq.auth.security.userdetails.CustomUserDetailsService;
import pl.uniq.auth.user.dto.ChangePasswordDto;
import pl.uniq.exceptions.UserOperationException;

import javax.transaction.Transactional;

@Service
public class UserService {
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final JwtTokenService jwtTokenService;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService customUserDetailsService;


	@Autowired
	public UserService(JwtUtil jwtUtil, UserRepository userRepository, JwtTokenService jwtTokenService, PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
		this.jwtTokenService = jwtTokenService;
		this.passwordEncoder = passwordEncoder;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Transactional
	public String updateUsername(String authHeader, User user, String newUsername) {
		if (!userRepository.existsByUsername(newUsername)) {
			user.setUsername(newUsername);
			userRepository.save(user);

			String token = jwtUtil.parseHeader(authHeader);
			jwtTokenService.deleteToken(token);
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(newUsername);
			String newToken = jwtUtil.generateToken(userDetails);
			jwtTokenService.addToken(newToken);
			return newToken;
		}
		throw new UserOperationException(newUsername + " is used by other user");
	}

	public String updatePassword(User user, ChangePasswordDto changePasswordDto) {
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
		return "";
	}
}
