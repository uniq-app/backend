package pl.uniq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uniq.model.User;
import pl.uniq.model.Board;
import pl.uniq.model.UserBoardFollow;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserBoardFollowRepository extends JpaRepository<UserBoardFollow, UUID> {

	Optional<UserBoardFollow> findUserBoardFollowByFromAndTo(User from, Board to);
}
