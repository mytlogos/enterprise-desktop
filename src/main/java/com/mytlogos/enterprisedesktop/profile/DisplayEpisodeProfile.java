package com.mytlogos.enterprisedesktop.profile;

import com.mytlogos.enterprisedesktop.controller.ReadFilter;
import com.mytlogos.enterprisedesktop.controller.SavedFilter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class DisplayEpisodeProfile extends AbstractProfile {
    public final int medium;
    public final int minEpisodeIndex;
    public final int maxEpisodeIndex;
    public final boolean latestOnly;
    public final List<Integer> filterListIds;
    public final List<Integer> ignoreListIds;
    public final List<Integer> filterMediumIds;
    public final List<Integer> ignoreMediumIds;
    public final byte readFilter;
    public final byte savedFilter;

    public DisplayEpisodeProfile(int medium, int minEpisodeIndex, int maxEpisodeIndex, boolean latestOnly, List<Integer> filterListIds, List<Integer> ignoreListIds, List<Integer> filterMediumIds, List<Integer> ignoreMediumIds, byte readFilter, byte savedFilter) {
        this.medium = medium;
        this.minEpisodeIndex = minEpisodeIndex;
        this.maxEpisodeIndex = maxEpisodeIndex;
        this.latestOnly = latestOnly;
        this.filterListIds = filterListIds;
        this.ignoreListIds = ignoreListIds;
        this.filterMediumIds = filterMediumIds;
        this.ignoreMediumIds = ignoreMediumIds;
        this.readFilter = readFilter;
        this.savedFilter = savedFilter;
    }

    public DisplayEpisodeProfile(int medium, int minEpisodeIndex, int maxEpisodeIndex, boolean latestOnly, List<Integer> filterListIds, List<Integer> ignoreListIds, List<Integer> filterMediumIds, List<Integer> ignoreMediumIds, ReadFilter readFilter, SavedFilter savedFilter) {
        this.medium = medium;
        this.minEpisodeIndex = minEpisodeIndex;
        this.maxEpisodeIndex = maxEpisodeIndex;
        this.latestOnly = latestOnly;
        this.filterListIds = filterListIds;
        this.ignoreListIds = ignoreListIds;
        this.filterMediumIds = filterMediumIds;
        this.ignoreMediumIds = ignoreMediumIds;
        this.readFilter = readFilter == null ? -1 : readFilter.getValue();
        this.savedFilter = savedFilter == null ? -1 : savedFilter.getValue();
    }

    public DisplayEpisodeProfile() {
        this.medium = 0;
        this.minEpisodeIndex = -1;
        this.maxEpisodeIndex = -1;
        this.latestOnly = false;
        this.filterListIds = Collections.emptyList();
        this.ignoreListIds = Collections.emptyList();
        this.filterMediumIds = Collections.emptyList();
        this.ignoreMediumIds = Collections.emptyList();
        this.readFilter = -1;
        this.savedFilter = -1;
    }

    @Override
    public int hashCode() {
        int result = medium;
        result = 31 * result + minEpisodeIndex;
        result = 31 * result + maxEpisodeIndex;
        result = 31 * result + (latestOnly ? 1 : 0);
        result = 31 * result + (filterListIds != null ? filterListIds.hashCode() : 0);
        result = 31 * result + (ignoreListIds != null ? ignoreListIds.hashCode() : 0);
        result = 31 * result + (filterMediumIds != null ? filterMediumIds.hashCode() : 0);
        result = 31 * result + (ignoreMediumIds != null ? ignoreMediumIds.hashCode() : 0);
        result = 31 * result + (int) readFilter;
        result = 31 * result + (int) savedFilter;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DisplayEpisodeProfile)) return false;

        DisplayEpisodeProfile that = (DisplayEpisodeProfile) o;

        if (medium != that.medium) return false;
        if (minEpisodeIndex != that.minEpisodeIndex) return false;
        if (maxEpisodeIndex != that.maxEpisodeIndex) return false;
        if (latestOnly != that.latestOnly) return false;
        if (readFilter != that.readFilter) return false;
        if (savedFilter != that.savedFilter) return false;
        if (!Objects.equals(filterListIds, that.filterListIds))
            return false;
        if (!Objects.equals(ignoreListIds, that.ignoreListIds))
            return false;
        if (!Objects.equals(filterMediumIds, that.filterMediumIds))
            return false;
        return Objects.equals(ignoreMediumIds, that.ignoreMediumIds);
    }

    @Override
    public String toString() {
        return "DisplayEpisodeProfile{" +
                "medium=" + medium +
                ", minEpisodeIndex=" + minEpisodeIndex +
                ", maxEpisodeIndex=" + maxEpisodeIndex +
                ", latestOnly=" + latestOnly +
                ", filterListIds=" + filterListIds +
                ", ignoreListIds=" + ignoreListIds +
                ", filterMediumIds=" + filterMediumIds +
                ", ignoreMediumIds=" + ignoreMediumIds +
                ", readFilter=" + readFilter +
                ", savedFilter=" + savedFilter +
                '}';
    }
}
