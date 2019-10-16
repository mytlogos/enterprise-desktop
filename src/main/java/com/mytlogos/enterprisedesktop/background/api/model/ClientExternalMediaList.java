package com.mytlogos.enterprisedesktop.background.api.model;

import com.mytlogos.enterprisedesktop.model.ExternalMediaList;

import java.util.Arrays;

public class ClientExternalMediaList implements ExternalMediaList {
    private String uuid;
    private int id;
    private String name;
    private int medium;
    private String url;
    private int[] items;

    public ClientExternalMediaList(String uuid, int id, String name, int medium, String url,
                                   int[] items) {
        this.uuid = uuid;
        this.id = id;
        this.name = name;
        this.medium = medium;
        this.url = url;
        this.items = items;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public int getListId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMedium() {
        return medium;
    }

    public String getUrl() {
        return url;
    }

    public int[] getItems() {
        return items;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientExternalMediaList that = (ClientExternalMediaList) o;

        return getId() == that.getId();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ClientExternalMediaList{" +
                "uuid='" + uuid + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", medium=" + medium +
                ", url='" + url + '\'' +
                ", items=" + Arrays.toString(items) +
                '}';
    }
}
