package sg.edu.nus.mockExcercise.model;

import java.io.Serializable;
import java.util.Random;

public class Book implements Serializable {
    private String id;
    private String title;
    private String author;
    private String cover;

    public Book() {
        this.id = generateID(8);
    }

    public Book(String id, String title, String author, String cover) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.cover = cover;
    }

    public Book(String title, String author, String cover) {
        this.id = generateID(8);
        this.title = title;
        this.author = author;
        this.cover = cover;
    }

    private synchronized String generateID(int numChars) {
        Random r = new Random();
        StringBuilder strBuilder = new StringBuilder();
        while (strBuilder.length() < numChars) {
            strBuilder.append(Integer.toHexString(r.nextInt()));
        }
        return strBuilder.toString().substring(0, numChars);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getField(String field) {
        switch (field) {
            case "title":
                return title;
            case "author":
                return author;
            default:
                return "not found";
        }
    }
}
