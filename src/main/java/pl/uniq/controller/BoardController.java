package pl.uniq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.uniq.models.Board;
import pl.uniq.models.BoardResults;
import pl.uniq.service.BoardService;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // Testowy endpoint pozniej usunac - Maciej
    @GetMapping(value="/test")
    public BoardResults test(@RequestParam(required = false) String creator){
        return boardService.test(creator);
    }

    @GetMapping
    public List<Board> getAll(@RequestParam(required = false) String creator){
        return (creator == null)? boardService.findAll() : boardService.findAll(creator);
    }

    @GetMapping(value = "/{id}")
    public Board getBoardById(@PathVariable(value = "id") String id) {
        return boardService.findById(id);
    }


    @PostMapping
    public Board createBoard(@RequestBody Board board){
        return boardService.insert(board);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Board> updateBoard(@PathVariable(value = "id") String id,
                                             @RequestBody Board board){
        Board storedBoard = boardService.findById(id);
        if(board.getName() != null) storedBoard.setName(board.getName());
        if(board.getCreatorId() != null) storedBoard.setCreatorId(board.getCreatorId());
        if(board.getPhotos() != null) storedBoard.setPhotos(board.getPhotos());
        if(board.getIsPrivate() != null) storedBoard.setIsPrivate(board.getIsPrivate());
        if(board.getIsCreatorHidden() != null) storedBoard.setIsCreatorHidden(board.getIsCreatorHidden());
        return ResponseEntity.ok(this.boardService.save(storedBoard));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable(value = "id") String id){
        Board storedBoard = boardService.findById(id);

        boardService.delete(storedBoard);
        return ResponseEntity.ok().body("Removed");
    }
}
