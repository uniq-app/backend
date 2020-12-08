package pl.uniq.photo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uniq.photo.models.Photo;
import pl.uniq.photo.repository.PhotoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {

	private final PhotoRepository photoRepository;

	@Autowired
	public PhotoService(PhotoRepository photoRepository) { this.photoRepository = photoRepository; }

	public List<Photo> save(List<Photo> photos) {
		if (!photos.isEmpty()) {
			for (Photo photo : photos) {
				photoRepository.save(photo);
			}
		}
		return photos;
	}

	public List<Photo> update(List<Photo> photos) {
//		List<Photo> result = new ArrayList<>();
		if (!photos.isEmpty()) {
			for (Photo photo : photos) {
				Optional<Photo> storedPhoto = photoRepository.findById(photo.getId());
				if (storedPhoto.isPresent()) {
					photoRepository.delete(storedPhoto.get());
					photoRepository.save(photo);
				}
			}
		}
		return photos;
	}
}
