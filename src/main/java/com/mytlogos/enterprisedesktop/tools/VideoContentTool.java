package com.mytlogos.enterprisedesktop.tools;


import com.mytlogos.enterprisedesktop.background.api.model.ClientDownloadedEpisode;
import com.mytlogos.enterprisedesktop.model.MediumType;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

// TODO: 05.08.2019 implement this class
public class VideoContentTool extends ContentTool {

    VideoContentTool(File internalContentDir) {
        super(internalContentDir);
    }

    @Override
    public int getMedium() {
        return MediumType.VIDEO;
    }

    @Override
    boolean isContentMedium(File file) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public boolean isSupported() {
        return false;
    }

    @Override
    void removeMediaEpisodes(Set<Integer> episodeIds, String internalFile) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    Pattern getMediumContainerPattern() {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    int getMediumContainerPatternGroup() {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public Map<Integer, String> getEpisodePaths(String mediumPath) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public String getItemPath(int mediumId) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    String getItemPath(int mediumId, File dir) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public void saveContent(Collection<ClientDownloadedEpisode> episode, int mediumId) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public long getEpisodeSize(File value, int episodeId) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public double getAverageEpisodeSize(int mediumId) {
        throw new IllegalStateException("Not yet implemented");
    }
}
