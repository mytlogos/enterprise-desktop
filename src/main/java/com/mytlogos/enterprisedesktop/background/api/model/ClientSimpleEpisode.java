package com.mytlogos.enterprisedesktop.background.api.model;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * API Model for SimpleEpisode.
 * Enterprise Web API 1.0.2.
 */
public class ClientSimpleEpisode {
    private final int id;
    private final int partId;
    private final int totalIndex;
    private final int partialIndex;
    private final LocalDateTime readDate;
    private final ClientEpisodeRelease[] releases;

    public ClientSimpleEpisode(int id, int partId, int totalIndex, int partialIndex, LocalDateTime readDate,
            ClientEpisodeRelease[] releases) {
        this.id = id;
        this.partId = partId;
        this.totalIndex = totalIndex;
        this.partialIndex = partialIndex;
        this.readDate = readDate;
        this.releases = releases;
    }

    public ClientEpisodeRelease[] getReleases() {
        return releases;
    }

    public int getId() {
        return id;
    }

    public double getCombiIndex() {
        return Double.parseDouble(this.getTotalIndex() + "." + this.getPartialIndex());
    }

    public int getEpisodeId() {
        return id;
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
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ClientSimpleEpisode that = (ClientSimpleEpisode) o;
        return id != that.id;
    }

    @Override
    public String toString() {
        return "ClientEpisode{" + "id=" + id + ", partId=" + partId + ", totalIndex=" + totalIndex + ", partialIndex="
                + partialIndex + ", readDate=" + readDate + ", releases=" + Arrays.toString(releases) + '}';
    }
}
