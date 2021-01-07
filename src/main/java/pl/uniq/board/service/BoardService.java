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
import java.util.Optional;
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

	public BoardDto getBoardById(UUID uuid, User user) {
		Optional<Board> boardOptional = boardRepository.findBoardByBoardId(uuid);
		if (boardOptional.isPresent()) {
			Board board = boardOptional.get();
			if (!board.getIsPrivate()) {
				return BoardDto.create(board);
			} else {
				if (board.getUserId().equals(user.getUserId())) {
					return BoardDto.create(board);
				} else {
					throw new AuthorizationException("You do not have access to this resource");
				}
			}
		} else {
			throw new ResourceNotFoundException("Board with given id does not exist");
		}
	}

	public BoardDto saveBoard(Board board, User user) {
		board.setUserId(user.getUserId());
		boardRepository.save(board);
		return BoardDto.create(board);
	}

	public BoardDto updateBoard(UUID uuid, Board board, User user) {
		Optional<Board> boardOptional = boardRepository.findBoardByBoardIdAndUserId(uuid, user.getUserId());
		if (boardOptional.isPresent()) {
			Board storedBoard = boardOptional.get();
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
		} else {
			throw new ResourceNotFoundException("Board with given id does not exist");
		}

	}

	public void deleteBoard(UUID uuid, User user) {
		Optional<Board> boardOptional = boardRepository.findBoardByBoardIdAndUserId(uuid, user.getUserId());
		if (boardOptional.isPresent()) {
			boardRepository.delete(boardOptional.get());
		} else {
			throw new ResourceNotFoundException("Board with given id does not exist");
		}
	}

	public Board findBoardByBoardIdAndIsPrivate(UUID uuid) {
		Optional<Board> boardOptional = boardRepository.findBoardByBoardIdAndIsPrivate(uuid, false);
		boardOptional.orElseThrow(() -> {
			throw new ResourceNotFoundException("Board with id: " + uuid.toString() + " not found.");
		});
		return boardOptional.get();
	}
}
