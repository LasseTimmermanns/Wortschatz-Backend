package de.lasse.duden.database.Wordlists;

import com.fasterxml.jackson.annotation.JsonView;
import de.lasse.duden.Views;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Wordlists")
public class Wordlist {

    @Id
    @JsonView(Views.LearnWordlistView.class)
    private String id;

    @Field(name = "owner")
    private String owner;

    @Field(name = "follower")
    private String[] follower;

    @Field(name = "creation_date")
    private long creationDate;

    @Field(name = "name")
    private String name;

    @Field(name = "description")
    private String description;

    @Field(name = "public")
    private boolean isPublic;

    @Field(name = "likes")
    private int likes;

    @Field(name = "words")
    @JsonView(Views.LearnWordlistView.class)
    private String[] words;

    public Wordlist(String owner, String name, boolean isPublic) {
        this.owner = owner;
        this.name = name;
        this.isPublic = isPublic;
        this.follower = new String[0];
        this.creationDate = System.currentTimeMillis();
        this.words = new String[0];
        this.description = null;
        this.likes = 0;
    }

    public Wordlist() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String[] getFollower() {
        return follower;
    }

    public void setFollower(String[] follower) {
        this.follower = follower;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }
}
