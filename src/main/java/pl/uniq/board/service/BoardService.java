package pl.uniq.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.board.models.Board;
import pl.uniq.board.models.BoardResults;
import pl.uniq.board.repository.BoardRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardResults test() {
        List<String> photos = Arrays.asList("https://via.placeholder.com/150", "https://via.placeholder.com/150",
                "https://via.placeholder.com/150",
                "https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg");
        List<Board> boards = Arrays.asList(
                Board.builder()
                        .id(UUID.randomUUID())
                        .name("1")
                        .creatorId("1")
                        .isPrivate(false)
                        .isCreatorHidden(false)
                        .photos(photos)
                        .timestamp(Timestamp.from(Instant.now()))
                        .build(),
                Board.builder()
                        .id(UUID.randomUUID())
                        .name("1")
                        .creatorId("1")
                        .isPrivate(false)
                        .isCreatorHidden(false)
                        .photos(photos)
                        .timestamp(Timestamp.from(Instant.now()))
                        .build(),
                Board.builder()
                        .id(UUID.randomUUID())
                        .name("1")
                        .creatorId("1")
                        .isPrivate(false)
                        .isCreatorHidden(false)
                        .photos(photos)
                        .timestamp(Timestamp.from(Instant.now()))
                        .build());
        return new BoardResults(boards);
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public List<Board> findAll(String id) {
        return boardRepository.findAllByCreatorId(id);
    }

    public Board findById(UUID uuid) throws ResourceNotFoundException {
        return boardRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException("Resource with id: " + uuid + " not found!")
        );
    }

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public void delete(Board board) {
        boardRepository.delete(board);
    }

}
