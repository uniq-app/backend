package pl.uniq.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uniq.board.models.Follow;

import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<Follow, UUID> {}
