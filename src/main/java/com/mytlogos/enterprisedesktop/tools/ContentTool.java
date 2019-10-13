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
    final File internalContentDir;
    final File externalContentDir;
    final int minMBSpaceAvailable = 150;


    ContentTool(File internalContentDir, File externalContentDir) {
        this.internalContentDir = internalContentDir;
        this.externalContentDir = externalContentDir;
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

        if (externalContentDir != null) {
            books.addAll(this.getMediaPaths(externalContentDir));
        }
        if (internalContentDir != null) {
            books.addAll(this.getMediaPaths(internalContentDir));
        }
        return books;
    }

    private List<String> getMediaPaths(File dir) {
        List<String> imageMedia = new ArrayList<>();
        for (File file : dir.listFiles()) {
            if (isContentMedium(file)) {
                imageMedia.add(file.getAbsolutePath());
            }
        }
        return imageMedia;
    }

    abstract boolean isContentMedium(File file);

    public abstract int getMedium();

    public void removeMediaEpisodes(int mediumId, Set<Integer> episodeIds) {
        if (externalContentDir != null) {
            String externalFile = getItemPath(mediumId, externalContentDir);

            if (externalFile != null) {
                this.removeMediaEpisodes(episodeIds, externalFile);
            }
        }
        if (internalContentDir != null) {
            String internalFile = getItemPath(mediumId, internalContentDir);

            if (internalFile != null) {
                this.removeMediaEpisodes(episodeIds, internalFile);
            }
        }
    }

    abstract String getItemPath(int mediumId, File dir);

    abstract void removeMediaEpisodes(Set<Integer> episodeIds, String internalFile);

    public abstract Map<Integer, String> getEpisodePaths(String mediumPath);

    public String getItemPath(int mediumId) {
        String bookZipFile = null;

        if (externalContentDir != null) {
            bookZipFile = getItemPath(mediumId, externalContentDir);
        }

        if (bookZipFile == null && internalContentDir != null) {
            bookZipFile = getItemPath(mediumId, internalContentDir);
        }

        if (bookZipFile == null) {
            return "";
        }
        return bookZipFile;
    }

    public abstract void saveContent(Collection<ClientDownloadedEpisode> episode, int mediumId) throws IOException;

    public void mergeIfNecessary() {
        if (!this.isSupported()) {
            return;
        }
        if (this.writeInternal() && !this.writeExternal()) {
            this.mergeExternalAndInternalMedia(false);
        } else if (!this.writeInternal() && this.writeExternal()) {
            this.mergeExternalAndInternalMedia(true);
        }
    }

    public abstract boolean isSupported();

    synchronized public void mergeExternalAndInternalMedia(boolean toExternal) {
        Map<Integer, File> internalContainers = this.getItemContainers(false);
        Map<Integer, File> externalContainers = this.getItemContainers(true);

        Map<Integer, File> sourceContainers = toExternal ? internalContainers : externalContainers;
        Map<Integer, File> toContainers = toExternal ? externalContainers : internalContainers;

        File toParent = toExternal ? externalContentDir : internalContentDir;

        for (Map.Entry<Integer, File> entry : sourceContainers.entrySet()) {
            File file = toContainers.get(entry.getKey());
            this.mergeExternalAndInternalMedium(toExternal, entry.getValue(), file, toParent, entry.getKey());
        }
    }

    public Map<Integer, File> getItemContainers(boolean externalSpace) {
        File file = externalSpace ? externalContentDir : internalContentDir;

        if (file == null) {
            return new HashMap<>();
        }
        Pattern pattern = getMediumContainerPattern();
        File[] files = file.listFiles((dir, name) -> Pattern.matches(pattern.pattern(), name));

        Map<Integer, File> mediumIdFileMap = new HashMap<>();

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

    abstract void mergeExternalAndInternalMedium(boolean toExternal, File source, File goal, File toParent, Integer mediumId);

    abstract Pattern getMediumContainerPattern();

    abstract int getMediumContainerPatternGroup();

    public void removeMedia(int id) {
        if (externalContentDir != null) {
            String externalFile = getItemPath(id, externalContentDir);

            if (externalFile != null && !new File(externalFile).delete()) {
                System.err.println("could not delete file: " + externalFile);
            }
        }
        if (internalContentDir != null) {
            String internalFile = getItemPath(id, internalContentDir);

            if (internalFile != null && !new File(internalFile).delete()) {
                System.err.println("could not delete file: " + internalFile);
            }
        }
    }

    public void removeAll() {
        for (File file : this.internalContentDir.listFiles()) {
            if (file.isDirectory()) {
                this.deleteDir(file);
            }
            if (file.exists() && !file.delete()) {
                System.err.println("could not delete file: " + file.getAbsolutePath());
            }
        }
        for (File file : this.externalContentDir.listFiles()) {
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
        return this.writeExternal() || this.writeInternal();
    }

    boolean writeExternal() {
        return externalContentDir != null && externalContentDir.getUsableSpace() >= minByteSpaceAvailable();
    }

    boolean writeInternal() {
        return internalContentDir != null && internalContentDir.getUsableSpace() >= minByteSpaceAvailable();
    }

    private long minByteSpaceAvailable() {
        return minMBSpaceAvailable * 1024 * 1024;
    }

    boolean writeable(long toWriteBytes) {
        return writeExternal(toWriteBytes) || writeInternal(toWriteBytes);
    }

    boolean writeExternal(long toWriteBytes) {
        return externalContentDir != null && (externalContentDir.getUsableSpace() - toWriteBytes) >= minByteSpaceAvailable();
    }

    boolean writeInternal(long toWriteBytes) {
        return internalContentDir != null && (internalContentDir.getUsableSpace() - toWriteBytes) >= minByteSpaceAvailable();
    }

    boolean writeable(File file, long toWriteBytes) {
        return file != null && (file.getUsableSpace() - toWriteBytes) >= minByteSpaceAvailable();
    }

    private void deleteDir(File file) {
        for (File content : file.listFiles()) {
            if (content.isDirectory()) {
                this.deleteDir(content);
            }
            if (content.exists() && !content.delete()) {
                break;
            }
        }
    }
}
