package pl.uniq.auth.security.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.uniq.auth.user.User;
import pl.uniq.auth.user.UserEntityRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserEntityRepository userEntityRepository;

	@Autowired
	public CustomUserDetailsService(UserEntityRepository userEntityRepository) {
		this.userEntityRepository = userEntityRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userEntityRepository.findByEmail(email);
		return new CustomUserDetails(user);
	}
}
