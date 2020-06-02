package com.mytlogos.enterprisedesktop.model;

import java.time.LocalDateTime;
import java.util.List;

public class TocEpisode implements Indexable, OpenableEpisode {
    private final int episodeId;
    private final float progress;
    private final LocalDateTime readDate;
    private final int partId;
    private final int totalIndex;
    private final int partialIndex;
    private final boolean saved;
    private final List<Release> releases;

    public TocEpisode(int episodeId, float progress, int partId, int partialIndex, int totalIndex, LocalDateTime readDate, boolean saved, List<Release> releases) {
        this.episodeId = episodeId;
        this.progress = progress;
        this.partId = partId;
        this.partialIndex = partialIndex;
        this.totalIndex = totalIndex;
        this.readDate = readDate;
        this.saved = saved;
        this.releases = releases;
    }

    public float getProgress() {
        return progress;
    }

    public int getPartId() {
        return partId;
    }

    @Override
    public int getPartialIndex() {
        return partialIndex;
    }

    @Override
    public int getTotalIndex() {
        return totalIndex;
    }

    public LocalDateTime getReadDate() {
        return readDate;
    }

    @Override
    public boolean isSaved() {
        return saved;
    }

    @Override
    public int getEpisodeId() {
        return episodeId;
    }

    public List<Release> getReleases() {
        return releases;
    }

    @Override
    public int hashCode() {
        return episodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TocEpisode that = (TocEpisode) o;

        return episodeId == that.episodeId;
    }
}
