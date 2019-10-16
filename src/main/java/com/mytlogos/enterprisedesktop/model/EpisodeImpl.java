package com.mytlogos.enterprisedesktop.model;

import java.time.LocalDateTime;

public class EpisodeImpl implements Episode {
    private final int episodeId;
    private final float progress;
    private final int partId;
    private final int partialIndex;
    private final int totalIndex;
    private final double combiIndex;
    private final LocalDateTime readDate;
    private final boolean saved;

    public EpisodeImpl(int episodeId, float progress, int partId, int partialIndex, int totalIndex, double combiIndex, LocalDateTime readDate, boolean saved) {
        this.episodeId = episodeId;
        this.progress = progress;
        this.partId = partId;
        this.partialIndex = partialIndex;
        this.totalIndex = totalIndex;
        this.combiIndex = combiIndex;
        this.readDate = readDate;
        this.saved = saved;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getEpisodeId();
        result = 31 * result + (getProgress() != +0.0f ? Float.floatToIntBits(getProgress()) : 0);
        result = 31 * result + getPartId();
        result = 31 * result + getPartialIndex();
        result = 31 * result + getTotalIndex();
        temp = Double.doubleToLongBits(getCombiIndex());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getReadDate() != null ? getReadDate().hashCode() : 0);
        result = 31 * result + (isSaved() ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EpisodeImpl episode = (EpisodeImpl) o;

        if (getEpisodeId() != episode.getEpisodeId()) return false;
        if (Float.compare(episode.getProgress(), getProgress()) != 0) return false;
        if (getPartId() != episode.getPartId()) return false;
        if (getPartialIndex() != episode.getPartialIndex()) return false;
        if (getTotalIndex() != episode.getTotalIndex()) return false;
        if (Double.compare(episode.getCombiIndex(), getCombiIndex()) != 0) return false;
        if (isSaved() != episode.isSaved()) return false;
        return getReadDate() != null ? getReadDate().equals(episode.getReadDate()) : episode.getReadDate() == null;
    }

    @Override
    public double getCombiIndex() {
        return combiIndex;
    }

    @Override
    public int getEpisodeId() {
        return episodeId;
    }

    @Override
    public float getProgress() {
        return progress;
    }

    @Override
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

    @Override
    public LocalDateTime getReadDate() {
        return readDate;
    }

    @Override
    public boolean isSaved() {
        return saved;
    }

    @Override
    public String toString() {
        return "EpisodeImpl{" +
                "episodeId=" + episodeId +
                ", progress=" + progress +
                ", partId=" + partId +
                ", partialIndex=" + partialIndex +
                ", totalIndex=" + totalIndex +
                ", combiIndex=" + combiIndex +
                ", readDate=" + readDate +
                ", saved=" + saved +
                '}';
    }
}
