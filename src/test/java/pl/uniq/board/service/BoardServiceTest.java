package pl.uniq.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import pl.uniq.auth.user.Role;
import pl.uniq.auth.user.User;
import pl.uniq.board.dto.BoardDto;
import pl.uniq.board.models.Board;
import pl.uniq.board.repository.BoardRepository;
import pl.uniq.exceptions.AuthorizationException;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.follow.model.UserBoardFollow;
import pl.uniq.notifications.service.NotificationService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardServiceTest {

	@Test
	void test1() {
		BoardRepository boardRepository = mock(BoardRepository.class);
		User user = User.builder().
				email("test").
				username("test").
				password("test").
				isActive(true).
				roles(Set.of(Role.UNIQ_USER)).build();
		doReturn(List.of(Board.builder().
				boardId(UUID.randomUUID()).
				name("board1").
				user(user).build(), Board.builder().
				boardId(UUID.randomUUID()).
				name("board2").
				user(user).build())).when(boardRepository).findAllByUserOrderByTimestampAsc(user);

		BoardService boardService = new BoardService(boardRepository, mock(NotificationService.class));
		List<BoardDto> result = boardService.getAllBoards(Pageable.unpaged(), user).
				get().
				collect(Collectors.toUnmodifiableList());
		assertEquals(2, result.size());
		assertEquals("test", result.get(0).getCreatorName());
		assertEquals("test", result.get(1).getCreatorName());
	}

	@Test
	void test2() {
		BoardRepository boardRepository = mock(BoardRepository.class);
		UUID userId = UUID.randomUUID();
		User user = User.builder().
				userId(userId).
				email("test").
				username("test").
				password("test").
				isActive(true).
				roles(Set.of(Role.UNIQ_USER)).build();
		Board board1 = Board.builder().
				boardId(UUID.randomUUID()).
				name("board1").
				user(user).build();
		Board board2 = Board.builder().
				boardId(UUID.randomUUID()).
				name("board2").
				user(user).build();
		Board board3 = Board.builder().
				boardId(UUID.randomUUID()).
				name("board3").
				user(user).build();
		List<Board> boards = List.of(board1, board2, board3);
		List<UserBoardFollow> follows = List.of(UserBoardFollow.builder().
				follow_id(UUID.randomUUID()).
				from(user).
				to(board1).build(), UserBoardFollow.builder().
				follow_id(UUID.randomUUID()).
				from(user).
				to(board2).build(), UserBoardFollow.builder().
				follow_id(UUID.randomUUID()).
				from(user).
				to(board3).build());
		user.setFollowing(follows);
		doReturn(boards).when(boardRepository).findPublicBoardsByFollower(userId);

		BoardService boardService = new BoardService(boardRepository, mock(NotificationService.class));
		List<BoardDto> result = boardService.getAllFollowed(Pageable.unpaged(), user).
				get().
				collect(Collectors.toUnmodifiableList());
		assertEquals(result.size(), boards.size());
		assertEquals(user.getUsername(), result.get(0).getCreatorName());
		assertEquals(user.getUsername(), result.get(1).getCreatorName());
		assertEquals(user.getUsername(), result.get(2).getCreatorName());
	}

	@Test
	void test3() {
		BoardRepository boardRepository = mock(BoardRepository.class);
		UUID userId = UUID.randomUUID();
		User user = User.builder().
				userId(userId).
				email("test").
				username("test").
				password("test").
				isActive(true).
				roles(Set.of(Role.UNIQ_USER)).build();
		Board board1 = Board.builder().
				boardId(UUID.randomUUID()).
				name("foxes").
				description("Board with a lot of foxes photos.").
				user(user).build();
		Board board2 = Board.builder().
				boardId(UUID.randomUUID()).
				name("cats").
				description("Board with a lot of cats photos.").
				user(user).build();
		Board board3 = Board.builder().
				boardId(UUID.randomUUID()).
				name("dogs").
				description("Board with a lot of dogs photos.").
				user(user).build();
		List<Board> boards = List.of(board1, board2, board3);
		doReturn(boards).when(boardRepository).findAllSearched("%board%", userId);

		BoardService boardService = new BoardService(boardRepository, mock(NotificationService.class));
		List<BoardDto> result = boardService.getAllSearched(Pageable.unpaged(), "board", user).
				get().
				collect(Collectors.toUnmodifiableList());
		assertEquals(3, result.size());
	}

	@Test
	void test4() {
		BoardRepository boardRepository = mock(BoardRepository.class);
		User boardOwner = User.builder().
				userId(UUID.randomUUID()).
				email("test").
				username("test").
				password("test").
				isActive(true).
				roles(Set.of(Role.UNIQ_USER)).build();
		User randomUser = User.builder().
				userId(UUID.randomUUID()).
				email("test").
				username("test").
				password("test").
				isActive(true).
				roles(Set.of(Role.UNIQ_USER)).build();
		UUID boardId = UUID.randomUUID();
		Board board = Board.builder().
				boardId(boardId).
				name("board1").
				isPrivate(true).
				user(boardOwner).build();
		doReturn(Optional.of(board)).when(boardRepository).findBoardByBoardId(boardId);

		BoardService boardService = new BoardService(boardRepository, mock(NotificationService.class));
		BoardDto result1 = boardService.getBoardById(boardId, boardOwner);
		assertEquals(result1.getCreatorName(), boardOwner.getUsername());
		assertEquals(board.getUser().getUserId(), boardOwner.getUserId());
		assertThrows(AuthorizationException.class, () -> boardService.getBoardById(boardId, randomUser), "You do not have access to this resource");
	}

	@Test
	void test5() {
		BoardRepository boardRepository = mock(BoardRepository.class);
		User user = User.builder().
				userId(UUID.randomUUID()).
				email("test").
				username("test").
				password("test").
				isActive(true).
				roles(Set.of(Role.UNIQ_USER)).build();
		UUID boardId = UUID.randomUUID();
		Board board = Board.builder().
				boardId(boardId).
				name("board1").
				isPrivate(true).
				user(user).build();
		when(boardRepository.save(any(Board.class))).thenAnswer(i -> i.getArguments()[0]);

		BoardService boardService = new BoardService(boardRepository, mock(NotificationService.class));
		boardService.saveBoard(board, user);
		verify(boardRepository, times(1)).save(any(Board.class));
	}

	@Test
	void test6() {
		BoardRepository boardRepository = mock(BoardRepository.class);
		UUID boardId = UUID.randomUUID();
		User user = User.builder().
				userId(UUID.randomUUID()).
				email("test").
				username("test").
				password("test").
				isActive(true).
				roles(Set.of(Role.UNIQ_USER)).build();
		Board board = Board.builder().
				boardId(boardId).
				name("board1").
				isPrivate(true).
				user(user).build();
		doReturn(Optional.of(board)).when(boardRepository).findBoardByBoardIdAndUser(boardId, user);

		BoardService boardService = new BoardService(boardRepository, mock(NotificationService.class));
		boardService.deleteBoard(boardId, user);
		verify(boardRepository, times(1)).delete(board);
		assertThrows(ResourceNotFoundException.class, () -> boardService.getBoardById(boardId, user), "Board with given id does not exist");
	}

	@Test
	void test7() {
		BoardRepository boardRepository = mock(BoardRepository.class);
		UUID boardId = UUID.randomUUID();
		User user = User.builder().
				userId(UUID.randomUUID()).
				email("test").
				username("test").
				password("test").
				isActive(true).
				roles(Set.of(Role.UNIQ_USER)).build();
		Board board1 = Board.builder().
				boardId(boardId).
				name("board1").
				isPrivate(false).
				user(user).build();
		doReturn(Optional.of(board1)).when(boardRepository).findBoardByBoardIdAndIsPrivate(boardId, false);

		BoardService boardService = new BoardService(boardRepository, mock(NotificationService.class));
		Board result1 = boardService.findBoardByBoardIdAndIsPrivate(boardId);
		assertEquals(result1, board1);
	}
}
