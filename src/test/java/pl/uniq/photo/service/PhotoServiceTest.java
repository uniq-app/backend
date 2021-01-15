package pl.uniq.photo.service;

import org.junit.jupiter.api.Test;
import pl.uniq.board.models.Board;
import pl.uniq.board.service.BoardService;
import pl.uniq.photo.dto.PhotoDto;
import pl.uniq.photo.models.Photo;
import pl.uniq.photo.repository.PhotoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PhotoServiceTest {

	@Test
	void test1() {
		PhotoRepository photoRepository = mock(PhotoRepository.class);
		UUID boardId = UUID.randomUUID();
		Board board = Board.builder().boardId(boardId).build();
		List<Photo> photos = List.of(Photo.builder().
				photoId(UUID.randomUUID()).
				value("photo1").
				board(board).build(), Photo.builder().
				photoId(UUID.randomUUID()).
				value("photo2").
				board(board).build(), Photo.builder().
				photoId(UUID.randomUUID()).
				value("photo3").
				board(board).build());
		doReturn(photos).when(photoRepository).findAllByBoardOrderByOrderAsc(board);

		BoardService boardService = mock(BoardService.class);
		doReturn(board).when(boardService).findBoardByBoardId(boardId);

		PhotoService photoService = new PhotoService(photoRepository,boardService);
		List<PhotoDto> result = photoService.findAllByBoard(boardId);
		assertEquals(photos.size(), result.size());
		assertEquals(boardId, result.get(0).getBoardId());
		assertEquals(boardId, result.get(1).getBoardId());
		assertEquals(boardId, result.get(2).getBoardId());
	}

	@Test
	void test2() {
		PhotoRepository photoRepository = mock(PhotoRepository.class);
		UUID boardId = UUID.randomUUID();
		Board board = Board.builder().boardId(boardId).build();
		List<Photo> photos = List.of(Photo.builder().
				photoId(UUID.randomUUID()).
				value("photo1").
				board(board).build(), Photo.builder().
				photoId(UUID.randomUUID()).
				value("photo2").
				board(board).build(), Photo.builder().
				photoId(UUID.randomUUID()).
				value("photo3").
				board(board).build());
		List<PhotoDto> photoDtos = photos.stream().map(PhotoDto::new).collect(Collectors.toList());
		when(photoRepository.save(any(Photo.class))).thenAnswer(i -> i.getArguments()[0]);

		BoardService boardService = mock(BoardService.class);
		doReturn(board).when(boardService).findBoardByBoardId(boardId);
		PhotoService photoService = new PhotoService(photoRepository,boardService);
		photoService.save(photoDtos, boardId);
		verify(photoRepository, times(3)).save(any(Photo.class));
	}

	@Test
	void test3() {
		PhotoRepository photoRepository = mock(PhotoRepository.class);
		UUID boardId = UUID.randomUUID();
		Board board = Board.builder().boardId(boardId).build();
		List<Photo> photos = List.of(Photo.builder().
				photoId(UUID.randomUUID()).
				value("photo").
				board(board).build(), Photo.builder().
				photoId(UUID.randomUUID()).
				value("photo").
				board(board).build(), Photo.builder().
				photoId(UUID.randomUUID()).
				value("photo").
				board(board).build());
		List<PhotoDto> photoDtos = photos.stream().map(PhotoDto::new).collect(Collectors.toList());
		when(photoRepository.findPhotoByPhotoId(any(UUID.class))).thenReturn(Optional.of(mock(Photo.class)));

		PhotoService photoService = new PhotoService(photoRepository, mock(BoardService.class));
		photoService.delete(photoDtos);
		verify(photoRepository, times(3)).delete(any(Photo.class));
	}
}
