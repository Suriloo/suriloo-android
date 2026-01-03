package com.suriloo.android.home;

public class Content {
    private String title;
    private String imageUrl;

    public Content(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
}