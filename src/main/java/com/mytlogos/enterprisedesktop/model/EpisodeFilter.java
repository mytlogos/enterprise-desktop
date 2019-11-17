package com.mytlogos.enterprisedesktop.model;

import com.mytlogos.enterprisedesktop.controller.ReadFilter;
import com.mytlogos.enterprisedesktop.controller.SavedFilter;

/**
 *
 */
public class EpisodeFilter {
    public final byte readFilter;
    public final byte savedFilter;

    public EpisodeFilter(ReadFilter readFilter, SavedFilter savedFilter) {
        this(readFilter != null ? readFilter.getValue() : -1, savedFilter != null ? savedFilter.getValue() : -1);
    }

    public EpisodeFilter(byte readFilter, byte savedFilter) {
        this.readFilter = readFilter;
        this.savedFilter = savedFilter;
    }
}
