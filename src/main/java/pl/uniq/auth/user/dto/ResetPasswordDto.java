package pl.uniq.auth.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordDto {
	private String email;
	private String password;
}
