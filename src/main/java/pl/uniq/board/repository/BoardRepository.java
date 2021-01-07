package pl.uniq.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.uniq.board.models.Board;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {

	List<Board> findAllByUserId(UUID userId);

	@Query(value = "SELECT b.* FROM board as b INNER JOIN user_board_followers ubf on b.board_id = ubf.to_board_fk WHERE b.is_private = false AND ubf.from_user_fk = ?1", nativeQuery = true)
	List<Board> findPublicBoardsByFollower(UUID from);

	Board findBoardByBoardIdAndUserId(UUID boardId, UUID userId);

	Board findBoardByBoardId(UUID uuid);

	Optional<Board> findBoardByBoardIdAndIsPrivate(UUID boardId, Boolean isPrivate);
}
