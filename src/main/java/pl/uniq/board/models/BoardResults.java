package pl.uniq.board.models;

import java.util.List;

public class BoardResults {
    int count;
    String next;
    String previous;
    List<Board> results;

    public BoardResults(List<Board> results) {
        this.count = 3;
        this.next = "XDDDAPI/v2/50heh";
        this.previous = "XDDDAPI/v2/prev";
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Board> getResults() {
        return results;
    }

    public void setResults(List<Board> results) {
        this.results = results;
    }
}
