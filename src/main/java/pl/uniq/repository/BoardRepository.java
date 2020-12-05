package pl.uniq.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.uniq.models.Board;

import java.util.List;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
    List<Board> findAll();
    List<Board> findAllByCreatorId(String id);
}
