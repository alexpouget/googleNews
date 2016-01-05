package com.alex.googlenewsreader;

/**
 * Created by alex on 27/12/2015.
 */
public class News {
    String title;
    String url;
    String snippet;

    public News() {
    }

    public News(String title, String url, String snippet) {
        this.title = title;
        this.url = url;
        this.snippet = snippet;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
