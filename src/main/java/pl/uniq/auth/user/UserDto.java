package pl.uniq.auth.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	private String username;
	private String email;
	private String bio;
	private String avatar;
	private Set<Role> roles;

	public UserDto(User user) {
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.bio = user.getBio();
		this.avatar = user.getAvatar();
		this.roles = user.getRoles();
	}
}
