package pl.uniq.auth.security.authorizartion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.uniq.auth.security.userdetails.CustomUserDetails;
import pl.uniq.user.User;
import pl.uniq.user.UserRepository;

@Service
public class AuthorizationService {

	private final UserRepository userRepository;

	@Autowired
	public AuthorizationService(UserRepository userRepository) {this.userRepository = userRepository;}

	public User getCurrentUser() {
		UserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepository.findByUsername(userDetails.getUsername());
	}
}