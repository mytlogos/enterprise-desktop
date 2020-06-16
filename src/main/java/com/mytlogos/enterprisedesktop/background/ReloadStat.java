package com.mytlogos.enterprisedesktop.background;

import java.util.Collection;
import java.util.Objects;

public class ReloadStat {
    public final Collection<Integer> loadPartEpisodes;
    public final Collection<Integer> loadPartReleases;
    public final Collection<Integer> loadMediumTocs;
    public final Collection<Integer> loadMedium;
    public final Collection<Integer> loadPart;

    public ReloadStat(Collection<Integer> loadPartEpisodes, Collection<Integer> loadPartReleases, Collection<Integer> loadMediumTocs, Collection<Integer> loadMedium, Collection<Integer> loadPart) {
        this.loadPartEpisodes = loadPartEpisodes;
        this.loadPartReleases = loadPartReleases;
        this.loadMediumTocs = loadMediumTocs;
        this.loadMedium = loadMedium;
        this.loadPart = loadPart;
    }

    @Override
    public int hashCode() {
        int result = loadPartEpisodes != null ? loadPartEpisodes.hashCode() : 0;
        result = 31 * result + (loadPartReleases != null ? loadPartReleases.hashCode() : 0);
        result = 31 * result + (loadMediumTocs != null ? loadMediumTocs.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReloadStat)) return false;

        ReloadStat that = (ReloadStat) o;

        if (!Objects.equals(loadPartEpisodes, that.loadPartEpisodes))
            return false;
        if (!Objects.equals(loadPartReleases, that.loadPartReleases))
            return false;
        return Objects.equals(loadMediumTocs, that.loadMediumTocs);
    }

    @Override
    public String toString() {
        return "ReloadStat{" +
                "loadPartEpisodes=" + loadPartEpisodes +
                ", loadPartReleases=" + loadPartReleases +
                ", loadMediumTocs=" + loadMediumTocs +
                '}';
    }
}
