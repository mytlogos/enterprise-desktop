package com.mytlogos.enterprisedesktop.model;

import com.mytlogos.enterprisedesktop.Formatter;

import java.time.LocalDateTime;

public class NewsImpl implements News {
    private final String title;
    private final LocalDateTime timeStamp;
    private final int id;
    private final boolean read;
    private final String url;
    // this is so ugly, but at the moment mediumType is not saved in storage
    private final int mediumType;

    public NewsImpl(String title, LocalDateTime timeStamp, int id, boolean read, String url) {
        this(title, timeStamp, id, read, url, MediumType.ALL);
    }

    public NewsImpl(String title, LocalDateTime timeStamp, int id, boolean read, String url, int mediumType) {
        this.title = title;
        this.timeStamp = timeStamp;
        this.id = id;
        this.read = read;
        this.url = url;
        this.mediumType = mediumType;
    }

    @Override
    public int getMediumType() {
        return mediumType;
    }

    @Override
    public String getTimeStampString() {
        return Formatter.format(this.getTimeStamp());
    }


    @Override
    public String getTitle() {
        return this.title;
    }


    @Override
    public int getId() {
        return this.id;
    }


    @Override
    public boolean isRead() {
        return this.read;
    }


    @Override
    public LocalDateTime getTimeStamp() {
        return this.timeStamp;
    }


    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "NewsImpl{" +
                "title='" + title + '\'' +
                ", timeStamp=" + timeStamp +
                ", id=" + id +
                ", read=" + read +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewsImpl news = (NewsImpl) o;

        if (id != news.id) return false;
        if (isRead() != news.isRead()) return false;
        if (getTitle() != null ? !getTitle().equals(news.getTitle()) : news.getTitle() != null)
            return false;
        return getTimeStamp() != null ? getTimeStamp().equals(news.getTimeStamp()) : news.getTimeStamp() == null;
    }

    @Override
    public int hashCode() {
        int result = getTitle() != null ? getTitle().hashCode() : 0;
        result = 31 * result + (getTimeStamp() != null ? getTimeStamp().hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (isRead() ? 1 : 0);
        return result;
    }
}
