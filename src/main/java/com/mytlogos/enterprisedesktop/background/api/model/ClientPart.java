package com.mytlogos.enterprisedesktop.background.api.model;

import com.mytlogos.enterprisedesktop.model.Part;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientPart implements Part {
    private final int mediumId;
    private final int id;
    private final String title;
    private final int totalIndex;
    private final int partialIndex;
    private final ClientEpisode[] episodes;

    public ClientPart(int mediumId, int id, String title, int totalIndex, int partialIndex, ClientEpisode[] episodes) {
        this.mediumId = mediumId;
        this.id = id;
        this.title = title;
        this.totalIndex = totalIndex;
        this.partialIndex = partialIndex;
        this.episodes = episodes;
    }

    @Override
    public int hashCode() {
        return getPartId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientPart that = (ClientPart) o;

        return getPartId() == that.getPartId();
    }

    @Override
    public String toString() {
        return "ClientPart{" +
                "mediumId=" + mediumId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", totalIndex=" + totalIndex +
                ", partialIndex=" + partialIndex +
                ", episodes=" + Arrays.toString(episodes) +
                '}';
    }

    public int getId() {
        return id;
    }

    @Override
    public int getPartId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getTotalIndex() {
        return totalIndex;
    }

    public int getPartialIndex() {
        return partialIndex;
    }

    public int getMediumId() {
        return mediumId;
    }

    @Override
    public double getCombiIndex() {
        return Double.parseDouble(this.getTotalIndex() + "." + this.getPartialIndex());
    }

    public ClientEpisode[] getClientEpisodes() {
        return episodes;
    }

    public List<Integer> getEpisodes() {
        final List<Integer> ids = new ArrayList<>(this.episodes.length);

        for (ClientEpisode episode : this.episodes) {
            ids.add(episode.getId());
        }
        return ids;
    }
}
