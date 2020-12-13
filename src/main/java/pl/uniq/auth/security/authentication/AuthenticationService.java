package pl.uniq.auth.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.uniq.auth.dto.JwtTokenDto;
import pl.uniq.auth.dto.LoginDto;
import pl.uniq.auth.dto.SignUpDto;
import pl.uniq.auth.security.jwt.JwtToken;
import pl.uniq.auth.security.jwt.JwtTokenService;
import pl.uniq.auth.user.User;
import pl.uniq.auth.user.UserEntityRepository;

@Service
public class AuthenticationService {
	private AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final UserEntityRepository userEntityRepository;
	private final JwtTokenService jwtTokenService;
	private final JwtToken jwtToken;

	@Autowired
	public AuthenticationService(PasswordEncoder passwordEncoder, UserEntityRepository userEntityRepository, JwtTokenService jwtTokenService, JwtToken jwtToken) {
		this.passwordEncoder = passwordEncoder;
		this.userEntityRepository = userEntityRepository;
		this.jwtTokenService = jwtTokenService;
		this.jwtToken = jwtToken;
	}

	public String register(SignUpDto signUpDto) {
		if (!checkUserExists(signUpDto.getEmail())) {
			User user = User.builder().email(signUpDto.getEmail()).password(signUpDto.getPassword()).roles(signUpDto.getRoles()).build();
			userEntityRepository.save(user);
			return "Created";
		}
		return "User you want to register exists";
	}

	public JwtTokenDto login(LoginDto loginDto) {
		Authentication auth = authenticationManager.
				authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt = jwtToken.generateToken(auth);
		return new JwtTokenDto(jwt);
	}

	public String logout(String authHeader) {
		String token = jwtToken.parseJwt(authHeader);
		jwtTokenService.revokeToken(token);
		return "Logout";
	}


	private boolean checkUserExists(String email) {
		return userEntityRepository.existsByEmail(email);
	}
}
