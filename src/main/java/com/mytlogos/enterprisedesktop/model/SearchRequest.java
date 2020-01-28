package com.mytlogos.enterprisedesktop.model;

/**
 *
 */
public class SearchRequest {
    public final String title;
    public final int mediumType;

    public SearchRequest(String title, int mediumType) {
        this.title = title;
        this.mediumType = mediumType;
    }
}
