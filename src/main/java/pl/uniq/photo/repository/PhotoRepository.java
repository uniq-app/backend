package pl.uniq.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uniq.photo.models.Photo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, UUID> {

	List<Photo> findAllByBoardId(UUID boardID);

	Integer countPhotoByBoardId(UUID boardID);

	Optional<Photo> findPhotoByPhotoId(UUID photoId);

	Optional<Photo> findByValueAndBoardId(String value, UUID boardID);
}
