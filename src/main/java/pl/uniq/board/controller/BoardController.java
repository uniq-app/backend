package pl.uniq.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.uniq.board.models.Board;
import pl.uniq.board.models.BoardResults;
import pl.uniq.board.service.BoardService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    // Testowy endpoint pozniej usunac - Maciej
    @GetMapping(value = "/test")
    public BoardResults test() {
        return boardService.test();
    }

    @GetMapping
    public List<Board> getAll(@RequestParam(required = false) String creator) {
        return (creator == null) ? boardService.findAll() : boardService.findAll(creator);
    }

    @GetMapping(value = "/{uuid}")
    public Board getBoardById(@PathVariable(value = "uuid") UUID uuid) {
        return boardService.findById(uuid);
    }


    @PutMapping(value = "/{uuid}")
    public ResponseEntity<Board> updateBoard(@PathVariable(value = "uuid") UUID uuid,
                                             @RequestBody Board board) {
        Board storedBoard = boardService.findById(uuid);
        if (board.getName() != null) storedBoard.setName(board.getName());
        if (board.getCreatorId() != null) storedBoard.setCreatorId(board.getCreatorId());
        if (board.getPhotos() != null) storedBoard.setPhotos(board.getPhotos());
        if (board.getIsPrivate() != null) storedBoard.setIsPrivate(board.getIsPrivate());
        if (board.getIsCreatorHidden() != null) storedBoard.setIsCreatorHidden(board.getIsCreatorHidden());
        return ResponseEntity.ok(this.boardService.save(storedBoard));
    }

    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<String> deleteBoard(@PathVariable(value = "uuid") UUID uuid) {
        Board storedBoard = boardService.findById(uuid);

        boardService.delete(storedBoard);
        return ResponseEntity.ok().body("Removed");
    }
}
