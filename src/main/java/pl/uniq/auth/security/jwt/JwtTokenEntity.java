package pl.uniq.auth.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenEntity {

	@Id
	private UUID token_id;

	@Column(name = "created_at")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private final Date created_at = new Date();
}
