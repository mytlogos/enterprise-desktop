package com.mytlogos.enterprisedesktop.model;

import java.io.Serializable;

public class MediumInWaitImpl implements Serializable, MediumInWait {
    /**
     * Alibi Uid.
     */
    private static final long serialVersionUID = 8428220461047209897L;
    private final String title;
    private final int medium;
    private final String link;

    public MediumInWaitImpl(String title, int medium, String link) {
        this.title = title;
        this.medium = medium;
        this.link = link;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getMedium() {
        return medium;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediumInWait that = (MediumInWait) o;

        if (getMedium() != that.getMedium()) return false;
        if (!getTitle().equals(that.getTitle())) return false;
        return getLink().equals(that.getLink());
    }

    @Override
    public int hashCode() {
        int result = getTitle().hashCode();
        result = 31 * result + getMedium();
        result = 31 * result + getLink().hashCode();
        return result;
    }
}
