package pl.uniq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uniq.exceptions.ResourceNotFoundException;
import pl.uniq.models.Board;
import pl.uniq.models.BoardResults;
import pl.uniq.repository.BoardRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    // Dla endpointu testowego póki nie ma bazy danych zeby mozna było wyswietlac widoczki - Maciej
    public BoardResults test(String id){
        List<Board> boards = new ArrayList<>();
        List<String> photos = new ArrayList<>();
        photos.add("https://via.placeholder.com/150");
        photos.add("https://via.placeholder.com/150");
        photos.add("https://via.placeholder.com/150");
        photos.add("https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg");
        photos.add("https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg");
        photos.add("https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg");
        photos.add("https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg");
        photos.add("https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg");
        photos.add("https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg");
        photos.add("https://upload.wikimedia.org/wikipedia/commons/b/b1/Eyes_of_gorilla.jpg");


        boards.add(new Board("13", "pierwszy", "123", photos, false, false));
        boards.add(new Board("14", "drugi", "123", photos, false, false));
        boards.add(new Board("15", "trzeci", "123", photos, false, false));
        return new BoardResults(boards);
    }

    public List<Board> findAll(){
        return boardRepository.findAll();
    }
    public List<Board> findAll(String id){
        return boardRepository.findAllByCreatorId(id);
    }

    public Board findById(String id) throws ResourceNotFoundException {
        return boardRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource with id: " + id + " not found!")
        );
    }

    public Board insert(Board board) {
        return boardRepository.insert(board);
    }

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public void delete(Board board) {
        boardRepository.delete(board);
    }

}
