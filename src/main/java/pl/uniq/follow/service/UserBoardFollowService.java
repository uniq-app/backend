package pl.uniq.follow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uniq.auth.user.User;
import pl.uniq.board.models.Board;
import pl.uniq.board.repository.BoardRepository;
import pl.uniq.exceptions.FollowAlreadyExists;
import pl.uniq.exceptions.FollowNotFound;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.follow.model.UserBoardFollow;
import pl.uniq.follow.repository.UserBoardFollowRepository;

import java.util.UUID;

@Service
public class UserBoardFollowService {
	private final BoardRepository boardRepository;
	private final UserBoardFollowRepository userBoardFollowRepository;

	@Autowired
	public UserBoardFollowService(BoardRepository boardRepository, UserBoardFollowRepository userBoardFollowRepository) {
		this.boardRepository = boardRepository;
		this.userBoardFollowRepository = userBoardFollowRepository;
	}

	public void follow(UUID uuid, User currentUser) throws ResourceNotFoundException, FollowAlreadyExists {
		Board storedBoard = boardRepository.findBoardByBoardId(uuid);
		UserBoardFollow existingFollow = userBoardFollowRepository.findUserBoardFollowByFromAndTo(currentUser, storedBoard);
		if (existingFollow != null) {
			throw new FollowAlreadyExists("User " + currentUser.getUsername() + " already follows board \"" + storedBoard.getName() + "\"");
		}
		UserBoardFollow userBoardFollow = UserBoardFollow.builder().from(currentUser).to(storedBoard).build();
		userBoardFollowRepository.save(userBoardFollow);
	}

	public void unfollow(UUID uuid, User currentUser) throws ResourceNotFoundException, FollowNotFound {
		Board storedBoard = boardRepository.findBoardByBoardId(uuid);
		UserBoardFollow userBoardFollow = userBoardFollowRepository.findUserBoardFollowByFromAndTo(currentUser, storedBoard);
		if (userBoardFollow == null) {
			throw new FollowNotFound("User " + currentUser.getUsername() + " is not following board \"" + storedBoard.getName() + "\"");
		}
		userBoardFollowRepository.delete(userBoardFollow);
	}
}
