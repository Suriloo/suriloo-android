package com.suriloo.android.home;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Content implements Serializable {
    @SerializedName("name")
    private String title;

    @SerializedName("cover")
    private String imageUrl;

    @SerializedName("author")
    private String author;

    @SerializedName("src")
    private String streamUrl; // The URL of the audio stream

    // This constructor is needed for the ExploreFragment's placeholder data
    public Content(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public String getAuthor() { return author; }
    public String getStreamUrl() { return streamUrl; }
}
