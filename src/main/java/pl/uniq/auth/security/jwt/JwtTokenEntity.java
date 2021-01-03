package pl.uniq.auth.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class JwtTokenEntity {

	@Id
	private UUID tokenId;

	@Column(name = "token")
	private String token;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "created_at")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private final Date created_at = new Date();
}
