package com.mytlogos.enterprisedesktop.background.api.model;

import com.mytlogos.enterprisedesktop.model.ExternalUser;

import java.util.Arrays;

/**
 * API Model for PureExternalUser (lists == null) and DisplayExternalUser.
 * Enterprise Web API 1.0.2.
 */
public class ClientExternalUser implements ExternalUser {
    private final String localUuid;
    private final String uuid;
    private final String identifier;
    private final int type;
    private final ClientExternalMediaList[] lists;

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
