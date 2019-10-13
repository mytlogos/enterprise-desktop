package com.mytlogos.enterprisedesktop.model;


import java.time.LocalDateTime;

public class NotificationItem {

    private String title;

    private String description;

    private LocalDateTime datetime;

    public NotificationItem( String title,  String description,  LocalDateTime datetime) {
        this.title = title;
        this.description = description;
        this.datetime = datetime;
    }

    public static NotificationItem createNow(String title, String description) {
        return new NotificationItem(title, description, LocalDateTime.now());
    }


    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }


    public LocalDateTime getDatetime() {
        return datetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationItem that = (NotificationItem) o;

        if (!getTitle().equals(that.getTitle())) return false;
        return getDatetime().equals(that.getDatetime());
    }

    @Override
    public int hashCode() {
        int result = getTitle().hashCode();
        result = 31 * result + getDatetime().hashCode();
        return result;
    }
}
