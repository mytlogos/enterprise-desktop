package com.mytlogos.enterprisedesktop.model;

import java.time.LocalDateTime;

/**
 *
 */
public interface Episode {
    double getCombiIndex();

    int getEpisodeId();

    float getProgress();

    int getPartId();

    int getPartialIndex();

    int getTotalIndex();

    LocalDateTime getReadDate();

    boolean isSaved();
}
