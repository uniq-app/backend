package pl.uniq.auth.dto;

import lombok.Data;
import pl.uniq.auth.user.Role;

import java.util.Set;

@Data
public class SignUpDto {
	private String email;
	private String password;
	private final Set<Role> roles = Set.of(Role.UNIQ_USER);
}
