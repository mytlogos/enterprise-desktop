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
    public final List<Integer> listsIds;
    public final List<Integer> mediumIds;
    public final boolean ignoreMedia;
    public final boolean ignoreLists;
    public final byte readFilter;
    public final byte savedFilter;

    public DisplayEpisodeProfile(int medium, int minEpisodeIndex, int maxEpisodeIndex, boolean latestOnly, List<Integer> listsIds, List<Integer> mediumIds, boolean ignoreMedia, boolean ignoreLists, byte readFilter, byte savedFilter) {
        this.medium = medium;
        this.minEpisodeIndex = minEpisodeIndex;
        this.maxEpisodeIndex = maxEpisodeIndex;
        this.latestOnly = latestOnly;
        this.listsIds = listsIds;
        this.mediumIds = mediumIds;
        this.ignoreMedia = ignoreMedia;
        this.ignoreLists = ignoreLists;
        this.readFilter = readFilter;
        this.savedFilter = savedFilter;
    }

    public DisplayEpisodeProfile(int medium, int minEpisodeIndex, int maxEpisodeIndex, boolean latestOnly, List<Integer> listsIds, List<Integer> mediumIds, boolean ignoreMedia, boolean ignoreLists, ReadFilter readFilter, SavedFilter savedFilter) {
        this.medium = medium;
        this.minEpisodeIndex = minEpisodeIndex;
        this.maxEpisodeIndex = maxEpisodeIndex;
        this.latestOnly = latestOnly;
        this.listsIds = listsIds;
        this.mediumIds = mediumIds;
        this.ignoreMedia = ignoreMedia;
        this.ignoreLists = ignoreLists;
        this.readFilter = readFilter == null ? -1 : readFilter.getValue();
        this.savedFilter = savedFilter == null ? -1 : savedFilter.getValue();
    }

    public DisplayEpisodeProfile() {
        this.medium = 0;
        this.minEpisodeIndex = -1;
        this.maxEpisodeIndex = -1;
        this.latestOnly = false;
        this.listsIds = Collections.emptyList();
        this.mediumIds = Collections.emptyList();
        this.ignoreMedia = false;
        this.ignoreLists = false;
        this.readFilter = -1;
        this.savedFilter = -1;
    }

    @Override
    public int hashCode() {
        int result = medium;
        result = 31 * result + minEpisodeIndex;
        result = 31 * result + maxEpisodeIndex;
        result = 31 * result + (latestOnly ? 1 : 0);
        result = 31 * result + (listsIds != null ? listsIds.hashCode() : 0);
        result = 31 * result + (mediumIds != null ? mediumIds.hashCode() : 0);
        result = 31 * result + (ignoreMedia ? 1 : 0);
        result = 31 * result + (ignoreLists ? 1 : 0);
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
        if (ignoreMedia != that.ignoreMedia) return false;
        if (ignoreLists != that.ignoreLists) return false;
        if (!Objects.equals(listsIds, that.listsIds)) return false;
        return Objects.equals(mediumIds, that.mediumIds);
    }

    @Override
    public String toString() {
        return "DisplayEpisodeProfile{" +
                "medium=" + medium +
                ", minEpisodeIndex=" + minEpisodeIndex +
                ", maxEpisodeIndex=" + maxEpisodeIndex +
                ", latestOnly=" + latestOnly +
                ", listsIds=" + listsIds +
                ", mediumIds=" + mediumIds +
                ", ignoreMedia=" + ignoreMedia +
                ", ignoreLists=" + ignoreLists +
                '}';
    }
}
