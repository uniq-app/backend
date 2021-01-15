package pl.uniq.profile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uniq.auth.user.User;
import pl.uniq.auth.user.UserRepository;
import pl.uniq.profile.dto.ProfileDto;

@Service
public class ProfileService {
	private final UserRepository userRepository;

	@Autowired
	public ProfileService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User update(User currentUser, ProfileDto requestBodyUser) {
		if (requestBodyUser.getUsername() != null)
			currentUser.setUsername(requestBodyUser.getUsername());
		if (requestBodyUser.getBio() != null)
			currentUser.setBio(requestBodyUser.getBio());
		if (requestBodyUser.getAvatar() != null)
			currentUser.setAvatar(requestBodyUser.getAvatar());
		userRepository.save(currentUser);
		return currentUser;
	}
}
