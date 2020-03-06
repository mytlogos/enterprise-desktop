package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.model.EpisodeFilter;

import java.util.List;

/**
 *
 */
public class ReleaseFilter extends EpisodeFilter {
    public final int medium;
    public final int minEpisodeIndex;
    public final int maxEpisodeIndex;
    public final boolean latestOnly;
    public final List<Integer> listsIds;
    public final List<Integer> mediumIds;
    public final boolean ignoreMedia;
    public final boolean ignoreLists;

    ReleaseFilter(ReadFilter readFilter, SavedFilter savedFilter, int medium, int minEpisodeIndex, int maxEpisodeIndex, boolean latestOnly, List<Integer> listsIds, List<Integer> mediumIds, boolean ignoreMedia, boolean ignoreLists) {
        super(readFilter, savedFilter);
        this.medium = medium;
        this.minEpisodeIndex = minEpisodeIndex;
        this.maxEpisodeIndex = maxEpisodeIndex;
        this.latestOnly = latestOnly;
        this.listsIds = listsIds;
        this.mediumIds = mediumIds;
        this.ignoreMedia = ignoreMedia;
        this.ignoreLists = ignoreLists;
    }

    public ReleaseFilter(byte readFilter, byte savedFilter, int medium, int minEpisodeIndex, int maxEpisodeIndex, boolean latestOnly, List<Integer> listsIds, List<Integer> mediumIds, boolean ignoreMedia, boolean ignoreLists) {
        super(readFilter, savedFilter);
        this.medium = medium;
        this.minEpisodeIndex = minEpisodeIndex;
        this.maxEpisodeIndex = maxEpisodeIndex;
        this.latestOnly = latestOnly;
        this.listsIds = listsIds;
        this.mediumIds = mediumIds;
        this.ignoreMedia = ignoreMedia;
        this.ignoreLists = ignoreLists;
    }
}
