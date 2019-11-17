package com.mytlogos.enterprisedesktop.preferences;

import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.mytlogos.enterprisedesktop.model.MediumType;
import javafx.beans.property.*;

import java.io.File;
import java.nio.file.FileSystems;

/**
 *
 */
public class DownloadPreferences extends CategoryPreference {

    private SimpleObjectProperty<File> defaultFile = new SimpleObjectProperty<>(FileSystems.getDefault().getPath("medium").toFile());
    private ObjectProperty<File> textFile = new SimpleObjectProperty<>(FileSystems.getDefault().getPath("medium", "text").toFile());
    private ObjectProperty<File> videoFile = new SimpleObjectProperty<>(FileSystems.getDefault().getPath("medium", "video").toFile());
    private ObjectProperty<File> audioFile = new SimpleObjectProperty<>(FileSystems.getDefault().getPath("medium", "audio").toFile());
    private ObjectProperty<File> imageFile = new SimpleObjectProperty<>(FileSystems.getDefault().getPath("medium", "image").toFile());
    private BooleanProperty autoDownload = new SimpleBooleanProperty(true);
    private IntegerProperty defaultSizeLimit = new SimpleIntegerProperty(4000);
    private IntegerProperty videoSizeLimit = new SimpleIntegerProperty(1000);
    private IntegerProperty textSizeLimit = new SimpleIntegerProperty(1000);
    private IntegerProperty audioSizeLimit = new SimpleIntegerProperty(1000);
    private IntegerProperty imageSizeLimit = new SimpleIntegerProperty(1000);

    public File getDefaultFile() {
        return defaultFile.get();
    }

    public SimpleObjectProperty<File> defaultFileProperty() {
        return defaultFile;
    }

    public File getTextFile() {
        return textFile.get();
    }

    public ObjectProperty<File> textFileProperty() {
        return textFile;
    }

    public File getVideoFile() {
        return videoFile.get();
    }

    public ObjectProperty<File> videoFileProperty() {
        return videoFile;
    }

    public File getAudioFile() {
        return audioFile.get();
    }

    public ObjectProperty<File> audioFileProperty() {
        return audioFile;
    }

    public File getImageFile() {
        return imageFile.get();
    }

    public ObjectProperty<File> imageFileProperty() {
        return imageFile;
    }

    public boolean isAutoDownload() {
        return autoDownload.get();
    }

    public BooleanProperty autoDownloadProperty() {
        return autoDownload;
    }

    public int getDefaultSizeLimit() {
        return defaultSizeLimit.get();
    }

    public IntegerProperty defaultSizeLimitProperty() {
        return defaultSizeLimit;
    }

    public IntegerProperty videoSizeLimitProperty() {
        return videoSizeLimit;
    }

    public IntegerProperty textSizeLimitProperty() {
        return textSizeLimit;
    }

    public IntegerProperty audioSizeLimitProperty() {
        return audioSizeLimit;
    }

    public IntegerProperty imageSizeLimitProperty() {
        return imageSizeLimit;
    }

    public int getDownloadLimitCount(int medium) {
        if (MediumType.is(medium, MediumType.VIDEO)) {
            return -1;
        } else if (MediumType.is(medium, MediumType.IMAGE)) {
            return -1;
        } else if (MediumType.is(medium, MediumType.AUDIO)) {
            return -1;
        } else if (MediumType.is(medium, MediumType.TEXT)) {
            return -1;
        } else {
            throw new IllegalArgumentException("Unknown Medium");
        }
    }

    public int getDownloadLimitSize(int type) {
        if (MediumType.is(type, MediumType.VIDEO)) {
            return this.getVideoSizeLimit();
        } else if (MediumType.is(type, MediumType.IMAGE)) {
            return this.getImageSizeLimit();
        } else if (MediumType.is(type, MediumType.AUDIO)) {
            return this.getAudioSizeLimit();
        } else if (MediumType.is(type, MediumType.TEXT)) {
            return this.getTextSizeLimit();
        } else {
            throw new IllegalArgumentException("Unknown Medium");
        }
    }

    public int getVideoSizeLimit() {
        return videoSizeLimit.get();
    }

    public int getImageSizeLimit() {
        return imageSizeLimit.get();
    }

    public int getAudioSizeLimit() {
        return audioSizeLimit.get();
    }

    public int getTextSizeLimit() {
        return textSizeLimit.get();
    }

    public int getMediumDownloadLimitSize(int id) {
        return -1;
    }

    @Override
    Category getCategory() {
        return Category.of(
                "Download",
                Group.of(
                        "Location",
                        Setting.of("Default Location", this.defaultFile, true),
                        Setting.of("Text Location", this.textFile, true),
                        Setting.of("Video Location", this.videoFile, true),
                        Setting.of("Image Location", this.imageFile, true),
                        Setting.of("Audio Location", this.audioFile, true)
                ),
                Group.of(
                        "Auto Download",
                        Setting.of("Enabled", this.autoDownload)
                ),
                Group.of(
                        "Size Limit",
                        Setting.of("Default", this.defaultSizeLimit),
                        Setting.of("Text", this.textSizeLimit),
                        Setting.of("Image", this.imageSizeLimit),
                        Setting.of("Video", this.videoSizeLimit),
                        Setting.of("Audio", this.audioSizeLimit)
                )
        );
    }
}
