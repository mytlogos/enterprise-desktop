package com.mytlogos.enterprisedesktop.background.api.model;



import java.util.Objects;

public class InvalidatedData {
    private final int mediumId;
    private final int partId;
    private final int newsId;
    private final int episodeId;
    private final boolean userUuid;
    private final String externalUuid;
    private final int externalListId;
    private final int listId;
    private final String uuid;

    public InvalidatedData(int mediumId, int partId, int newsId, int episodeId, boolean userUuid, String externalUuid, int externalListId, int listId, String uuid) {
        this.mediumId = mediumId;
        this.partId = partId;
        this.newsId = newsId;
        this.episodeId = episodeId;
        this.userUuid = userUuid;
        this.externalUuid = externalUuid;
        this.externalListId = externalListId;
        this.listId = listId;
        this.uuid = uuid;
    }


    @Override
    public String toString() {
        return "InvalidatedData{" +
                "mediumId=" + mediumId +
                ", partId=" + partId +
                ", id=" + episodeId +
                ", userUuid=" + userUuid +
                ", externalUuid=" + externalUuid +
                ", externalListId=" + externalListId +
                ", listId=" + listId +
                ", newsId=" + newsId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvalidatedData that = (InvalidatedData) o;
        return mediumId == that.mediumId &&
                partId == that.partId &&
                newsId == that.newsId &&
                episodeId == that.episodeId &&
                userUuid == that.userUuid &&
                externalListId == that.externalListId &&
                listId == that.listId &&
                Objects.equals(externalUuid, that.externalUuid) &&
                Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mediumId, partId, newsId, episodeId, userUuid, externalUuid, externalListId, listId, uuid);
    }

    public int getNewsId() {
        return newsId;
    }

    public String getUuid() {
        return uuid;
    }

    public int getMediumId() {
        return mediumId;
    }

    public int getPartId() {
        return partId;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public boolean isUserUuid() {
        return userUuid;
    }

    public String getExternalUuid() {
        return externalUuid;
    }

    public int getExternalListId() {
        return externalListId;
    }

    public int getListId() {
        return listId;
    }
}
