package com.mytlogos.enterprisedesktop.model;


import java.time.LocalDateTime;
import java.util.Objects;

public class MediumItem implements Medium {
    private final String title;
    private final int mediumId;
    private final String author;
    private final String artist;
    private final int medium;
    private final int stateTL;
    private final int stateOrigin;
    private final String countryOfOrigin;
    private final String languageOfOrigin;
    private final String lang;
    private final String series;
    private final String universe;
    private final Integer currentRead;
    private final Integer currentReadEpisode;
    private final Integer lastEpisode;
    private final LocalDateTime lastUpdated;

    public MediumItem(String title, int mediumId, String author, String artist, int medium, int stateTL, int stateOrigin, String countryOfOrigin, String languageOfOrigin, String lang, String series, String universe, Integer currentRead, Integer currentReadEpisode, Integer lastEpisode, LocalDateTime lastUpdated) {
        this.title = title;
        this.mediumId = mediumId;
        this.author = author;
        this.artist = artist;
        this.medium = medium;
        this.stateTL = stateTL;
        this.stateOrigin = stateOrigin;
        this.countryOfOrigin = countryOfOrigin;
        this.languageOfOrigin = languageOfOrigin;
        this.lang = lang;
        this.series = series;
        this.universe = universe;
        this.currentRead = currentRead;
        this.currentReadEpisode = currentReadEpisode;
        this.lastEpisode = lastEpisode;
        this.lastUpdated = lastUpdated;
    }

    public Integer getCurrentReadEpisode() {
        return currentReadEpisode;
    }

    public Integer getLastEpisode() {
        return lastEpisode;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public Integer getCurrentRead() {
        return currentRead;
    }

    @Override
    public int getMediumId() {
        return mediumId;
    }

    @Override
    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    @Override
    public String getLanguageOfOrigin() {
        return languageOfOrigin;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getMedium() {
        return medium;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public String getLang() {
        return lang;
    }

    @Override
    public int getStateOrigin() {
        return stateOrigin;
    }

    @Override
    public int getStateTL() {
        return stateTL;
    }

    @Override
    public String getSeries() {
        return series;
    }

    @Override
    public String getUniverse() {
        return universe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediumItem)) return false;

        MediumItem that = (MediumItem) o;

        if (mediumId != that.mediumId) return false;
        if (medium != that.medium) return false;
        if (stateTL != that.stateTL) return false;
        if (stateOrigin != that.stateOrigin) return false;
        if (!Objects.equals(title, that.title)) return false;
        if (!Objects.equals(author, that.author)) return false;
        if (!Objects.equals(artist, that.artist)) return false;
        if (!Objects.equals(countryOfOrigin, that.countryOfOrigin))
            return false;
        if (!Objects.equals(languageOfOrigin, that.languageOfOrigin))
            return false;
        if (!Objects.equals(lang, that.lang)) return false;
        if (!Objects.equals(series, that.series)) return false;
        if (!Objects.equals(universe, that.universe)) return false;
        if (!Objects.equals(currentRead, that.currentRead)) return false;
        if (!Objects.equals(currentReadEpisode, that.currentReadEpisode))
            return false;
        if (!Objects.equals(lastEpisode, that.lastEpisode)) return false;
        return Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + mediumId;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + medium;
        result = 31 * result + stateTL;
        result = 31 * result + stateOrigin;
        result = 31 * result + (countryOfOrigin != null ? countryOfOrigin.hashCode() : 0);
        result = 31 * result + (languageOfOrigin != null ? languageOfOrigin.hashCode() : 0);
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        result = 31 * result + (series != null ? series.hashCode() : 0);
        result = 31 * result + (universe != null ? universe.hashCode() : 0);
        result = 31 * result + (currentRead != null ? currentRead.hashCode() : 0);
        result = 31 * result + (currentReadEpisode != null ? currentReadEpisode.hashCode() : 0);
        result = 31 * result + (lastEpisode != null ? lastEpisode.hashCode() : 0);
        result = 31 * result + (lastUpdated != null ? lastUpdated.hashCode() : 0);
        return result;
    }
}
