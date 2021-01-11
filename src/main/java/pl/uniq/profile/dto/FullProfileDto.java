package pl.uniq.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.uniq.auth.user.Role;
import pl.uniq.auth.user.User;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullProfileDto {

	private String username;
	private String email;
	private String bio;
	private String avatar;
	private Set<Role> roles;

	public FullProfileDto(User user) {
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.bio = user.getBio();
		this.avatar = user.getAvatar();
		this.roles = user.getRoles();
	}
}
