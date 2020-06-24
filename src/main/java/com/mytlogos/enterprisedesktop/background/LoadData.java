package com.mytlogos.enterprisedesktop.background;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A container for synchronized Sets.
 */
public class LoadData {
    private final Set<Integer> media = Collections.synchronizedSet(new HashSet<>());
    private final Set<Integer> part = Collections.synchronizedSet(new HashSet<>());
    private final Set<Integer> episode = Collections.synchronizedSet(new HashSet<>());
    private final Set<Integer> news = Collections.synchronizedSet(new HashSet<>());
    private final Set<String> externalUser = Collections.synchronizedSet(new HashSet<>());
    private final Set<Integer> externalMediaList = Collections.synchronizedSet(new HashSet<>());
    private final Set<Integer> mediaList = Collections.synchronizedSet(new HashSet<>());

    public Set<Integer> getMedia() {
        return media;
    }

    public Set<Integer> getPart() {
        return part;
    }

    public Set<Integer> getEpisodes() {
        return episode;
    }

    public Set<Integer> getNews() {
        return news;
    }

    public Set<Integer> getExternalMediaList() {
        return externalMediaList;
    }

    public Set<String> getExternalUser() {
        return externalUser;
    }

    public Set<Integer> getMediaList() {
        return mediaList;
    }

    public void clearAll() {
        this.media.clear();
        this.part.clear();
        this.episode.clear();
        this.news.clear();
        this.externalMediaList.clear();
        this.externalUser.clear();
        this.mediaList.clear();
    }
}
