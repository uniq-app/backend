package pl.uniq.auth.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.uniq.auth.code.Code;
import pl.uniq.auth.code.CodeService;
import pl.uniq.auth.dto.AuthenticationResponse;
import pl.uniq.auth.dto.AuthenticationRequest;
import pl.uniq.auth.security.email.EmailManager;
import pl.uniq.auth.security.jwt.JwtUtil;
import pl.uniq.auth.security.jwt.JwtTokenService;
import pl.uniq.auth.security.userdetails.CustomUserDetails;
import pl.uniq.auth.security.userdetails.CustomUserDetailsService;
import pl.uniq.auth.user.Role;
import pl.uniq.auth.user.User;
import pl.uniq.auth.user.UserRepository;
import pl.uniq.utils.Message;

import javax.security.auth.login.AccountException;
import javax.transaction.Transactional;
import java.util.Set;

@Service
public class AuthenticationService {

	private final JwtUtil jwtUtil;
	private final CodeService codeService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenService jwtTokenService;
	private final AuthenticationManager authenticationManager;
	private final CustomUserDetailsService customUserDetailsService;

	@Autowired
	public AuthenticationService(JwtUtil jwtUtil, CodeService codeService, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService, AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService) {
		this.jwtUtil = jwtUtil;
		this.codeService = codeService;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenService = jwtTokenService;
		this.authenticationManager = authenticationManager;
		this.customUserDetailsService = customUserDetailsService;
	}

	public Message register(AuthenticationRequest authenticationRequest) {
		if (!userRepository.existsByUsername(authenticationRequest.getUsername())) {
			if (!userRepository.existsByEmail(authenticationRequest.getEmail())) {
				User user = User.builder().
						email(authenticationRequest.getEmail()).
						username(authenticationRequest.getUsername()).
						password(passwordEncoder.encode(authenticationRequest.getPassword())).
						roles(Set.of(Role.UNIQ_USER)).
						isActive(false).
						build();
				userRepository.save(user);
				Code code = codeService.generateCode(user.getUserId());
				EmailManager.sendEmail(authenticationRequest.getEmail(), "Activation mail", "Activate your UNIQ account using this code: " + code.getValue());
				return new Message("User has been created");
			} else {
				throw new BadCredentialsException("This email is used by other user");
			}
		} else {
			throw new BadCredentialsException("Given username is not available");
		}
	}

	public AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws AccountException {
		CustomUserDetails userDetails = customUserDetailsService.loadUserByEmail(authenticationRequest.getEmail());
		if (authenticationRequest.getEmail().equals(userDetails.getEmail()) && passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword())) {
			if (userDetails.isEnabled()) {
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), authenticationRequest.getPassword()));
				String jwt = jwtUtil.generateToken(userDetails);
				jwtTokenService.addToken(jwt);
				User currentUser = userDetails.getUser();
				currentUser.setFCMToken(authenticationRequest.getFCMToken());
				userRepository.save(currentUser);
				return new AuthenticationResponse(jwt);
			} else {
				throw new AccountException("Account is not active. Use code on sent on your email to activate account");
			}
		} else {
			throw new BadCredentialsException("Wrong credentials");
		}
	}

	@Transactional
	public Message logout(String authHeader) {
		String token = jwtUtil.parseHeader(authHeader);
		userRepository.removeFCMTokenByUsername(jwtUtil.extractUsername(token));
		jwtTokenService.deleteToken(token);
		return new Message("User has been logged out");
	}
}
