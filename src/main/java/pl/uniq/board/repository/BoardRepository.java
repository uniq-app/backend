package pl.uniq.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uniq.board.models.Board;

import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {

	Page<Board> findAllByUserId(Pageable pageable, UUID userId);
	Board findBoardByBoardIdAndUserId(UUID boardId, UUID userId);
}
