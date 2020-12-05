package pl.uniq.models;

import com.mongodb.lang.NonNull;
import com.mongodb.lang.NonNullApi;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


@Document(collection = "boards")
public class Board {
    @Id
    private String id;
    private String name;
    private String creatorId;
    private List<String> photos;
    private Boolean isPrivate = false;
    private Boolean isCreatorHidden = false;
    // private Boolean isCloned; ???
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date timestamp = new Date();

    public Board() { }

    public Board(String id,
                 String name,
                 String creatorId,
                 List<String> photos,
                 Boolean isPrivate,
                 Boolean isCreatorHidden) {
        this.id = id;
        this.name = name;
        this.creatorId = creatorId;
        this.photos = photos;
        this.isPrivate = isPrivate;
        this.isCreatorHidden = isCreatorHidden;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Boolean getIsCreatorHidden() {
        return isCreatorHidden;
    }

    public void setIsCreatorHidden(Boolean creatorHidden) {
        isCreatorHidden = creatorHidden;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
