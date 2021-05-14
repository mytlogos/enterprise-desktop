package com.mytlogos.enterprisedesktop.background.api.model;

/**
 * API Model for UpdateMedium.
 * Enterprise Web API 1.0.2.
 */
public class ClientUpdateMedium {
    public int id;
    public String countryOfOrigin;
    public String languageOfOrigin;
    public String author;
    public String title;
    public Integer medium;
    public String artist;
    public String lang;
    public Integer stateOrigin;
    public Integer stateTL;
    public String series;
    public String universe;

    public ClientUpdateMedium(int id, String countryOfOrigin, String languageOfOrigin, String author, String title,
            Integer medium, String artist, String lang, Integer stateOrigin, Integer stateTL, String series,
            String universe) {
        this.id = id;
        this.countryOfOrigin = countryOfOrigin;
        this.languageOfOrigin = languageOfOrigin;
        this.author = author;
        this.title = title;
        this.medium = medium;
        this.artist = artist;
        this.lang = lang;
        this.stateOrigin = stateOrigin;
        this.stateTL = stateTL;
        this.series = series;
        this.universe = universe;
    }
}
