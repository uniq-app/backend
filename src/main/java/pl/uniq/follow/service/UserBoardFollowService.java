package pl.uniq.follow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uniq.auth.user.User;
import pl.uniq.board.models.Board;
import pl.uniq.board.repository.BoardRepository;
import pl.uniq.exceptions.FollowAlreadyExistsException;
import pl.uniq.exceptions.FollowNotFoundException;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.follow.model.UserBoardFollow;
import pl.uniq.follow.repository.UserBoardFollowRepository;

import java.util.Optional;
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

	public void follow(UUID uuid, User currentUser) throws ResourceNotFoundException, FollowAlreadyExistsException {
		Optional<Board> boardOptional = boardRepository.findBoardByBoardIdAndIsPrivate(uuid, false);
		boardOptional.orElseThrow(() -> {
			throw new ResourceNotFoundException("Board with id: " + uuid.toString() + " not found.");
		});
		Board storedBoard = boardOptional.get();
		Optional<UserBoardFollow> followOptional = userBoardFollowRepository.findUserBoardFollowByFromAndTo(currentUser, storedBoard);
		followOptional.ifPresent(follow -> {
			throw new FollowAlreadyExistsException("User " + follow.getFrom().getUserId() + " already follows board \"" + follow.getTo().getBoardId() + "\"");
		});
		UserBoardFollow userBoardFollow = UserBoardFollow.builder().from(currentUser).to(storedBoard).build();
		userBoardFollowRepository.save(userBoardFollow);
	}

	public void unfollow(UUID uuid, User currentUser) throws ResourceNotFoundException, FollowNotFoundException {
		Optional<Board> boardOptional = boardRepository.findBoardByBoardIdAndIsPrivate(uuid, false);
		boardOptional.orElseThrow(() -> {
			throw new ResourceNotFoundException("Board with id: " + uuid.toString() + " not found.");
		});
		Board storedBoard = boardOptional.get();
		Optional<UserBoardFollow> followOptional = userBoardFollowRepository.findUserBoardFollowByFromAndTo(currentUser, storedBoard);
		followOptional.orElseThrow(() -> {
			throw new FollowNotFoundException("User " + currentUser.getUsername() + " is not following board \"" + storedBoard.getName() + "\"");
		});
		UserBoardFollow userBoardFollow = followOptional.get();
		userBoardFollowRepository.delete(userBoardFollow);
	}
}
