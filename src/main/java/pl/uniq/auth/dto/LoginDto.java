package pl.uniq.auth.dto;

import lombok.Data;

@Data
public class LoginDto {
	private String email;
	private String password;
}
