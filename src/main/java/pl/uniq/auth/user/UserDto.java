package pl.uniq.auth.user;

import lombok.Data;
import pl.uniq.auth.user.Role;

import java.util.Set;

@Data
public class UserDto {
	private String email;
	private Set<Role> roles;
}
