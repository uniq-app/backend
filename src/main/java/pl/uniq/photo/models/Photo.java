package pl.uniq.photo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.uniq.board.models.Board;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "photos")
public class Photo {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(name = "value")
	private String value;

}
