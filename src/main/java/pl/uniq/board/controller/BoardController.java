package pl.uniq.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.uniq.auth.security.authorizartion.AuthorizationService;
import pl.uniq.auth.user.User;
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
	private final AuthorizationService authorizationService;

	@Autowired
	public BoardController(BoardService boardService, PhotoService photoService, AuthorizationService authorizationService) {
		this.boardService = boardService;
		this.photoService = photoService;
		this.authorizationService = authorizationService;
	}

	@GetMapping
	public Page<Board> getAll(@RequestParam(required = false) String creator, Pageable page) {
		User user = authorizationService.getCurrentUser();
		return boardService.findAll(page, user.getUserId());
	}

	@GetMapping(value = "/{uuid}")
	public Board getBoardById(@PathVariable(value = "uuid") UUID uuid) {
		return boardService.findById(uuid, authorizationService.getCurrentUser());
	}

	@PostMapping(value = "/")
	public ResponseEntity<Board> saveBoard(@RequestBody Board board) {
		return new ResponseEntity<>(boardService.save(board, authorizationService.getCurrentUser()), HttpStatus.OK);
	}

	@PutMapping(value = "/{uuid}")
	public ResponseEntity<Board> updateBoard(@PathVariable(value = "uuid") UUID uuid, @RequestBody Board board) {
		Board storedBoard = boardService.updateBoard(uuid, board, authorizationService.getCurrentUser());
		return new ResponseEntity<>(storedBoard, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{uuid}")
	public ResponseEntity<String> deleteBoard(@PathVariable(value = "uuid") UUID uuid) {
		Board storedBoard = boardService.findById(uuid, authorizationService.getCurrentUser());

		boardService.delete(storedBoard);
		return ResponseEntity.ok().body("Removed");
	}

	@GetMapping(value = "/{uuid}/photos")
	public ResponseEntity<List<Photo>> getPhotosByBoard(@PathVariable(value = "uuid") UUID uuid) {
		return new ResponseEntity<>(photoService.findAllByBoard(uuid), HttpStatus.OK);
	}

	@PutMapping(value = "/{uuid}/photos")
	public ResponseEntity<List<Photo>> savePhotosByBoard(@PathVariable(value = "uuid") UUID uuid, @RequestBody List<Photo> photos) {
		return new ResponseEntity<>(photoService.save(photos, uuid), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{uuid}/photos")
	public ResponseEntity<String> deletePhotosByBoard(@PathVariable(value = "uuid") UUID uuid, @RequestBody List<Photo> photos) {
		photoService.delete(photos, uuid);
		return ResponseEntity.ok().body("Removed");
	}

	@GetMapping(value = "/{uuid}/cover")
	public ResponseEntity<Photo> getCover(@PathVariable(value = "uuid") UUID uuid) {
		return new ResponseEntity<>(boardService.getCover(uuid, authorizationService.getCurrentUser()), HttpStatus.OK);
	}

	@PutMapping(value = "/{uuid}/cover")
	public ResponseEntity<Board> updateCover(@PathVariable(value = "uuid") UUID uuid, @RequestBody Photo photo) {
		Board storedBoard = boardService.updateCover(uuid, photo, authorizationService.getCurrentUser());
		return new ResponseEntity<>(storedBoard, HttpStatus.OK);
	}
}
