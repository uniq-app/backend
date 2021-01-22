package pl.uniq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uniq.model.User;
import pl.uniq.dto.BoardDto;
import pl.uniq.model.Board;
import pl.uniq.repository.BoardRepository;
import pl.uniq.exceptions.AuthorizationException;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.model.UserBoardFollow;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BoardService {

	private final BoardRepository boardRepository;
	private final NotificationService notificationService;

	@Autowired
	public BoardService(BoardRepository boardRepository, NotificationService notificationService) {
		this.boardRepository = boardRepository;
		this.notificationService = notificationService;
	}

	public Page<BoardDto> getAllBoards(Pageable page, User user) {
		List<BoardDto> boards = boardRepository.findAllByUserOrderByTimestampAsc(user).stream().map(BoardDto::create).collect(Collectors.toList());
		return new PageImpl<>(boards, page, boards.size());
	}

	public Page<BoardDto> getAllFollowed(Pageable page, User user) {
		List<BoardDto> boards = boardRepository.findPublicBoardsByFollower(user.getUserId()).stream().map(BoardDto::create).collect(Collectors.toList());
		return new PageImpl<>(boards, page, boards.size());
	}

	public Page<BoardDto> getAllSearched(Pageable page, String query, User user) {
		if (!query.isBlank()) {
			query = "%" + query.toLowerCase() + "%";
			List<BoardDto> boards = boardRepository.findAllSearched(query, user.getUserId()).stream().map(BoardDto::create).collect(Collectors.toList());
			if (boards.size() != 0) {
				return new PageImpl<>(boards, page, boards.size());
			} else {
				throw new ResourceNotFoundException("There is no matching boards for " + query);
			}
		} else {
			return new PageImpl<>(List.of(), page, 0);
		}
	}

	public BoardDto getBoardById(UUID uuid, User user) {
		Optional<Board> boardOptional = boardRepository.findBoardByBoardId(uuid);
		if (boardOptional.isPresent()) {
			Board board = boardOptional.get();
			if (!board.getIsPrivate()) {
				return BoardDto.create(board);
			} else {
				if (board.getUser().getUserId().equals(user.getUserId())) {
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
		board.setUser(user);
		//		board.setUserId(user.getUserId());
		boardRepository.save(board);
		return BoardDto.create(board);
	}

	public BoardDto updateBoard(UUID uuid, Board board, User user) {
		Optional<Board> boardOptional = boardRepository.findBoardByBoardIdAndUser(uuid, user);
		if (boardOptional.isPresent()) {
			Board storedBoard = boardOptional.get();
			if (board.getName() != null)
				storedBoard.setName(board.getName());
			if (board.getDescription() != null)
				storedBoard.setDescription(board.getDescription());
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
		Optional<Board> boardOptional = boardRepository.findBoardByBoardIdAndUser(uuid, user);
		if (boardOptional.isPresent()) {
			boardRepository.delete(boardOptional.get());
		} else {
			throw new ResourceNotFoundException("Board with given id does not exist");
		}
	}

	public Board findBoardByBoardId(UUID uuid) {
		Optional<Board> boardOptional = boardRepository.findBoardByBoardId(uuid);
		boardOptional.orElseThrow(() -> {
			throw new ResourceNotFoundException("Board with id: " + uuid.toString() + " not found.");
		});
		return boardOptional.get();
	}

	public Board findBoardByBoardIdAndIsPrivate(UUID uuid) {
		Optional<Board> boardOptional = boardRepository.findBoardByBoardIdAndIsPrivate(uuid, false);
		boardOptional.orElseThrow(() -> {
			throw new ResourceNotFoundException("Board with id: " + uuid.toString() + " not found.");
		});
		return boardOptional.get();
	}

	public void notifyFollowersAboutNewPhotos(UUID boardId, Integer photoCount) {
		Optional<Board> boardOptional = boardRepository.findBoardByBoardId(boardId);
		if (boardOptional.isPresent()) {
			Board board = boardOptional.get();

			String creator = board.getUser().getUsername();
			String boardName = board.getName();
			String title = String.format("New photos in %s", boardName);
			String body = String.format("%s added new photo to his board", creator);
			if (photoCount > 1) {
				body = String.format("%s added %d new photos to his board", creator, photoCount);
			}

			List<User> followers = board.getFollowers().stream().map(UserBoardFollow::getFrom).collect(Collectors.toList());
			List<String> tokens = followers.stream().map(User::getFCMToken).filter(Objects::nonNull).collect(Collectors.toList());
			notificationService.sendBatchNotification(tokens, title, body);
		}
	}
}
