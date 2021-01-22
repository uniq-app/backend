package pl.uniq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "uniq_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue
	private UUID userId;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Board> boards;

	@Lob
	@Column(name = "bio")
	@Type(type = "org.hibernate.type.TextType")
	private String bio;

	@Column(name = "avatar")
	private String avatar;

	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	@OnDelete(action= OnDeleteAction.CASCADE)
	@JoinColumn
	private Set<Role> roles;

	@Column(name = "is_active")
	private boolean isActive;

	@OneToMany(mappedBy = "from")
	private List<UserBoardFollow> following;

	@Column(name = "fcm_token")
	private String FCMToken;
}
