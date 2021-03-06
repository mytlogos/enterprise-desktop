package com.mytlogos.enterprisedesktop.background.api.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API Model for DataStats.
 * Enterprise Web API 1.0.2.
 */
public class ClientStat {
    private final Map<String, Map<String, Partstat>> media;
    private final Map<String, ClientMediaStat> mediaStats;
    private final Map<String, List<Integer>> lists;
    private final Map<String, List<Integer>> extLists;
    private final Map<String, List<Integer>> extUser;

    public ClientStat(Map<String, Map<String, Partstat>> media, Map<String, ClientMediaStat> mediaStats, Map<String, List<Integer>> lists, Map<String, List<Integer>> extLists, Map<String, List<Integer>> extUser) {
        this.media = media;
        this.mediaStats = mediaStats;
        this.lists = lists;
        this.extLists = extLists;
        this.extUser = extUser;
    }

    public static class Partstat {
        public final long episodeCount;
        public final long episodeSum;
        public final long releaseCount;

        public Partstat(long episodeCount, long episodeSum, long releaseCount) {
            this.episodeCount = episodeCount;
            this.episodeSum = episodeSum;
            this.releaseCount = releaseCount;
        }
    }

    public static class ParsedStat {
        public final Map<Integer, Map<Integer, Partstat>> media;
        public final Map<Integer, ClientMediaStat> mediaStats;
        public final Map<Integer, List<Integer>> lists;
        public final Map<Integer, List<Integer>> extLists;
        public final Map<String, List<Integer>> extUser;

        private ParsedStat(Map<Integer, Map<Integer, Partstat>> media, Map<Integer, ClientMediaStat> mediaStats, Map<Integer, List<Integer>> lists, Map<Integer, List<Integer>> extLists, Map<String, List<Integer>> extUser) {
            this.media = media;
            this.mediaStats = mediaStats;
            this.lists = lists;
            this.extLists = extLists;
            this.extUser = extUser;
        }
    }

    public ParsedStat parse() {
        Map<Integer, Map<Integer, Partstat>> parsedMedia = new HashMap<>();
        Map<Integer, ClientMediaStat> parsedMediaStats = new HashMap<>();
        Map<Integer, List<Integer>> parsedLists = new HashMap<>();
        Map<Integer, List<Integer>> parsedExtLists = new HashMap<>();

        for (Map.Entry<String, Map<String, Partstat>> entry : this.media.entrySet()) {
            Map<Integer, Partstat> medium = new HashMap<>();

            for (Map.Entry<String, Partstat> partstatEntry : entry.getValue().entrySet()) {
                medium.put(Integer.parseInt(partstatEntry.getKey()), partstatEntry.getValue());
            }

            parsedMedia.put(Integer.parseInt(entry.getKey()), medium);
        }

        for (Map.Entry<String, List<Integer>> entry : this.lists.entrySet()) {
            parsedLists.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }

        for (Map.Entry<String, List<Integer>> entry : this.extLists.entrySet()) {
            parsedExtLists.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }

        for (Map.Entry<String, ClientMediaStat> entry : this.mediaStats.entrySet()) {
            parsedMediaStats.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }

        return new ParsedStat(parsedMedia, parsedMediaStats, parsedLists, parsedExtLists, this.extUser);
    }
}
