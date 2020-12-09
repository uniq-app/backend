package pl.uniq.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.uniq.board.models.Board;
import pl.uniq.board.service.BoardService;
import pl.uniq.photo.models.Photo;
import pl.uniq.photo.service.PhotoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/boards")
public class BoardController {

	private final BoardService boardService;
	private final PhotoService photoService;

	@Autowired
	public BoardController(BoardService boardService, PhotoService photoService) {
		this.boardService = boardService;
		this.photoService = photoService;
	}

	@GetMapping
	public Page<Board> getAll(@RequestParam(required = false) String creator, Pageable page) {
		return boardService.findAll(page);
	}

	@GetMapping(value = "/{uuid}")
	public Board getBoardById(@PathVariable(value = "uuid") UUID uuid) {
		return boardService.findById(uuid);
	}

	@PostMapping(value = "/")
	public ResponseEntity<Board> saveBoard(@RequestBody Board board) {
		return new ResponseEntity<>(boardService.save(board), HttpStatus.OK);
	}

	@PutMapping(value = "/{uuid}")
	public ResponseEntity<Board> updateBoard(@PathVariable(value = "uuid") UUID uuid, @RequestBody Board board) {
		Board storedBoard = boardService.updateBoard(uuid, board);
		return new ResponseEntity<>(storedBoard, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{uuid}")
	public ResponseEntity<String> deleteBoard(@PathVariable(value = "uuid") UUID uuid) {
		Board storedBoard = boardService.findById(uuid);

		boardService.delete(storedBoard);
		return ResponseEntity.ok().body("Removed");
	}

	@GetMapping(value = "/{uuid}/photos")
	public ResponseEntity<List<Photo>> getPhotosByBoard(@PathVariable(value = "uuid") UUID uuid) {
		return new ResponseEntity<>(photoService.findAllByBoard(uuid), HttpStatus.OK);
	}

	@PostMapping(value = "/{uuid}/photos")
	public ResponseEntity<List<Photo>> savePhotosByBoard(@PathVariable(value = "uuid") UUID uuid, @RequestBody List<Photo> photos) {
		return new ResponseEntity<>(photoService.save(photos, uuid), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{uuid}/photos")
	public ResponseEntity<String> deletePhotosByBoard(@PathVariable(value = "uuid") UUID uuid, @RequestBody List<Photo> photos) {
		photoService.delete(photos, uuid);
		return ResponseEntity.ok().body("Removed");
	}
}
