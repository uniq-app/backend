package pl.uniq.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uniq.board.models.Board;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {

	List<Board> findAllByUserId(UUID userId);

	Board findBoardByBoardIdAndUserId(UUID boardId, UUID userId);

	Board findBoardByBoardId(UUID boardId);
}
