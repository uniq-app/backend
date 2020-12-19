package pl.uniq.auth.security.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.uniq.auth.dto.AuthenticationResponse;
import pl.uniq.auth.dto.AuthenticationRequest;
import pl.uniq.auth.security.jwt.JwtUtil;
import pl.uniq.auth.security.jwt.JwtTokenService;
import pl.uniq.auth.security.userdetails.CustomUserDetailsService;
import pl.uniq.auth.user.User;
import pl.uniq.auth.user.UserEntityRepository;

@Service
public class AuthenticationService {
	private final static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	private final CustomUserDetailsService customUserDetailsService;
	private final AuthenticationManager authenticationManager;
	private final UserEntityRepository userEntityRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenService jwtTokenService;
	private final JwtUtil jwtUtil;

	@Autowired
	public AuthenticationService(AuthenticationManager authenticationManager, UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService, CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.userEntityRepository = userEntityRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenService = jwtTokenService;
		this.customUserDetailsService = customUserDetailsService;
		this.jwtUtil = jwtUtil;
	}

	public String register(AuthenticationRequest authenticationRequest) {
		if (!checkUserExists(authenticationRequest.getUsername())) {
			User user = User.builder().
					username(authenticationRequest.getUsername()).
					password(passwordEncoder.encode(authenticationRequest.getPassword())).
					roles(authenticationRequest.getRoles()).
					build();
			userEntityRepository.save(user);
			return "Created";
		}
		return "User you want to register exists";
	}

	public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			logger.error("Incorrect username or password");
		}
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String jwt = jwtUtil.generateToken(userDetails);
		jwtTokenService.addToken(jwt);
		return new AuthenticationResponse(jwt);
	}

	public String logout(String authHeader) {
		String token = jwtUtil.parseHeader(authHeader);
		jwtTokenService.revokeToken(token);
		return "Logout";
	}

	private boolean checkUserExists(String email) {
		return userEntityRepository.existsByUsername(email);
	}
}
