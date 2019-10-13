package com.mytlogos.enterprisedesktop.tools;

import com.mytlogos.enterprisedesktop.background.RepositoryProvider;
import com.mytlogos.enterprisedesktop.model.MediumType;
import javafx.application.Application;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileTools {
    private static final int minMBSpaceAvailable = 150;


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
        return new TextContentTool(getInternalBookDir(), getExternalBookDir());
    }

    public static AudioContentTool getAudioContentTool() {
        return new AudioContentTool(getInternalAudioDir(), getExternalAudioDir());
    }

    public static VideoContentTool getVideoContentTool() {
        return new VideoContentTool(getInternalVideoDir(), getExternalVideoDir());
    }

    public static ImageContentTool getImageContentTool() {
        return new ImageContentTool(null, null, new RepositoryProvider().provide());
    }

    public static boolean isImageContentSupported() {
        return new ImageContentTool(null, null, null).isSupported();
    }

    public static boolean isVideoContentSupported() {
        return new ImageContentTool(null, null, null).isSupported();
    }

    public static boolean isTextContentSupported() {
        return new ImageContentTool(null, null, null).isSupported();
    }

    public static boolean isAudioContentSupported() {
        return new ImageContentTool(null, null, null).isSupported();
    }


    public static File getExternalBookDir() {
        return createBookDirectory(null);
    }


    public static File getInternalBookDir() {
        return createBookDirectory(null);
    }


    public static File getInternalAudioDir() {
        return createAudioDirectory(getInternalAppDir());
    }


    public static File getExternalAudioDir() {
        File dir = getExternalAppDir();
        if (dir == null) {
            return null;
        }
        return createAudioDirectory(dir);
    }


    public static File getExternalImageDir() {
        File dir = getExternalAppDir();
        if (dir == null) {
            return null;
        }
        return createImageDirectory(dir);
    }


    public static File getInternalImageDir() {
        return createImageDirectory(getInternalAppDir());
    }


    public static File getExternalVideoDir() {
        File dir = getExternalAppDir();
        if (dir == null) {
            return null;
        }
        return createVideoDirectory(dir);
    }


    public static File getInternalVideoDir() {
        return createVideoDirectory(getInternalAppDir());
    }


    public static File getExternalAppDir() {
        throw new UnsupportedOperationException();
    }


    public static File getInternalAppDir() {
        return null;
    }

    private static File createBookDirectory(File filesDir) {
        return createDir(filesDir, "Enterprise Books");
    }

    private static File createAudioDirectory(File filesDir) {
        return createDir(filesDir, "Enterprise Audios");
    }

    private static File createVideoDirectory(File filesDir) {
        return createDir(filesDir, "Enterprise Videos");
    }

    private static File createImageDirectory(File filesDir) {
        return createDir(filesDir, "Enterprise Images");
    }

    private static File createDir(File filesDir, String name) {
        if (filesDir == null) {
            return null;
        }
        File file = new File(filesDir, name);

        if (!file.exists()) {
            // TODO: 13.08.2019 cannot create dir on external storage
            if (file.mkdirs()) {
                return file;
            } else {
                return null;
            }
        }

        return file;
    }

    public static Set<ContentTool> getSupportedContentTools(Application application) {
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

    public static boolean writeInternal(Application application) {
        return isWriteable(application, getInternalAppDir());
    }

    public static boolean writeExternal(Application application) {
        return isWriteable(application, getExternalAppDir());
    }

    public static boolean writable(Application application) {
        return FileTools.writeExternal(application) || FileTools.writeInternal(application);
    }

    private static boolean isWriteable(Application application, File dir) {
        return dir != null && getFreeMBSpace(dir) >= minMBSpaceAvailable;
    }


    public static long getFreeMBSpace(File file) {
        return file.getUsableSpace() / (1024L * 1024L);
    }
}
