package pl.uniq.auth.user.dto;

import lombok.Data;
import pl.uniq.auth.user.Role;

import java.util.Set;

@Data
public class UserDto {
	private String username;
	private Set<Role> roles;
}
