package com.mytlogos.enterprisedesktop.model;

/**
 *
 */
public class SearchResponse {
    public final String title;
    public final String coverUrl;
    public final String link;

    public SearchResponse(String title, String coverUrl, String link) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.link = link;
    }
}
