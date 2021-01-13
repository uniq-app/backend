package pl.uniq.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.uniq.auth.security.authorizartion.AuthorizationService;
import pl.uniq.board.dto.BoardDto;
import pl.uniq.board.models.Board;
import pl.uniq.board.service.BoardService;
import pl.uniq.exceptions.AuthorizationException;
import pl.uniq.exceptions.FollowAlreadyExistsException;
import pl.uniq.exceptions.FollowNotFoundException;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.follow.service.UserBoardFollowService;
import pl.uniq.photo.dto.PhotoDto;
import pl.uniq.photo.models.Photo;
import pl.uniq.photo.service.PhotoService;
import pl.uniq.utils.Message;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/boards")
public class BoardController {

	private final BoardService boardService;
	private final PhotoService photoService;
	private final UserBoardFollowService userBoardFollowService;
	private final AuthorizationService authorizationService;

	@Autowired
	public BoardController(BoardService boardService, PhotoService photoService, AuthorizationService authorizationService, UserBoardFollowService userBoardFollowService) {
		this.boardService = boardService;
		this.photoService = photoService;
		this.userBoardFollowService = userBoardFollowService;
		this.authorizationService = authorizationService;
	}

	@GetMapping
	public Page<BoardDto> getAll(Pageable page) {
		return boardService.getAllBoards(page, authorizationService.getCurrentUser());
	}

	@PostMapping
	public ResponseEntity<BoardDto> saveBoard(@RequestBody Board board) {
		return new ResponseEntity<>(boardService.saveBoard(board, authorizationService.getCurrentUser()), HttpStatus.OK);
	}

	@GetMapping(value = "/followed")
	public Page<BoardDto> getAllFollowed(Pageable page) {
		return boardService.getAllFollowed(page, authorizationService.getCurrentUser());
	}

	@GetMapping(value = "/search")
	public Page<BoardDto> getAllByKey(Pageable page, @RequestParam String q) {
		try {
			return boardService.getAllSearched(page, q);
		} catch (ResourceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
		}
	}

	@GetMapping(value = "/{uuid}")
	public ResponseEntity<BoardDto> getBoardById(@PathVariable(value = "uuid") UUID uuid) {
		try {
			return new ResponseEntity<>(boardService.getBoardById(uuid, authorizationService.getCurrentUser()), HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (AuthorizationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	@PutMapping(value = "/{uuid}")
	public ResponseEntity<BoardDto> updateBoard(@PathVariable(value = "uuid") UUID uuid, @RequestBody Board board) {
		try {
			return new ResponseEntity<>(boardService.updateBoard(uuid, board, authorizationService.getCurrentUser()), HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@DeleteMapping(value = "/{uuid}")
	public ResponseEntity<Message> deleteBoard(@PathVariable(value = "uuid") UUID uuid) {
		try {
			boardService.deleteBoard(uuid, authorizationService.getCurrentUser());
		} catch (ResourceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<>(new Message("Removed"), HttpStatus.OK);
	}

	@GetMapping(value = "/{uuid}/photos")
	public ResponseEntity<List<PhotoDto>> getPhotosByBoard(@PathVariable(value = "uuid") UUID uuid) {
		return new ResponseEntity<>(photoService.findAllByBoard(uuid), HttpStatus.OK);
	}

	@PutMapping(value = "/{uuid}/photos")
	public ResponseEntity<List<PhotoDto>> savePhotosByBoard(@PathVariable(value = "uuid") UUID uuid, @RequestBody List<PhotoDto> photos) {
		return new ResponseEntity<>(photoService.save(photos, uuid), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{uuid}/photos")
	public ResponseEntity<Message> deletePhotosByBoard(@PathVariable(value = "uuid") UUID uuid, @RequestBody List<PhotoDto> photos) {
		photoService.delete(photos);
		return new ResponseEntity<>(new Message("Removed"), HttpStatus.OK);
	}

	@PostMapping(value = "/{uuid}/follow")
	public ResponseEntity<Message> followBoard(@PathVariable(value = "uuid") UUID uuid) {
		try {
			userBoardFollowService.follow(uuid, authorizationService.getCurrentUser());
		} catch (FollowAlreadyExistsException alreadyExists) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, alreadyExists.getMessage());
		}
		return new ResponseEntity<>(new Message("Followed"), HttpStatus.OK);
	}

	@PostMapping(value = "/{uuid}/unfollow")
	public ResponseEntity<Message> unfollowBoard(@PathVariable(value = "uuid") UUID uuid) {
		try {
			userBoardFollowService.unfollow(uuid, authorizationService.getCurrentUser());
		} catch (FollowNotFoundException notFound) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, notFound.getMessage());
		}
		return new ResponseEntity<>(new Message("Unfollowed"), HttpStatus.OK);
	}
}
