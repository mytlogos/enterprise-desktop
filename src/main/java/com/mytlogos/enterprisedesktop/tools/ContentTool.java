package com.mytlogos.enterprisedesktop.tools;

import com.mytlogos.enterprisedesktop.background.api.model.ClientDownloadedEpisode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ContentTool {
    final File contentDir;
    static final int MIN_MB_SPACE_AVAILABLE = 150;


    ContentTool(File contentDir) {
        this.contentDir = contentDir;
    }

    /**
     * Copied from <a href="https://stackoverflow.com/a/4770586/9492864">
     * How to move/rename file from internal app storage to external storage on Android?
     * </a>
     */
    public static void copyFile(File src, File dst) throws IOException {
        try (FileChannel inChannel = new FileInputStream(src).getChannel(); FileChannel outChannel = new FileOutputStream(dst).getChannel()) {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        }
    }

    public List<String> getMediaPaths() {
        List<String> books = new ArrayList<>();

        if (this.contentDir != null) {
            books.addAll(this.getMediaPaths(contentDir));
        }
        return books;
    }

    private List<String> getMediaPaths(File dir) {
        List<String> mediaPaths = new ArrayList<>();
        final File[] files = dir.listFiles();

        if (files == null) {
            return mediaPaths;
        }
        for (File file : files) {
            if (isContentMedium(file)) {
                mediaPaths.add(file.getAbsolutePath());
            }
        }
        return mediaPaths;
    }

    abstract boolean isContentMedium(File file);

    public abstract int getMedium();

    public void removeMediaEpisodes(int mediumId, Set<Integer> episodeIds) {
        if (this.contentDir != null) {
            String itemPath = getItemPath(mediumId, this.contentDir);

            if (itemPath != null) {
                this.removeMediaEpisodes(episodeIds, itemPath);
            }
        }
    }

    abstract String getItemPath(int mediumId, File dir);

    abstract void removeMediaEpisodes(Set<Integer> episodeIds, String internalFile);

    public abstract Map<Integer, String> getEpisodePaths(String mediumPath);

    public String getItemPath(int mediumId) {
        String bookZipFile = null;

        if (this.contentDir != null) {
            bookZipFile = getItemPath(mediumId, this.contentDir);
        }

        if (bookZipFile == null) {
            return "";
        }
        return bookZipFile;
    }

    public abstract void saveContent(Collection<ClientDownloadedEpisode> episode, int mediumId) throws IOException;

    public abstract boolean isSupported();

    public Map<Integer, File> getItemContainers() {
        File file = this.contentDir;

        if (file == null) {
            return new HashMap<>();
        }
        Pattern pattern = getMediumContainerPattern();
        File[] files = file.listFiles((dir, name) -> Pattern.matches(pattern.pattern(), name));

        Map<Integer, File> mediumIdFileMap = new HashMap<>();

        if (files == null) {
            System.err.println("could not list files for: " + file);
            return mediumIdFileMap;
        }
        for (File bookFile : files) {
            Matcher matcher = pattern.matcher(bookFile.getName());

            if (!matcher.matches()) {
                continue;
            }
            String mediumIdString = matcher.group(getMediumContainerPatternGroup());
            int mediumId;

            try {
                mediumId = Integer.parseInt(mediumIdString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                continue;
            }
            mediumIdFileMap.put(mediumId, bookFile);
        }
        return mediumIdFileMap;
    }

    abstract Pattern getMediumContainerPattern();

    abstract int getMediumContainerPatternGroup();

    public void removeMedia(int id) {
        if (this.contentDir != null) {
            String internalFile = getItemPath(id, this.contentDir);

            if (internalFile != null && !new File(internalFile).delete()) {
                System.err.println("could not delete file: " + internalFile);
            }
        }
    }

    public void removeAll() {
        final File[] files = this.contentDir.listFiles();
        if (files == null) {
            System.err.println("could not list files of: " + this.contentDir);
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                this.deleteDir(file);
            }
            if (file.exists() && !file.delete()) {
                System.err.println("could not delete file: " + file.getAbsolutePath());
            }
        }
    }

    public long getEpisodeSize(File value, int episodeId, Map<Integer, String> episodePaths) {
        return this.getEpisodeSize(value, episodeId);
    }

    public abstract long getEpisodeSize(File value, int episodeId);

    public abstract double getAverageEpisodeSize(int mediumId);

    boolean writeable() {
        return contentDir != null && contentDir.getUsableSpace() >= minByteSpaceAvailable();
    }

    private long minByteSpaceAvailable() {
        return MIN_MB_SPACE_AVAILABLE * 1024L * 1024L;
    }

    boolean notWriteable(long toWriteBytes) {
        return this.contentDir != null && (this.contentDir.getUsableSpace() - toWriteBytes) >= minByteSpaceAvailable();
    }

    boolean notWriteable(File file, long toWriteBytes) {
        return file == null || (file.getUsableSpace() - toWriteBytes) < minByteSpaceAvailable();
    }

    private void deleteDir(File file) {
        final File[] files = file.listFiles();
        if (files == null) {
            System.err.println("could not list files of: " + file);
            return;
        }
        for (File content : files) {
            if (content.isDirectory()) {
                this.deleteDir(content);
            }
            if (content.exists() && !content.delete()) {
                break;
            }
        }
    }
}
