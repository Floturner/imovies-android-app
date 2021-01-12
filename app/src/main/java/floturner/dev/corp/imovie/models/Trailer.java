package floturner.dev.corp.imovie.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Trailer implements Serializable {
    @SerializedName("key")
    @Expose
    private String key;

    public Trailer() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
