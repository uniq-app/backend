package pl.uniq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board")
public class Board {

	@Id
	@GeneratedValue
	private UUID boardId;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	private User user;

	@Column(name = "name")
	private String name;

	@Lob
	@Column(name = "description")
	@Type(type = "org.hibernate.type.TextType")
	private String description;

	@Column(name = "is_private")
	private Boolean isPrivate = false;

	@Column(name = "created_at")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date timestamp = new Date();

	@Column(name = "cover")
	private String cover;

	@Lob
	@Column(name = "extra_data")
	@Type(type = "org.hibernate.type.TextType")
	private String extraData;

	@OneToMany(mappedBy = "to", cascade = CascadeType.REMOVE)
	private List<UserBoardFollow> followers;

	@OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
	private List<Photo> photos;

}
