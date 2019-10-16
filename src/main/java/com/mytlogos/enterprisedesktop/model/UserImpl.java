package com.mytlogos.enterprisedesktop.model;

public class UserImpl implements User {
    private final String uuid;
    private final String session;
    private final String name;

    public UserImpl(String uuid, String session, String name) {
        this.uuid = uuid;
        this.session = session;
        this.name = name;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getSession() {
        return session;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (getUuid() != null ? !getUuid().equals(user.getUuid()) : user.getUuid() != null)
            return false;
        return getSession() != null ? getSession().equals(user.getSession()) : user.getSession() == null;
    }

    @Override
    public int hashCode() {
        int result = getUuid() != null ? getUuid().hashCode() : 0;
        result = 31 * result + (getSession() != null ? getSession().hashCode() : 0);
        return result;
    }
}
