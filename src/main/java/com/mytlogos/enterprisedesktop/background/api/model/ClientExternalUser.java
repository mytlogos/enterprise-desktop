package com.mytlogos.enterprisedesktop.background.api.model;

import com.mytlogos.enterprisedesktop.model.ExternalUser;

import java.util.Arrays;

public class ClientExternalUser implements ExternalUser {
    private String localUuid;
    private String uuid;
    private String identifier;
    private int type;
    private ClientExternalMediaList[] lists;

    public ClientExternalUser(String localUuid, String uuid, String identifier, int type, ClientExternalMediaList[] lists) {
        this.localUuid = localUuid;
        this.uuid = uuid;
        this.identifier = identifier;
        this.type = type;
        this.lists = lists;
    }

    public int getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String getUserUuid() {
        return localUuid;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ClientExternalMediaList[] getLists() {
        return lists;
    }

    public String getLocalUuid() {
        return localUuid;
    }

    @Override
    public int hashCode() {
        return getUuid() != null ? getUuid().hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientExternalUser that = (ClientExternalUser) o;

        return getUuid() != null ? getUuid().equals(that.getUuid()) : that.getUuid() == null;
    }

    @Override
    public String toString() {
        return "ClientExternalUser{" +
                "localUuid='" + localUuid + '\'' +
                ", uuid='" + uuid + '\'' +
                ", identifier='" + identifier + '\'' +
                ", type=" + type +
                ", lists=" + Arrays.toString(lists) +
                '}';
    }
}
