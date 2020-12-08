package pl.uniq.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uniq.photo.models.Photo;

import java.util.List;
import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, UUID> {

	List<Photo> findAllByBoard(UUID uuid);
	Photo findByValueAndBoard(String value, UUID uuid);
}
