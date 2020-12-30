package pl.uniq.auth.dto;

import lombok.Data;
import pl.uniq.auth.user.Role;

import java.io.Serializable;
import java.util.Set;

@Data
public class AuthenticationRequest implements Serializable {
	private String username;
	private String password;
	private final Set<Role> roles = Set.of(Role.UNIQ_USER);
}
