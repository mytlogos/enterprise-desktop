package com.mytlogos.enterprisedesktop.background.api.model;

import com.mytlogos.enterprisedesktop.model.Release;

import java.time.LocalDateTime;

/**
 * API Model for EpisodeRelease.
 * Enterprise Web API 1.0.2.
 */
public class ClientEpisodeRelease implements Release {
    private final int episodeId;
    private final int tocId;
    private final String url;
    private final String title;
    private final LocalDateTime releaseDate;
    private final boolean locked;
    private final String sourceType;

    public ClientEpisodeRelease(int episodeId, int tocId, String title, String url, boolean locked,
            LocalDateTime releaseDate, String sourceType) {
        this.episodeId = episodeId;
        this.tocId = tocId;
        this.title = title;
        this.url = url;
        this.locked = locked;
        this.releaseDate = releaseDate;
        this.sourceType = sourceType;
    }

    public int getTocId() {
        return tocId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public boolean isLocked() {
        return locked;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "ClientEpisodeRelease{" + "id=" + episodeId + ", title='" + title + '\'' + ", url='" + url + '\''
                + ", locked='" + locked + '\'' + ", releaseDate=" + releaseDate + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ClientRelease that = (ClientRelease) o;

        if (getEpisodeId() != that.getEpisodeId())
            return false;
        if (isLocked() != that.isLocked())
            return false;
        if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null)
            return false;
        if (getUrl() != null ? !getUrl().equals(that.getUrl()) : that.getUrl() != null)
            return false;
        return getReleaseDate() != null ? getReleaseDate().equals(that.getReleaseDate())
                : that.getReleaseDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getEpisodeId();
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
        result = 31 * result + (isLocked() ? 1 : 0);
        result = 31 * result + (getReleaseDate() != null ? getReleaseDate().hashCode() : 0);
        return result;
    }
}
