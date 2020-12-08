package pl.uniq.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.board.models.Board;
import pl.uniq.board.models.BoardResults;
import pl.uniq.board.repository.BoardRepository;
import pl.uniq.photo.models.Photo;
import pl.uniq.photo.service.PhotoService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

	private final BoardRepository boardRepository;
	private final PhotoService photoService;

	@Autowired
	public BoardService(BoardRepository boardRepository, PhotoService photoService) {
		this.boardRepository = boardRepository;
		this.photoService = photoService;
	}

	public BoardResults test() {
//		UUID board_test = UUID.fromString("fe2b40df-e4e7-4930-b62d-2084d515f5fe");
//		Board test = Board.builder().id(board_test).name("test").creatorId("1").isPrivate(false).isCreatorHidden(false).timestamp(Timestamp.from(Instant.now())).build();
//		boardRepository.save(test);
//		Photo photo_test = Photo.builder().id(UUID.randomUUID()).value("test_photo").board(test).build();
//		photoService.save(List.of(photo_test));

		List<Board> boards = Arrays.asList(
				Board.builder().id(UUID.randomUUID()).name("1").creatorId("1").isPrivate(false).isCreatorHidden(false).timestamp(Timestamp.from(Instant.now())).build(),
				Board.builder().id(UUID.randomUUID()).name("1").creatorId("1").isPrivate(false).isCreatorHidden(false).timestamp(Timestamp.from(Instant.now())).build(),
				Board.builder().id(UUID.randomUUID()).name("1").creatorId("1").isPrivate(false).isCreatorHidden(false).timestamp(Timestamp.from(Instant.now())).build()
		);
		return new BoardResults(boards);
	}

	public List<Board> findAll() {
		return boardRepository.findAll();
	}

	public List<Board> findAll(String id) {
		return boardRepository.findAllByCreatorId(id);
	}

	public Board findById(UUID uuid) throws ResourceNotFoundException {
		return boardRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Resource with id: " + uuid + " not found!"));
	}

	public Board save(Board board) {
		boardRepository.save(board);
		return board;
	}

	public Board updateBoard(UUID uuid, Board board) {
		Board storedBoard = findById(uuid);
		if (board.getName() != null) storedBoard.setName(board.getName());
		if (board.getCreatorId() != null) storedBoard.setCreatorId(board.getCreatorId());
		if (board.getIsPrivate() != null) storedBoard.setIsPrivate(board.getIsPrivate());
		if (board.getIsCreatorHidden() != null) storedBoard.setIsCreatorHidden(board.getIsCreatorHidden());
		return storedBoard;
	}

	public void delete(Board board) {
		boardRepository.delete(board);
	}
}
