package com.mytlogos.enterprisedesktop.background.api.model;

/**
 * API Model for SimpleRelease.
 * Enterprise Web API 1.0.2.
 */
public class ClientSimpleRelease {
    public final String url;
    public final int episodeId;

    public ClientSimpleRelease(String url, int episodeId) {
        this.url = url;
        this.episodeId = episodeId;
    }

    @Override
    public String toString() {
        return "ClientSimpleRelease{" +
                "url='" + url + '\'' +
                ", episodeId=" + episodeId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientSimpleRelease that = (ClientSimpleRelease) o;

        if (episodeId != that.episodeId) return false;
        return url.equals(that.url);
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + episodeId;
        return result;
    }
}
