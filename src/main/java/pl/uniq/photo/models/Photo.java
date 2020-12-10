package pl.uniq.photo.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.uniq.board.models.Board;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "photos")
public class Photo {

	@Id
	@GeneratedValue
	private UUID photo_id;

	@Column(name = "board_id")
	private UUID board;

	@Column(name = "value")
	private String value;

}
