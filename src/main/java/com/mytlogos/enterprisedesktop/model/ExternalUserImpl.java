package com.mytlogos.enterprisedesktop.model;

/**
 *
 */
public class ExternalUserImpl implements ExternalUser {
    private final String uuid;
    private final String userUuid;
    private final String identifier;
    private final int type;

    public ExternalUserImpl(String uuid, String userUuid, String identifier, int type) {
        this.uuid = uuid;
        this.userUuid = userUuid;
        this.identifier = identifier;
        this.type = type;
    }

    @Override
    public int hashCode() {
        int result = getUuid() != null ? getUuid().hashCode() : 0;
        result = 31 * result + (getUserUuid() != null ? getUserUuid().hashCode() : 0);
        result = 31 * result + (getIdentifier() != null ? getIdentifier().hashCode() : 0);
        result = 31 * result + getType();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExternalUser that = (ExternalUser) o;

        if (getType() != that.getType()) return false;
        if (getUuid() != null ? !getUuid().equals(that.getUuid()) : that.getUuid() != null) return false;
        if (getUserUuid() != null ? !getUserUuid().equals(that.getUserUuid()) : that.getUserUuid() != null)
            return false;
        return getIdentifier() != null ? getIdentifier().equals(that.getIdentifier()) : that.getIdentifier() == null;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getUserUuid() {
        return userUuid;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "ExternalUserImpl{" +
                "uuid='" + uuid + '\'' +
                ", userUuid='" + userUuid + '\'' +
                ", identifier='" + identifier + '\'' +
                ", type=" + type +
                '}';
    }
}
