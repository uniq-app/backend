package pl.uniq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.uniq.model.User;
import pl.uniq.repository.UserRepository;
import pl.uniq.model.CustomUserDetails;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		return new CustomUserDetails(user);
	}

	public CustomUserDetails loadUserByEmail(String email) {
		Optional<User> userOptional = userRepository.findUserByEmail(email);
		if (userOptional.isPresent())
		{
			return new CustomUserDetails(userOptional.get());
		}
		throw new BadCredentialsException("User does not exist");
	}
}
