package com.mytlogos.enterprisedesktop.model;

public class MediaListImpl implements MediaList {
    private final String uuid;
    private final int listId;
    private final String name;
    private final int medium;
    private final int size;

    public MediaListImpl(String uuid, int listId, String name, int medium, int size) {
        this.uuid = uuid;
        this.listId = listId;
        this.name = name;
        this.medium = medium;
        this.size = size;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public int getListId() {
        return listId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMedium() {
        return medium;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaListImpl mediaList = (MediaListImpl) o;

        return listId == mediaList.listId;
    }

    @Override
    public int hashCode() {
        return listId;
    }
}
