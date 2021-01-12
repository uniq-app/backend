package pl.uniq.photo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uniq.board.service.BoardService;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.photo.dto.PhotoDto;
import pl.uniq.photo.models.Photo;
import pl.uniq.photo.repository.PhotoRepository;

import java.util.*;

@Service
public class PhotoService {

	private final PhotoRepository photoRepository;
	private final BoardService boardService;

	@Autowired
	public PhotoService(PhotoRepository photoRepository, BoardService boardService) {
		this.photoRepository = photoRepository;
		this.boardService = boardService;
	}


	public List<Photo> findAllByBoard(UUID boardId) throws ResourceNotFoundException {
		return photoRepository.findAllByBoardId(boardId);
	}

	public List<Photo> save(List<PhotoDto> photoDtos, UUID boardId) {
		int newPhotos = 0;
		List<Photo> photos = new LinkedList<>();
		for (PhotoDto photoDto : photoDtos) {
			Optional<Photo> photoOptional = photoRepository.findPhotoByPhotoId(photoDto.getPhotoId());
			Photo photo;
			if (photoOptional.isPresent()) {
				photo = photoOptional.get();
				if (photoDto.getOrder() != null) {
					photo.setOrder(photoDto.getOrder());
				}
				if (photoDto.getExtraData() != null) {
					photo.setExtraData(photoDto.getExtraData());
				}
			} else {
				photoDto.setBoardId(boardId);
				int order = photoRepository.countPhotoByBoardId(boardId);
				photo = Photo.create(photoDto);
				photo.setOrder(order+1);
				newPhotos++;
			}
			photoRepository.save(photo);
			photos.add(photo);
		}
		if (newPhotos > 0) {
			boardService.notifyFollowersAboutNewPhotos(boardId, newPhotos);
		}
		return photos;
	}

	public void delete(List<PhotoDto> photoDtos) {
		for (PhotoDto photoDto : photoDtos) {
			Optional<Photo> photoOptional = photoRepository.findPhotoByPhotoId(photoDto.getPhotoId());
			photoOptional.ifPresent(photoRepository::delete);
		}
	}
}
