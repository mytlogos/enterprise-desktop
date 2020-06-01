package com.mytlogos.enterprisedesktop.background.api.model;

import java.time.LocalDateTime;

public class ClientReadEpisode {
    private final int episodeId;
    private final LocalDateTime readDate;
    private final float progress;

    public ClientReadEpisode(int episodeId, LocalDateTime readDate, float progress) {
        this.episodeId = episodeId;
        this.readDate = readDate;
        this.progress = progress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientReadEpisode that = (ClientReadEpisode) o;

        return getEpisodeId() == that.getEpisodeId();
    }

    @Override
    public int hashCode() {
        return getEpisodeId();
    }

    @Override
    public String toString() {
        return "ClientReadEpisode{" +
                "id=" + episodeId +
                ", readDate=" + readDate +
                ", progress=" + progress +
                '}';
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public LocalDateTime getReadDate() {
        return readDate;
    }

    public float getProgress() {
        return progress;
    }
}
