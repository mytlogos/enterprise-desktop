package com.mytlogos.enterprisedesktop.background.api.model;

import java.time.LocalDateTime;

/**
 * API Model for DisplayRelease.
 * Enterprise Web API 1.0.2.
 */
public class ClientDisplayRelease {
    private final int episodeId;
    private final int mediumId;
    private final String link;
    private final String title;
    private final LocalDateTime date;
    private final boolean locked;
    private final float progress;

    public ClientDisplayRelease(int episodeId, int mediumId, String title, String link, boolean locked,
            LocalDateTime date, float progress) {
        this.episodeId = episodeId;
        this.mediumId = mediumId;
        this.title = title;
        this.link = link;
        this.locked = locked;
        this.date = date;
        this.progress = progress;
    }

    public float getProgress() {
        return progress;
    }

    public int getMediumId() {
        return mediumId;
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
        return link;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "ClientDisplayRelease{" + "id=" + episodeId + ", title='" + title + '\'' + ", link='" + link + '\''
                + ", locked='" + locked + '\'' + ", date=" + date +'\'' + ", progress=" + progress + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ClientDisplayRelease that = (ClientDisplayRelease) o;

        if (getEpisodeId() != that.getEpisodeId())
            return false;
        if (isLocked() != that.isLocked())
            return false;
        if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null)
            return false;
        if (getUrl() != null ? !getUrl().equals(that.getUrl()) : that.getUrl() != null)
            return false;
        return getDate() != null ? getDate().equals(that.getDate())
                : that.getDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getEpisodeId();
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
        result = 31 * result + (isLocked() ? 1 : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        return result;
    }
}
