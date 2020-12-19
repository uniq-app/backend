package pl.uniq.auth.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
	private final String jwt;
}
