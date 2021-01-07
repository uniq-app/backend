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
import pl.uniq.follow.repository.UserBoardFollowRepository;
import pl.uniq.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BoardService {

	private final BoardRepository boardRepository;
	private final UserBoardFollowRepository userBoardFollowRepository;

	@Autowired
	public BoardService(BoardRepository boardRepository, UserBoardFollowRepository userBoardFollowRepository) {
		this.boardRepository = boardRepository;
		this.userBoardFollowRepository = userBoardFollowRepository;
	}

	public Page<BoardDto> findAll(Pageable page, User user) {
		List<BoardDto> boards = boardRepository.findAllByUserId(user.getUserId()).stream().map(BoardDto::create).collect(Collectors.toList());
		return new PageImpl<>(boards, page, boards.size());
	}

	public Page<BoardDto> findAllFollowed(Pageable page, User user) {
		List<BoardDto> boards = boardRepository.findPublicBoardsByFollower(user.getUserId()).stream().map(BoardDto::create).collect(Collectors.toList());
		return new PageImpl<>(boards, page, boards.size());
	}

	public BoardDto findBoardById(UUID uuid, User user) throws ResourceNotFoundException {
		return BoardDto.create(boardRepository.findBoardByBoardIdAndUserId(uuid, user.getUserId()));
	}

	public BoardDto save(Board board, User user) {
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

	public void delete(UUID uuid, User user) {
		Board storedBoard = boardRepository.findBoardByBoardIdAndUserId(uuid, user.getUserId());
		boardRepository.delete(storedBoard);
	}
}
