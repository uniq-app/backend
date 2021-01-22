package pl.uniq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.uniq.controller.BoardController;
import pl.uniq.model.Board;

import java.util.Date;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

	private UUID boardId;
	private String name;
	private String creatorName;
	private String description;
	private Boolean isPrivate;
	private Date timestamp;
	private String cover;
	private String extraData;
	private String photos;

	public static BoardDto create(Board board) {
		return builder().
				boardId(board.getBoardId()).
				name(board.getName()).
				creatorName(board.getUser().getUsername()).
				description(board.getDescription()).
				isPrivate(board.getIsPrivate()).
				timestamp(board.getTimestamp()).
				cover(board.getCover()).
				extraData(board.getExtraData()).
				photos(linkTo(BoardController.class).slash(board.getBoardId()).slash("photos").toUri().toString()).
				build();
	}
}
