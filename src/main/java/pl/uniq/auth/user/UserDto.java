package pl.uniq.auth.user;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
	private String username;
	private Set<Role> roles;
}
