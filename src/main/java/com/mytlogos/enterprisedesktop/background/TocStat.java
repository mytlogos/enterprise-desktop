package com.mytlogos.enterprisedesktop.background;

/**
 *
 */
public class TocStat {
    public final int mediumId;
    public final int count;

    public TocStat(int mediumId, int count) {
        this.mediumId = mediumId;
        this.count = count;
    }

    @Override
    public int hashCode() {
        int result = mediumId;
        result = 31 * result + count;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TocStat)) return false;

        TocStat tocStat = (TocStat) o;

        if (mediumId != tocStat.mediumId) return false;
        return count == tocStat.count;
    }

    @Override
    public String toString() {
        return "TocStat{" +
                "mediumId=" + mediumId +
                ", count=" + count +
                '}';
    }
}
