package com.mytlogos.enterprisedesktop.background.api.model;

import com.mytlogos.enterprisedesktop.model.User;

/**
 * API Model for SimpleUser.
 * Enterprise Web API 1.0.2.
 */
public class ClientSimpleUser implements User {
    private final String uuid;
    private final String session;
    private final String name;

    public ClientSimpleUser(String uuid, String session, String name) {
        this.uuid = uuid;
        this.session = session;
        this.name = name;
    }

    @Override
    public int hashCode() {
        int result = getUuid() != null ? getUuid().hashCode() : 0;
        result = 31 * result + (getSession() != null ? getSession().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientSimpleUser that = (ClientSimpleUser) o;

        if (getUuid() != null ? !getUuid().equals(that.getUuid()) : that.getUuid() != null)
            return false;
        if (getSession() != null ? !getSession().equals(that.getSession()) : that.getSession() != null)
            return false;
        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
    }

    public String getUuid() {
        return uuid;
    }

    public String getSession() {
        return session;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ClientSimpleUser{" +
                "uuid='" + uuid + '\'' +
                ", session='" + session + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
