package pl.uniq.board.models;

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
@Table(name = "board")
public class Board {

	@Id
	@GeneratedValue
	private UUID boardId;

	@Column(name = "user_id")
	private UUID userId;

	@Column(name = "name")
	private String name;

	@Column(name = "is_private")
	private Boolean isPrivate = false;

	@Column(name = "is_creator_hidden")
	private Boolean isCreatorHidden = false;

	@Column(name = "created_at")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date timestamp = new Date();
}
