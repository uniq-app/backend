package pl.uniq.photo.service;

import org.junit.jupiter.api.Test;
import pl.uniq.board.models.Board;
import pl.uniq.photo.models.Photo;
import pl.uniq.photo.repository.PhotoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PhotoServiceTest {

	@Test
	void test1() {
		PhotoRepository photoRepository = mock(PhotoRepository.class);
		UUID boardId = UUID.randomUUID();
		List<Photo> photos = List.of(Photo.builder().
				photo_id(UUID.randomUUID()).
				value("photo1").
				board(boardId).build(), Photo.builder().
				photo_id(UUID.randomUUID()).
				value("photo2").
				board(boardId).build(), Photo.builder().
				photo_id(UUID.randomUUID()).
				value("photo3").
				board(boardId).build());
		doReturn(photos).when(photoRepository).findAllByBoard(boardId);

		PhotoService photoService = new PhotoService(photoRepository);
		List<Photo> result = photoService.findAllByBoard(boardId);
		assertEquals(photos.size(), result.size());
		assertEquals(boardId, result.get(0).getBoard());
		assertEquals(boardId, result.get(1).getBoard());
		assertEquals(boardId, result.get(2).getBoard());
	}

	@Test
	void test2() {
		PhotoRepository photoRepository = mock(PhotoRepository.class);
		UUID boardId = UUID.randomUUID();
		List<Photo> photos = List.of(Photo.builder().
				photo_id(UUID.randomUUID()).
				value("photo1").
				board(boardId).build(), Photo.builder().
				photo_id(UUID.randomUUID()).
				value("photo2").
				board(boardId).build(), Photo.builder().
				photo_id(UUID.randomUUID()).
				value("photo3").
				board(boardId).build());
		when(photoRepository.save(any(Photo.class))).thenAnswer(i -> i.getArguments()[0]);

		PhotoService photoService = new PhotoService(photoRepository);
		photoService.save(photos, boardId);
		verify(photoRepository, times(3)).save(any(Photo.class));
	}

	@Test
	void test3() {
		PhotoRepository photoRepository = mock(PhotoRepository.class);
		UUID boardId = UUID.randomUUID();
		List<Photo> photos = List.of(Photo.builder().
				photo_id(UUID.randomUUID()).
				value("photo").
				board(boardId).build(), Photo.builder().
				photo_id(UUID.randomUUID()).
				value("photo").
				board(boardId).build(), Photo.builder().
				photo_id(UUID.randomUUID()).
				value("photo").
				board(boardId).build());
		when(photoRepository.findByValueAndBoard(anyString(), eq(boardId))).thenReturn(mock(Photo.class));

		PhotoService photoService = new PhotoService(photoRepository);
		photoService.delete(photos, boardId);
		verify(photoRepository, times(3)).delete(any(Photo.class));
	}
}
