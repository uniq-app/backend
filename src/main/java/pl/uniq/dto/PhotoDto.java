package pl.uniq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.uniq.model.Photo;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDto {

	private UUID photoId;
	private UUID boardId;
	private String value;
	private Integer order;
	private String extraData;

	public PhotoDto(Photo photo) {
		this.photoId = photo.getPhotoId();
		this.boardId = photo.getBoard().getBoardId();
		this.value = photo.getValue();
		this.order = photo.getOrder();
		this.extraData = photo.getExtraData();
	}
}
