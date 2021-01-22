package pl.uniq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uniq.model.Board;
import pl.uniq.service.BoardService;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.dto.PhotoDto;
import pl.uniq.model.Photo;
import pl.uniq.repository.PhotoRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhotoService {

	private final PhotoRepository photoRepository;
	private final BoardService boardService;

	@Autowired
	public PhotoService(PhotoRepository photoRepository, BoardService boardService) {
		this.photoRepository = photoRepository;
		this.boardService = boardService;
	}


	public List<PhotoDto> findAllByBoard(UUID boardId) throws ResourceNotFoundException {
		return photoRepository.findAllByBoardOrderByOrderAsc(boardService.findBoardByBoardId(boardId))
				.stream()
				.map(PhotoDto::new)
				.collect(Collectors.toList());
	}

	public List<PhotoDto> save(List<PhotoDto> photoDtos, UUID boardId) {
		int newPhotos = 0;
		List<PhotoDto> photos = new LinkedList<>();
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
				Board board = boardService.findBoardByBoardId(boardId);
				photoDto.setBoardId(board.getBoardId());
				int order = photoRepository.countPhotoByBoard(board);
				photo = Photo.create(photoDto, board);
				photo.setOrder(order + 1);
				newPhotos++;
			}
			photoRepository.save(photo);
			photos.add(new PhotoDto(photo));
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
