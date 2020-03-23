package com.mytlogos.enterprisedesktop.tools;

import com.mytlogos.enterprisedesktop.background.api.model.ClientDownloadedEpisode;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 */
public class PdfContentTool extends ContentTool {
    PdfContentTool(File contentDir) {
        super(contentDir);
    }

    @Override
    boolean isContentMedium(File file) {
        return file.isFile() && file.getName().matches("^.+\\.pdf$");
    }

    @Override
    public int getMedium() {
        return 0;
    }

    @Override
    String getItemPath(int mediumId, File dir) {
        return null;
    }

    @Override
    void removeMediaEpisodes(Set<Integer> episodeIds, String internalFile) {

    }

    @Override
    public Map<Integer, String> getEpisodePaths(String mediumPath) {
        return null;
    }

    @Override
    public void saveContent(Collection<ClientDownloadedEpisode> episode, int mediumId) throws IOException {

    }

    @Override
    public boolean isSupported() {
        return false;
    }

    @Override
    Pattern getMediumContainerPattern() {
        return null;
    }

    @Override
    int getMediumContainerPatternGroup() {
        return 0;
    }

    @Override
    public long getEpisodeSize(File value, int episodeId) {
        return 0;
    }

    @Override
    public double getAverageEpisodeSize(int mediumId) {
        return 0;
    }

    public void readPdf(File file) {

    }
}
