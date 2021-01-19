package pl.uniq.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordDto {
	String oldPassword;
	String newPassword;
	String repeatedNewPassword;
}
