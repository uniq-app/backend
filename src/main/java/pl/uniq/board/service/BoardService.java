package pl.uniq.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uniq.auth.user.User;
import pl.uniq.board.dto.BoardDto;
import pl.uniq.board.models.Board;
import pl.uniq.board.repository.BoardRepository;
import pl.uniq.exceptions.AuthorizationException;
import pl.uniq.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BoardService {

	private final BoardRepository boardRepository;

	@Autowired
	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	public Page<BoardDto> getAllBoards(Pageable page, User user) {
		List<BoardDto> boards = boardRepository.findAllByUserId(user.getUserId()).stream().map(BoardDto::create).collect(Collectors.toList());
		return new PageImpl<>(boards, page, boards.size());
	}

	public Page<BoardDto> getAllFollowed(Pageable page, User user) {
		List<BoardDto> boards = boardRepository.findPublicBoardsByFollower(user.getUserId()).stream().map(BoardDto::create).collect(Collectors.toList());
		return new PageImpl<>(boards, page, boards.size());
	}

	public BoardDto getBoardById(UUID uuid, User user) throws ResourceNotFoundException {
		Board board = boardRepository.findBoardByBoardId(uuid);
		if (!board.getIsPrivate()) {
			return BoardDto.create(board);
		} else {
			if (board.getUserId().equals(user.getUserId())) {
				return BoardDto.create(board);
			} else {
				throw new AuthorizationException("You do not have access to this resource");
			}
		}
	}

	public BoardDto saveBoard(Board board, User user) {
		board.setUserId(user.getUserId());
		boardRepository.save(board);
		return BoardDto.create(board);
	}

	public BoardDto updateBoard(UUID uuid, Board board, User user) {
		Board storedBoard = boardRepository.findBoardByBoardIdAndUserId(uuid, user.getUserId());
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
		return BoardDto.create(storedBoard);
	}

	public void deleteBoard(UUID uuid, User user) {
		Board storedBoard = boardRepository.findBoardByBoardIdAndUserId(uuid, user.getUserId());
		boardRepository.delete(storedBoard);
	}
}
