package pl.uniq.board.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import pl.uniq.photo.models.Photo;

import javax.persistence.*;
import java.util.Date;
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

	@Column(name = "user_id")
	private UUID userId;

	@Column(name = "name")
	private String name;

	@Lob
	@Column(name = "description")
	@Type(type = "org.hibernate.type.TextType")
	private String description;

	@Column(name = "is_private")
	private Boolean isPrivate = false;

	@Column(name = "is_creator_hidden")
	private Boolean isCreatorHidden = false;

	@Column(name = "created_at")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date timestamp = new Date();

	@OneToOne(targetEntity = Photo.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "cover_id", referencedColumnName = "photo_id")
	private Photo cover;
}
