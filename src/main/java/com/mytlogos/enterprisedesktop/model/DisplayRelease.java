package com.mytlogos.enterprisedesktop.model;


import java.time.LocalDateTime;

public class DisplayRelease implements OpenableEpisode {
    private final int episodeId;
    private final String combiIndex;
    private final boolean saved;
    private final boolean read;
    private final String title;
    private final LocalDateTime releaseDate;
    private final boolean locked;
    private final int mediumId;

    public DisplayRelease(int episodeId, String title, String combiIndex, boolean saved, boolean read, LocalDateTime releaseDate, boolean locked, int mediumId) {
        this.episodeId = episodeId;
        this.title = title;
        this.combiIndex = combiIndex;
        this.saved = saved;
        this.read = read;
        this.releaseDate = releaseDate;
        this.locked = locked;
        this.mediumId = mediumId;
    }

    public int getMediumId() {
        return mediumId;
    }

    public String getCombiIndex() {
        return combiIndex;
    }

    @Override
    public boolean isSaved() {
        return saved;
    }

    @Override
    public int getEpisodeId() {
        return episodeId;
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
        return getReleaseDate().equals(that.getReleaseDate());
    }

    @Override
    public String toString() {
        return "DisplayRelease{" +
                "episodeId=" + episodeId +
                ", combiIndex=" + combiIndex +
                ", saved=" + saved +
                ", read=" + read +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", locked=" + locked +
                '}';
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public boolean isLocked() {
        return locked;
    }
}
