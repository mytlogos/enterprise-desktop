package com.mytlogos.enterprisedesktop.model;


import java.time.LocalDateTime;

public interface Release {
    LocalDateTime getReleaseDate();

    String getTitle();

    String getUrl();

    int getEpisodeId();

    boolean isLocked();
}
