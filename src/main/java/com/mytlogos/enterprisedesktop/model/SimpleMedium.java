package com.mytlogos.enterprisedesktop.model;

public class SimpleMedium implements Medium {
    private final int mediumId;
    private final String title;
    private final int medium;

    public SimpleMedium(int mediumId, String title, int medium) {
        this.mediumId = mediumId;
        this.title = title;
        this.medium = medium;
    }

    public int getMedium() {
        return medium;
    }

    @Override
    public String getArtist() {
        return null;
    }

    @Override
    public String getLang() {
        return null;
    }

    @Override
    public int getStateOrigin() {
        return 0;
    }

    @Override
    public int getStateTL() {
        return 0;
    }

    @Override
    public String getSeries() {
        return null;
    }

    @Override
    public String getUniverse() {
        return null;
    }

    @Override
    public Integer getCurrentRead() {
        return null;
    }

    public int getMediumId() {
        return mediumId;
    }

    @Override
    public String getCountryOfOrigin() {
        return null;
    }

    @Override
    public String getLanguageOfOrigin() {
        return null;
    }

    @Override
    public String getAuthor() {
        return null;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleMedium that = (SimpleMedium) o;

        return getMediumId() != that.getMediumId();
    }

    @Override
    public int hashCode() {
        return getMediumId();
    }
}
