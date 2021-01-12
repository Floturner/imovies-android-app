package floturner.dev.corp.imovie.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Review implements Serializable {

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("content")
    @Expose
    private String content;

    public Review() {

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
