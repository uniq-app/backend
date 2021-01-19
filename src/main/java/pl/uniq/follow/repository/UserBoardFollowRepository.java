package pl.uniq.follow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uniq.user.User;
import pl.uniq.board.models.Board;
import pl.uniq.follow.model.UserBoardFollow;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserBoardFollowRepository extends JpaRepository<UserBoardFollow, UUID> {

	Optional<UserBoardFollow> findUserBoardFollowByFromAndTo(User from, Board to);
}
