package pl.uniq.auth.dto;

import lombok.Data;
import pl.uniq.auth.user.Role;

import java.io.Serializable;
import java.util.Set;

@Data
public class AuthenticationRequest implements Serializable {
	private String email;
	private String username;
	private String password;
	private String FCMToken;
}
