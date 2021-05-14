package com.mytlogos.enterprisedesktop.background.api.model;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.model.News;

import java.time.LocalDateTime;

/**
 * API Model for PureNews.
 * Enterprise Web API 1.0.2.
 */
public class ClientPureNews implements News {
    private final String title;
    private final String link;
    private final LocalDateTime date;
    private final int id;
    private final boolean read;

    public ClientPureNews(String title, String link, LocalDateTime date, int id, boolean read) {
        this.title = title;
        this.link = link;
        this.date = date;
        this.id = id;
        this.read = read;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientNews that = (ClientNews) o;

        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public String toString() {
        return "ClientPureNews{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", date=" + date +
                ", id=" + id +
                ", read=" + read +
                '}';
    }

    @Override
    public int getMediumType() {
        return 0;
    }

    @Override
    public String getTimeStampString() {
        return Formatter.format(date);
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public boolean isRead() {
        return read;
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return date;
    }

    @Override
    public String getUrl() {
        return link;
    }
}
