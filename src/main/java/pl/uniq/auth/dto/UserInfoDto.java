package pl.uniq.auth.dto;

import pl.uniq.auth.user.Role;

import java.util.Set;

public class UserInfoDto {
	private String email;
	private Set<Role> roles;
}
