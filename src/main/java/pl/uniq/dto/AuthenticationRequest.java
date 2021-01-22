package pl.uniq.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthenticationRequest implements Serializable {
	private String email;
	private String username;
	private String password;
	private String FCMToken;
}
