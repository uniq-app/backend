package pl.uniq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uniq.model.User;
import pl.uniq.model.Board;
import pl.uniq.exceptions.FollowAlreadyExistsException;
import pl.uniq.exceptions.FollowNotFoundException;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.model.UserBoardFollow;
import pl.uniq.repository.UserBoardFollowRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserBoardFollowService {
	private final BoardService boardService;
	private final UserBoardFollowRepository userBoardFollowRepository;
	private final NotificationService notificationService;

	@Autowired
	public UserBoardFollowService(BoardService boardService, UserBoardFollowRepository userBoardFollowRepository, NotificationService notificationService) {
		this.boardService = boardService;
		this.userBoardFollowRepository = userBoardFollowRepository;
		this.notificationService = notificationService;
	}

	public void follow(UUID uuid, User currentUser) throws ResourceNotFoundException, FollowAlreadyExistsException {
		Board storedBoard = boardService.findBoardByBoardIdAndIsPrivate(uuid);
		Optional<UserBoardFollow> followOptional = userBoardFollowRepository.findUserBoardFollowByFromAndTo(currentUser, storedBoard);
		followOptional.ifPresent(follow -> {
			throw new FollowAlreadyExistsException("User " + follow.getFrom().getUserId() + " already follows board \"" + follow.getTo().getBoardId() + "\"");
		});
		UserBoardFollow userBoardFollow = UserBoardFollow.builder().from(currentUser).to(storedBoard).build();
		userBoardFollowRepository.save(userBoardFollow);
		notificationService.sendNotification(storedBoard.getUser().getFCMToken(), "Board has been followed", "Your board has been followed by " + currentUser.getUsername());
	}

	public void unfollow(UUID uuid, User currentUser) throws ResourceNotFoundException, FollowNotFoundException {
		Board storedBoard = boardService.findBoardByBoardIdAndIsPrivate(uuid);
		Optional<UserBoardFollow> followOptional = userBoardFollowRepository.findUserBoardFollowByFromAndTo(currentUser, storedBoard);
		followOptional.orElseThrow(() -> {
			throw new FollowNotFoundException("User " + currentUser.getUsername() + " is not following board \"" + storedBoard.getName() + "\"");
		});
		UserBoardFollow userBoardFollow = followOptional.get();
		userBoardFollowRepository.delete(userBoardFollow);
	}
}
