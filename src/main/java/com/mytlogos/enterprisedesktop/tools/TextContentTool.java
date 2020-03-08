package com.mytlogos.enterprisedesktop.tools;


import com.mytlogos.enterprisedesktop.background.api.model.ClientDownloadedEpisode;
import com.mytlogos.enterprisedesktop.model.MediumType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextContentTool extends ContentTool {

    private Map<Integer, File> episodeFiles;
    private Map<Integer, String> episodePaths;

    TextContentTool(File contentDir) {
        super(contentDir);
    }

    @Override
    boolean isContentMedium(File file) {
        return file.getName().matches("^\\d+$") && file.isDirectory();
    }

    @Override
    public int getMedium() {
        return MediumType.TEXT;
    }

    @Override
    public String getItemPath(int mediumId, File dir) {
        if (dir == null) {
            return null;
        }
        final File[] files = dir.listFiles();

        if (files == null) {
            return null;
        }

        for (File file : files) {
            if (file.getName().matches(mediumId + "")) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    @Override
    void removeMediaEpisodes(Set<Integer> episodeIds, String path) {
        if (path == null || path.isEmpty()) {
            return;
        }
        Map<Integer, String> episodePaths = getEpisodePaths(path);

        episodePaths.keySet().retainAll(episodeIds);
        for (String toRemovePath : episodePaths.values()) {
            if (!new File(toRemovePath).delete()) {
                System.err.println("could not delete file: " + toRemovePath);
            }
        }
    }

    @Override
    public Map<Integer, String> getEpisodePaths(String mediumPath) {
        if (mediumPath == null) {
            throw new NullPointerException("expected path string, got: null");
        }
        final File file = new File(mediumPath);
        final File[] files = file.listFiles();
        if (files == null) {
            throw new IllegalStateException("either not a directory or an io error occurred listing its files: " + mediumPath);
        }
        final Pattern filePattern = Pattern.compile("(\\d+?).html");
        Map<Integer, String> episodeMap = new HashMap<>();

        for (File listFile : files) {
            final Matcher matcher = filePattern.matcher(listFile.getName());

            if (!matcher.matches()) {
                continue;
            }
            final String idString = matcher.group(1);
            final int episodeId = Integer.parseInt(idString);
            episodeMap.put(episodeId, listFile.getAbsolutePath());
        }
        return episodeMap;
    }

    @Override
    public void saveContent(Collection<ClientDownloadedEpisode> episodes, int mediumId) throws IOException {
        if (this.episodeFiles == null) {
            this.episodeFiles = this.getItemContainers();
        }

        File file;

        boolean writeable = writeable();

        if (!writeable) {
            throw new IOException("Out of Storage Space: Less than " + this.minMBSpaceAvailable + "  MB available");
        }
        if (episodeFiles.containsKey(mediumId)) {
            file = episodeFiles.get(mediumId);
        } else {
            file = new File(contentDir, mediumId + "");
            this.episodeFiles.put(mediumId, file);
        }
        if (file == null) {
            return;
        }
        if (!file.exists() && !file.mkdirs()) {
            throw new IOException("Could not create medium directory: " + file.getAbsolutePath());
        }
        for (ClientDownloadedEpisode episode : episodes) {
            final Path episodePath = Paths.get(file.getAbsolutePath(), episode.getEpisodeId() + ".html");
            final File episodeFile = episodePath.toFile();

            final byte[] bytes = toXhtml(episode).getBytes(StandardCharsets.UTF_8);

            if (episodeFile.exists() && bytes.length == episodeFile.length()) {
                continue;
            }
            Files.write(episodePath, bytes);
        }
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    Pattern getMediumContainerPattern() {
        return Pattern.compile("^(\\d+)$");
    }

    @Override
    int getMediumContainerPatternGroup() {
        return 1;
    }

    @Override
    public long getEpisodeSize(File value, int episodeId, Map<Integer, String> episodePaths) {
        this.episodePaths = episodePaths;
        return this.getEpisodeSize(value, episodeId);
    }

    @Override
    public long getEpisodeSize(File value, int episodeId) {
        if (this.episodePaths == null) {
            this.episodePaths = this.getEpisodePaths(value.getAbsolutePath());
        }
        String entryName = this.episodePaths.get(episodeId);
        return entryName == null || entryName.isEmpty() ? 0 : new File(entryName).length();
    }

    @Override
    public double getAverageEpisodeSize(int mediumId) {
        String path = this.getItemPath(mediumId);
        if (path == null || path.isEmpty()) {
            return 0;
        }
        final File mediumFile = new File(path);

        if (this.episodePaths == null) {
            this.episodePaths = this.getEpisodePaths(path);
        }
        double sum = 0;
        Set<Integer> values = this.episodePaths.keySet();
        for (Integer episodeId : values) {
            sum += this.getEpisodeSize(mediumFile, episodeId);
        }
        return values.isEmpty() ? 0 : sum / values.size();
    }

    private String toXhtml(ClientDownloadedEpisode episode) {
        String[] arrayContent = episode.getContent();

        if (arrayContent.length == 0) {
            return "";
        }
        String content = arrayContent[0];
        int titleIndex = content.indexOf(episode.getTitle());

        if (titleIndex < 0 || titleIndex > (content.length() / 3)) {
            content = "<h3>" + episode.getTitle() + "</h3>" + content;
        }

        if (content.matches("\\s*<html.*>(<head>.*</head>)?<body>.+</body></html>\\s*")) {
            return content;
        }
        return "<html><head><meta charset=\"UTF-8\"></head><body>" + content + "</body></html>";
    }
}
