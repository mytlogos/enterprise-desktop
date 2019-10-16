package com.mytlogos.enterprisedesktop.model;

import java.time.LocalDateTime;

/**
 *
 */
public interface News {
    int getMediumType();

    String getTimeStampString();

    String getTitle();

    int getId();

    boolean isRead();

    LocalDateTime getTimeStamp();

    String getUrl();
}
