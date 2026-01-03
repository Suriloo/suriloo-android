package com.suriloo.android.model;

import com.google.gson.annotations.SerializedName;

public class PopularChoiceResponse {

    // @SerializedName ensures Gson maps the JSON keys exactly to these variables
    @SerializedName("title")
    private String title;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("contentUrl")
    private String contentUrl;

    // Empty Constructor (Required by Gson)
    public PopularChoiceResponse() {
    }

    // Full Constructor
    public PopularChoiceResponse(String title, String imageUrl, String contentUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.contentUrl = contentUrl;
    }

    // --- Getters ---
    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    // --- Setters ---
    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}