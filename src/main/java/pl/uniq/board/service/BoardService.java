package pl.uniq.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uniq.auth.user.User;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.board.models.Board;
import pl.uniq.board.repository.BoardRepository;
import pl.uniq.photo.models.Photo;
import pl.uniq.photo.repository.PhotoRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class BoardService {

	private final BoardRepository boardRepository;
	private final PhotoRepository photoRepository;

	@Autowired
	public BoardService(BoardRepository boardRepository, PhotoRepository photoRepository) {
		this.boardRepository = boardRepository;
		this.photoRepository = photoRepository;
	}

	public Page<Board> findAll(Pageable page, UUID userId) {
		return boardRepository.findAllByUserId(page, userId);
	}

	public Board findById(UUID uuid, User user) throws ResourceNotFoundException {
		return boardRepository.findBoardByBoardIdAndUserId(uuid, user.getUserId());
	}

	public Board save(Board board, User user) {
		board.setUserId(user.getUserId());
		boardRepository.save(board);
		return board;
	}

	public Board updateBoard(UUID uuid, Board board, User user) {
		Board storedBoard = findById(uuid, user);
		if (board.getName() != null)
			storedBoard.setName(board.getName());
		if (board.getDescription() != null)
			storedBoard.setDescription(board.getDescription());
		if (board.getUserId() != null)
			storedBoard.setUserId(board.getUserId());
		if (board.getIsPrivate() != null)
			storedBoard.setIsPrivate(board.getIsPrivate());
		if (board.getExtraData() != null)
			storedBoard.setExtraData(board.getExtraData());
		if (board.getCover() != null)
			storedBoard.setCover(board.getCover());
		boardRepository.save(storedBoard);
		return storedBoard;
	}

	public void delete(Board board) {
		boardRepository.delete(board);
	}
}
