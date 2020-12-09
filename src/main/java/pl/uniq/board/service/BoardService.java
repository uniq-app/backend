package pl.uniq.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.board.models.Board;
import pl.uniq.board.repository.BoardRepository;

import java.util.UUID;

@Service
public class BoardService {

	private final BoardRepository boardRepository;

	@Autowired
	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	public Page<Board> findAll(Pageable page) {
		return boardRepository.findAll(page);
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
		if (board.getName() != null)
			storedBoard.setName(board.getName());
		if (board.getCreatorId() != null)
			storedBoard.setCreatorId(board.getCreatorId());
		if (board.getIsPrivate() != null)
			storedBoard.setIsPrivate(board.getIsPrivate());
		if (board.getIsCreatorHidden() != null)
			storedBoard.setIsCreatorHidden(board.getIsCreatorHidden());
		return storedBoard;
	}

	public void delete(Board board) {
		boardRepository.delete(board);
	}
}
