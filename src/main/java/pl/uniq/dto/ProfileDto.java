package pl.uniq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.uniq.model.User;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
	private String username;
	private String bio;
	private String avatar;

	public ProfileDto(User user) {
		this.username = user.getUsername();
		this.bio = user.getBio();
		this.avatar = user.getAvatar();
	}
}
