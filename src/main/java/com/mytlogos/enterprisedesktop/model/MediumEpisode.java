package com.mytlogos.enterprisedesktop.model;

/**
 *
 */
public class MediumEpisode {
    public final int episodeId;
    public final int mediumId;
    public final double combiIndex;
    public final boolean saved;
    public final boolean read;

    public MediumEpisode(int episodeId, int mediumId, double combiIndex, boolean saved, boolean read) {
        this.episodeId = episodeId;
        this.mediumId = mediumId;
        this.combiIndex = combiIndex;
        this.saved = saved;
        this.read = read;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public int getMediumId() {
        return mediumId;
    }

    public double getCombiIndex() {
        return combiIndex;
    }

    public boolean isSaved() {
        return saved;
    }

    public boolean isRead() {
        return read;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = episodeId;
        result = 31 * result + mediumId;
        temp = Double.doubleToLongBits(combiIndex);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (saved ? 1 : 0);
        result = 31 * result + (read ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediumEpisode)) return false;

        MediumEpisode that = (MediumEpisode) o;

        if (episodeId != that.episodeId) return false;
        if (mediumId != that.mediumId) return false;
        if (Double.compare(that.combiIndex, combiIndex) != 0) return false;
        if (saved != that.saved) return false;
        return read == that.read;
    }

    @Override
    public String toString() {
        return "MediumEpisode{" +
                "episodeId=" + episodeId +
                ", mediumId=" + mediumId +
                ", combiIndex=" + combiIndex +
                ", saved=" + saved +
                ", read=" + read +
                '}';
    }
}
