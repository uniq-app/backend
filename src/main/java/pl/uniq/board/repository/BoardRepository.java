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

	List<Board> findAllByUserIdOrderByTimestampAsc(UUID userId);

	@Query(value = "SELECT * FROM board WHERE (board.name LIKE ?1 OR board.description LIKE ?1) AND board.is_private = false", nativeQuery = true)
	List<Board> findAllSearched(String key);

	@Query(value = "SELECT b.* FROM board as b INNER JOIN user_board_followers ubf on b.board_id = ubf.to_board_fk WHERE b.is_private = false AND ubf.from_user_fk = ?1", nativeQuery = true)
	List<Board> findPublicBoardsByFollower(UUID from);

	Optional<Board> findBoardByBoardIdAndUserId(UUID boardId, UUID userId);

	Optional<Board> findBoardByBoardId(UUID uuid);

	Optional<Board> findBoardByBoardIdAndIsPrivate(UUID boardId, Boolean isPrivate);
}
