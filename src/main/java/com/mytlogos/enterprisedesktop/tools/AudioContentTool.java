package com.mytlogos.enterprisedesktop.tools;

import com.mytlogos.enterprisedesktop.background.api.model.ClientDownloadedEpisode;
import com.mytlogos.enterprisedesktop.model.MediumType;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

// TODO: 05.08.2019 implement this class
public class AudioContentTool extends ContentTool {

    AudioContentTool(File internalContentDir, File externalContentDir) {
        super(internalContentDir, externalContentDir);
    }

    @Override
    boolean isContentMedium(File file) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public int getMedium() {
        return MediumType.AUDIO;
    }

    @Override
    String getItemPath(int mediumId, File dir) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    void removeMediaEpisodes(Set<Integer> episodeIds, String internalFile) {
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
    public void saveContent(Collection<ClientDownloadedEpisode> episode, int mediumId) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public boolean isSupported() {
        return false;
    }

    @Override
    public void mergeExternalAndInternalMedia(boolean toExternal) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    void mergeExternalAndInternalMedium(boolean toExternal, File source, File goal, File toParent, Integer mediumId) {
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
    public long getEpisodeSize(File value, int episodeId) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public double getAverageEpisodeSize(int mediumId) {
        throw new IllegalStateException("Not yet implemented");
    }
}
