package pl.uniq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import pl.uniq.dto.PhotoDto;

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
	@Column(name = "photo_id")
	private UUID photoId;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Board.class)
	private Board board;

	@Column(name = "value")
	private String value;

	@Column(name = "sort_order", columnDefinition="integer not null default 0")
	private Integer order;

	@Lob
	@Column(name = "extra_data")
	@Type(type = "org.hibernate.type.TextType")
	private String extraData;

	public static Photo create(PhotoDto photoDto, Board board) {
		return builder()
				.board(board)
				.value(photoDto.getValue())
				.order(photoDto.getOrder())
				.extraData(photoDto.getExtraData())
				.build();
	}
}
