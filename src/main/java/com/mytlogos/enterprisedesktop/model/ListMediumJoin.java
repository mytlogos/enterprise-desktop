package com.mytlogos.enterprisedesktop.model;

/**
 *
 */
public class ListMediumJoin {
    public final int listId;
    public final int mediumId;
    public final boolean external;

    public ListMediumJoin(int listId, int mediumId, boolean external) {
        this.listId = listId;
        this.mediumId = mediumId;
        this.external = external;
    }

    @Override
    public int hashCode() {
        int result = getListId();
        result = 31 * result + getMediumId();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListMediumJoin that = (ListMediumJoin) o;

        if (getListId() != that.getListId()) return false;
        return getMediumId() == that.getMediumId();
    }

    @Override
    public String toString() {
        return "ListMediumJoin{" +
                "listId=" + listId +
                ", mediumId=" + mediumId +
                '}';
    }

    public int getListId() {
        return listId;
    }

    public int getMediumId() {
        return mediumId;
    }

    public boolean isExternal() {
        return external;
    }
}
