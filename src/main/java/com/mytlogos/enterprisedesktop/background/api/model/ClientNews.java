package com.mytlogos.enterprisedesktop.background.api.model;

import java.time.LocalDateTime;

public class ClientNews {
    private String title;
    private String link;
    private LocalDateTime date;
    private int id;
    private boolean read;

    public ClientNews(String title, String link, LocalDateTime date, int id, boolean read) {
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
        return "ClientNews{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", date=" + date +
                ", id=" + id +
                ", read=" + read +
                '}';
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
}
