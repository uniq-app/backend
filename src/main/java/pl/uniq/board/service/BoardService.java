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
		List<Photo> photos = Collections.singletonList(new Photo(UUID.randomUUID() ,"5fcfda0597b2c278a90084be"));
		List<Board> boards = Arrays.asList(Board.builder().id(UUID.randomUUID()).name("1").creatorId("1").isPrivate(false).isCreatorHidden(false).photos(photos).timestamp(Timestamp.from(Instant.now())).build(), Board.builder().id(UUID.randomUUID()).name("1").creatorId("1").isPrivate(false).isCreatorHidden(false).photos(photos).timestamp(Timestamp.from(Instant.now())).build(), Board.builder().id(UUID.randomUUID()).name("1").creatorId("1").isPrivate(false).isCreatorHidden(false).photos(photos).timestamp(Timestamp.from(Instant.now())).build());
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
		photoService.save(board.getPhotos());
		return boardRepository.save(board);
	}

	public void delete(Board board) {
		boardRepository.delete(board);
	}

	public Board updateBoard(UUID uuid, Board board) {
		Board storedBoard = findById(uuid);
		if (board.getName() != null) storedBoard.setName(board.getName());
		if (board.getCreatorId() != null) storedBoard.setCreatorId(board.getCreatorId());
		if (board.getPhotos() != null) storedBoard.setPhotos(photoService.save(board.getPhotos()));
		if (board.getIsPrivate() != null) storedBoard.setIsPrivate(board.getIsPrivate());
		if (board.getIsCreatorHidden() != null) storedBoard.setIsCreatorHidden(board.getIsCreatorHidden());
		return storedBoard;
	}
}
