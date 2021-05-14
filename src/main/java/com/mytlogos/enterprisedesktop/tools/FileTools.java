package com.mytlogos.enterprisedesktop.tools;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.model.MediumType;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileTools {
    private FileTools() {
        throw new IllegalAccessError("Do not instantiate FileTools!");
    }

    /**
     * Copied from
     * <a href="https://stackoverflow.com/a/3758880/9492864">
     * How to convert byte size into human readable format in java?
     * </a>
     */
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static TextContentTool getTextContentTool() {
        return new TextContentTool(getBookDir());
    }

    public static AudioContentTool getAudioContentTool() {
        return new AudioContentTool(getAudioDir());
    }

    public static VideoContentTool getVideoContentTool() {
        return new VideoContentTool(getVideoDir());
    }

    public static ImageContentTool getImageContentTool() {
        return new ImageContentTool(getImageDir());
    }

    public static boolean isImageContentSupported() {
        return new ImageContentTool(getImageDir()).isSupported();
    }

    public static boolean isVideoContentSupported() {
        return new VideoContentTool(getVideoDir()).isSupported();
    }

    public static boolean isTextContentSupported() {
        return new TextContentTool(getBookDir()).isSupported();
    }

    public static boolean isAudioContentSupported() {
        return new AudioContentTool(getAudioDir()).isSupported();
    }

    public static File getBookDir() {
        return createDir(ApplicationConfig.getMainPreferences().getDownloadPreferences().getTextFile());
    }


    public static File getAudioDir() {
        return createDir(ApplicationConfig.getMainPreferences().getDownloadPreferences().getAudioFile());
    }

    public static File getImageDir() {
        return createDir(ApplicationConfig.getMainPreferences().getDownloadPreferences().getImageFile());
    }

    public static File getVideoDir() {
        return createDir(ApplicationConfig.getMainPreferences().getDownloadPreferences().getVideoFile());
    }

    private static File createDir(File filesDir) {
        if (filesDir == null) {
            return null;
        }

        if (!filesDir.exists()) {
            if (filesDir.mkdirs()) {
                return filesDir;
            } else {
                return null;
            }
        }
        return filesDir;
    }

    public static Set<ContentTool> getSupportedContentTools() {
        Set<ContentTool> tools = new HashSet<>();
        ContentTool tool = FileTools.getImageContentTool();

        if (tool.isSupported()) {
            tools.add(tool);
        }
        tool = FileTools.getAudioContentTool();

        if (tool.isSupported()) {
            tools.add(tool);
        }
        tool = FileTools.getTextContentTool();

        if (tool.isSupported()) {
            tools.add(tool);
        }
        tool = FileTools.getVideoContentTool();

        if (tool.isSupported()) {
            tools.add(tool);
        }
        return tools;
    }

    public static ContentTool getContentTool(int mediumType) {
        if (MediumType.is(MediumType.TEXT, mediumType)) {
            return FileTools.getTextContentTool();

        } else if (MediumType.is(MediumType.IMAGE, mediumType)) {
            return FileTools.getImageContentTool();

        } else if (MediumType.is(MediumType.VIDEO, mediumType)) {
            return FileTools.getVideoContentTool();

        } else if (MediumType.is(MediumType.AUDIO, mediumType)) {
            return FileTools.getAudioContentTool();

        } else {
            throw new IllegalArgumentException("invalid medium type: " + mediumType);
        }
    }
}
