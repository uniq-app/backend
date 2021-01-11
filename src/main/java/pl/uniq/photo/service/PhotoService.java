package pl.uniq.photo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uniq.board.service.BoardService;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.photo.models.Photo;
import pl.uniq.photo.repository.PhotoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PhotoService {

	private final PhotoRepository photoRepository;
	private final BoardService boardService;

	@Autowired
	public PhotoService(PhotoRepository photoRepository, BoardService boardService) {
		this.photoRepository = photoRepository;
		this.boardService = boardService;
	}


	public List<Photo> findAllByBoard(UUID uuid) throws ResourceNotFoundException {
		return photoRepository.findAllByBoard(uuid);
	}

	public List<Photo> save(List<Photo> photos, UUID board_id) {
		for (Photo photo : photos) {
			photo.setBoard(board_id);
			photoRepository.save(photo);
		}
		boardService.notifyFollowers(board_id, photos.size());
		return photos;
	}

	public void delete(List<Photo> photos, UUID uuid) {
		for (Photo photo : photos) {
			Optional<Photo> temp_photo = Optional.ofNullable(photoRepository.findByValueAndBoard(photo.getValue(), uuid));
			temp_photo.ifPresent(photoRepository::delete);
		}
	}
}
