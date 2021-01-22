package pl.uniq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "code")
@NoArgsConstructor
@AllArgsConstructor
public class Code {
	
	@Id
	@GeneratedValue
	private UUID codeId;

	@Column(name = "userId")
	private UUID userId;

	@Column(name = "value")
	private int value;

	@Column(name = "expires_at")
	private Date expiresAt;
}
