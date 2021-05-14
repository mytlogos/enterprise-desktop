package com.mytlogos.enterprisedesktop.background.api.model;

import java.util.List;

/**
 * API Model for Toc.
 * Enterprise Web API 1.0.2.
 */
public class ClientFullToc {
    private String title;
    private List<TocContent> content;
    private int mediumId;
    private List<String> synonyms;
    private int mediumType;
    private boolean partsOnly;
    private boolean end;
    private String link;
    private String langCOO;
    private String langTL;
    private int statusCOO;
    private int statusTl;
    private List<LinkablePerson> authors;
    private List<LinkablePerson> artists;

    public ClientFullToc(String title, List<TocContent> content, int mediumId, List<String> synonyms, int mediumType,
            boolean partsOnly, boolean end, String link, String langCOO, String langTL, int statusCOO, int statusTl,
            List<LinkablePerson> authors, List<LinkablePerson> artists) {
        this.setTitle(title);
        this.setContent(content);
        this.setMediumId(mediumId);
        this.setSynonyms(synonyms);
        this.setMediumType(mediumType);
        this.setPartsOnly(partsOnly);
        this.setEnd(end);
        this.setLink(link);
        this.setLangCOO(langCOO);
        this.setLangTL(langTL);
        this.setStatusCOO(statusCOO);
        this.setStatusTl(statusTl);
        this.setAuthors(authors);
        this.setArtists(artists);
    }

    public List<LinkablePerson> getArtists() {
        return artists;
    }

    public void setArtists(List<LinkablePerson> artists) {
        this.artists = artists;
    }

    public List<LinkablePerson> getAuthors() {
        return authors;
    }

    public void setAuthors(List<LinkablePerson> authors) {
        this.authors = authors;
    }

    public int getStatusTl() {
        return statusTl;
    }

    public void setStatusTl(int statusTl) {
        this.statusTl = statusTl;
    }

    public int getStatusCOO() {
        return statusCOO;
    }

    public void setStatusCOO(int statusCOO) {
        this.statusCOO = statusCOO;
    }

    public String getLangTL() {
        return langTL;
    }

    public void setLangTL(String langTL) {
        this.langTL = langTL;
    }

    public String getLangCOO() {
        return langCOO;
    }

    public void setLangCOO(String langCOO) {
        this.langCOO = langCOO;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isPartsOnly() {
        return partsOnly;
    }

    public void setPartsOnly(boolean partsOnly) {
        this.partsOnly = partsOnly;
    }

    public int getMediumType() {
        return mediumType;
    }

    public void setMediumType(int mediumType) {
        this.mediumType = mediumType;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public int getMediumId() {
        return mediumId;
    }

    public void setMediumId(int mediumId) {
        this.mediumId = mediumId;
    }

    public List<TocContent> getContent() {
        return content;
    }

    public void setContent(List<TocContent> content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
