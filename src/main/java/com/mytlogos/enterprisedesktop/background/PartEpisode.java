package com.mytlogos.enterprisedesktop.background;

/**
 *
 */
public class PartEpisode {
    public final int episodeId;
    public final int partId;

    public PartEpisode(int episodeId, int partId) {
        this.episodeId = episodeId;
        this.partId = partId;
    }

    @Override
    public String toString() {
        return "PartEpisode{" +
                "episodeId=" + episodeId +
                ", partId=" + partId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PartEpisode)) return false;

        PartEpisode that = (PartEpisode) o;

        if (episodeId != that.episodeId) return false;
        return partId == that.partId;
    }

    @Override
    public int hashCode() {
        int result = episodeId;
        result = 31 * result + partId;
        return result;
    }
}
