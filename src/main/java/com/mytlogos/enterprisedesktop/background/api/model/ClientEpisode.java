package com.mytlogos.enterprisedesktop.background.api.model;


import com.mytlogos.enterprisedesktop.model.Episode;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * API Model for PureEpisode (releases == null), Episode.
 * Enterprise Web API 1.0.2.
 */
public class ClientEpisode implements Episode {
    private final int id;
    private final float progress;
    private final int partId;
    private final int totalIndex;
    private final int partialIndex;
    private final double combiIndex;
    private final LocalDateTime readDate;
    private final ClientRelease[] releases;

    public ClientEpisode(int id, float progress, int partId, int totalIndex, int partialIndex, double combiIndex, LocalDateTime readDate, ClientRelease[] releases) {
        this.id = id;
        this.progress = progress;
        this.partId = partId;
        this.totalIndex = totalIndex;
        this.partialIndex = partialIndex;
        this.combiIndex = combiIndex;
        this.readDate = readDate;
        this.releases = releases;
    }

    public ClientRelease[] getReleases() {
        return releases;
    }

    public int getId() {
        return id;
    }

    @Override
    public double getCombiIndex() {
        return combiIndex;
    }

    @Override
    public int getEpisodeId() {
        return id;
    }

    public float getProgress() {
        return progress;
    }

    public int getPartId() {
        return partId;
    }

    public int getPartialIndex() {
        return partialIndex;
    }

    public int getTotalIndex() {
        return totalIndex;
    }

    public LocalDateTime getReadDate() {
        return readDate;
    }

    @Override
    public boolean isSaved() {
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientEpisode that = (ClientEpisode) o;
        return id != that.id;
    }

    @Override
    public String toString() {
        return "ClientEpisode{" +
                "id=" + id +
                ", progress=" + progress +
                ", partId=" + partId +
                ", totalIndex=" + totalIndex +
                ", partialIndex=" + partialIndex +
                ", readDate=" + readDate +
                ", releases=" + Arrays.toString(releases) +
                '}';
    }
}
