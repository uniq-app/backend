package pl.uniq.auth.user;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "uniq_user")
public class User {

	@Id
	@GeneratedValue
	private UUID user_id;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@ElementCollection
	@Enumerated(EnumType.STRING)
	private Set<Role> roles;
}
