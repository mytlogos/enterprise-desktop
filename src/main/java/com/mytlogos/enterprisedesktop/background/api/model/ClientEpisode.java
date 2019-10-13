package com.mytlogos.enterprisedesktop.background.api.model;



import java.time.LocalDateTime;

import java.util.Arrays;

public class ClientEpisode {
    private final int id;
    private final float progress;
    private final int partId;
    private final int totalIndex;
    private final int partialIndex;
    private final LocalDateTime readDate;
    private final ClientRelease[] releases;

    public ClientEpisode(int id, float progress, int partId, int totalIndex, int partialIndex, LocalDateTime readDate, ClientRelease[] releases) {
        this.id = id;
        this.progress = progress;
        this.partId = partId;
        this.totalIndex = totalIndex;
        this.partialIndex = partialIndex;
        this.readDate = readDate;
        this.releases = releases;
    }

    public ClientRelease[] getReleases() {
        return releases;
    }

    public LocalDateTime getReadDate() {
        return readDate;
    }

    public int getId() {
        return id;
    }

    public int getPartId() {
        return partId;
    }

    public int getTotalIndex() {
        return totalIndex;
    }

    public int getPartialIndex() {
        return partialIndex;
    }

    public float getProgress() {
        return progress;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientEpisode that = (ClientEpisode) o;
        return id != that.id;
    }


    @Override
    public int hashCode() {
        return id;
    }
}
