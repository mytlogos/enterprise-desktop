package com.mytlogos.enterprisedesktop.background.api.model;

import java.time.LocalDateTime;

/**
 * API Model for MediumRelease.
 * Enterprise Web API 1.0.2.
 */
public class ClientMediumRelease {
    private final int episodeId;
    private final String link;
    private final String title;
    private final LocalDateTime date;
    private final double combiIndex;
    private final boolean locked;

    public ClientMediumRelease(int episodeId, String title, String link, boolean locked,
            LocalDateTime date, double combiIndex) {
        this.episodeId = episodeId;
        this.title = title;
        this.link = link;
        this.locked = locked;
        this.date = date;
        this.combiIndex = combiIndex;
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

    public String getLink() {
        return link;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getCombiIndex() {
        return combiIndex;
    }

    @Override
    public String toString() {
        return "ClientMediumRelease{" + "episodeId=" + episodeId + ", title='" + title + '\'' + ", link='" + link + '\''
                + ", locked='" + locked + '\'' + ", date=" + date + '\'' + ", combiIndex=" + combiIndex + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ClientMediumRelease that = (ClientMediumRelease) o;

        if (getEpisodeId() != that.getEpisodeId())
            return false;
        if (isLocked() != that.isLocked())
            return false;
        if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null)
            return false;
        if (getLink() != null ? !getLink().equals(that.getLink()) : that.getLink() != null)
            return false;
        return getDate() != null ? getDate().equals(that.getDate())
                : that.getDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getEpisodeId();
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getLink() != null ? getLink().hashCode() : 0);
        result = 31 * result + (isLocked() ? 1 : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        return result;
    }
}
