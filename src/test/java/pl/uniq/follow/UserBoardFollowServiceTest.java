package pl.uniq.follow;

import org.junit.jupiter.api.Test;
import pl.uniq.auth.user.Role;
import pl.uniq.auth.user.User;
import pl.uniq.board.models.Board;
import pl.uniq.board.service.BoardService;
import pl.uniq.follow.model.UserBoardFollow;
import pl.uniq.follow.repository.UserBoardFollowRepository;
import pl.uniq.follow.service.UserBoardFollowService;
import pl.uniq.notifications.service.NotificationService;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;

class UserBoardFollowServiceTest {

	@Test
	void test1() {
		UserBoardFollowRepository userBoardFollowRepository = mock(UserBoardFollowRepository.class);
		BoardService boardService = mock(BoardService.class);
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
		doReturn(board).when(boardService).findBoardByBoardIdAndIsPrivate(boardId);

		UserBoardFollowService userBoardFollowService = new UserBoardFollowService(boardService, userBoardFollowRepository, mock(NotificationService.class));
		userBoardFollowService.follow(boardId, user);
		when(userBoardFollowRepository.save(any(UserBoardFollow.class))).thenAnswer(i -> i.getArguments()[0]);
		verify(userBoardFollowRepository, times(1)).save(any(UserBoardFollow.class));
	}

	@Test
	void test2() {
		UserBoardFollowRepository userBoardFollowRepository = mock(UserBoardFollowRepository.class);
		BoardService boardService = mock(BoardService.class);
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
		UserBoardFollow userBoardFollow = UserBoardFollow.builder().
				follow_id(UUID.randomUUID()).
				from(user).
				to(board).build();
		doReturn(board).when(boardService).findBoardByBoardIdAndIsPrivate(boardId);
		doReturn(Optional.of(userBoardFollow)).when(userBoardFollowRepository).findUserBoardFollowByFromAndTo(user, board);

		UserBoardFollowService userBoardFollowService = new UserBoardFollowService(boardService, userBoardFollowRepository, mock(NotificationService.class));
		userBoardFollowService.unfollow(boardId, user);
		verify(userBoardFollowRepository, times(1)).delete(userBoardFollow);
	}
}
