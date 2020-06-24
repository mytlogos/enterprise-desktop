package com.mytlogos.enterprisedesktop.profile;

import com.mytlogos.enterprisedesktop.controller.ReadFilter;
import com.mytlogos.enterprisedesktop.controller.SavedFilter;

import java.util.Collections;
import java.util.List;

public class DisplayEpisodeProfileBuilder {
    private int medium;
    private int minEpisodeIndex;
    private int maxEpisodeIndex;
    private boolean latestOnly;
    private List<Integer> filterListIds;
    private List<Integer> ignoreListIds;
    private List<Integer> filterMediumIds;
    private List<Integer> ignoreMediumIds;
    private byte readFilter;
    private byte savedFilter;

    public DisplayEpisodeProfileBuilder() {
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

    public DisplayEpisodeProfileBuilder setMedium(int medium) {
        this.medium = medium;
        return this;
    }

    public DisplayEpisodeProfileBuilder setMinEpisodeIndex(int minEpisodeIndex) {
        this.minEpisodeIndex = minEpisodeIndex;
        return this;
    }

    public DisplayEpisodeProfileBuilder setMaxEpisodeIndex(int maxEpisodeIndex) {
        this.maxEpisodeIndex = maxEpisodeIndex;
        return this;
    }

    public DisplayEpisodeProfileBuilder setLatestOnly(boolean latestOnly) {
        this.latestOnly = latestOnly;
        return this;
    }

    public DisplayEpisodeProfileBuilder setFilterListIds(List<Integer> filterListIds) {
        this.filterListIds = filterListIds;
        return this;
    }

    public DisplayEpisodeProfileBuilder setIgnoreListIds(List<Integer> ignoreListIds) {
        this.ignoreListIds = ignoreListIds;
        return this;
    }

    public DisplayEpisodeProfileBuilder setFilterMediumIds(List<Integer> filterMediumIds) {
        this.filterMediumIds = filterMediumIds;
        return this;
    }

    public DisplayEpisodeProfileBuilder setIgnoreMediumIds(List<Integer> ignoreMediumIds) {
        this.ignoreMediumIds = ignoreMediumIds;
        return this;
    }

    public DisplayEpisodeProfileBuilder setReadFilter(byte readFilter) {
        this.readFilter = readFilter;
        return this;
    }

    public DisplayEpisodeProfileBuilder setReadFilter(ReadFilter readFilter) {
        this.readFilter = readFilter == null ? -1 : readFilter.getValue();
        return this;
    }

    public DisplayEpisodeProfileBuilder setSavedFilter(byte savedFilter) {
        this.savedFilter = savedFilter;
        return this;
    }

    public DisplayEpisodeProfileBuilder setSavedFilter(SavedFilter savedFilter) {
        this.savedFilter = savedFilter == null ? -1 : savedFilter.getValue();
        return this;
    }

    public DisplayEpisodeProfile create() {
        return new DisplayEpisodeProfile(medium, minEpisodeIndex, maxEpisodeIndex, latestOnly, filterListIds, ignoreListIds, filterMediumIds, ignoreMediumIds, readFilter, savedFilter);
    }
}