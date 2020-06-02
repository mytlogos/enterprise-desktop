package com.mytlogos.enterprisedesktop.model;


import java.time.LocalDateTime;

public class DisplayRelease implements Indexable, OpenableEpisode {
    private final int episodeId;
    private final int mediumId;
    private final String mediumTitle;
    private final int totalIndex;
    private final int partialIndex;
    private final boolean saved;
    private final boolean read;
    private final int medium;

    private final String title;

    private final String url;

    private final LocalDateTime releaseDate;
    private final boolean locked;

    public DisplayRelease(int episodeId, int mediumId, String mediumTitle, int totalIndex, int partialIndex, boolean saved, boolean read, int medium, String title, String url, LocalDateTime releaseDate, boolean locked) {
        this.episodeId = episodeId;
        this.mediumId = mediumId;
        this.mediumTitle = mediumTitle;
        this.totalIndex = totalIndex;
        this.partialIndex = partialIndex;
        this.saved = saved;
        this.read = read;
        this.medium = medium;
        this.title = title;
        this.url = url;
        this.releaseDate = releaseDate;
        this.locked = locked;
    }

    public int getMedium() {
        return medium;
    }

    public int getMediumId() {
        return mediumId;
    }

    public String getMediumTitle() {
        return mediumTitle;
    }

    public int getPartialIndex() {
        return partialIndex;
    }

    public int getTotalIndex() {
        return totalIndex;
    }

    @Override
    public boolean isSaved() {
        return saved;
    }

    public boolean isRead() {
        return read;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int hashCode() {
        int result = getEpisodeId();
        result = 31 * result + getUrl().hashCode();
        result = 31 * result + getReleaseDate().hashCode();
        result = 31 * result + (isLocked() ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DisplayRelease that = (DisplayRelease) o;

        if (getEpisodeId() != that.getEpisodeId()) return false;
        if (isLocked() != that.isLocked()) return false;
        if (!getUrl().equals(that.getUrl())) return false;
        return getReleaseDate().equals(that.getReleaseDate());
    }

    @Override
    public String toString() {
        return "DisplayRelease{" +
                "episodeId=" + episodeId +
                ", mediumId=" + mediumId +
                ", mediumTitle='" + mediumTitle + '\'' +
                ", totalIndex=" + totalIndex +
                ", partialIndex=" + partialIndex +
                ", saved=" + saved +
                ", read=" + read +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", releaseDate=" + releaseDate +
                ", locked=" + locked +
                '}';
    }

    @Override
    public int getEpisodeId() {
        return episodeId;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public boolean isLocked() {
        return locked;
    }
}
