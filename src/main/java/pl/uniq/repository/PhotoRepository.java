package pl.uniq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uniq.model.Board;
import pl.uniq.model.Photo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, UUID> {

	List<Photo> findAllByBoardOrderByOrderAsc(Board board);

	Integer countPhotoByBoard(Board board);

	Optional<Photo> findPhotoByPhotoId(UUID photoId);
}
