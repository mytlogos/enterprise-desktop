package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.model.DisplayEpisode;
import com.mytlogos.enterprisedesktop.model.MediaList;
import com.mytlogos.enterprisedesktop.model.Medium;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Objects;

/**
 *
 */
public class MainController {
    @FXML
    private ListView<DisplayEpisode> episodes;
    @FXML
    private ChoiceBox<ReadFilter> readFilter;
    @FXML
    private ChoiceBox<SavedFilter> savedFilter;
    @FXML
    private Spinner<Integer> minEpisodeIndex;
    @FXML
    private Spinner<Integer> maxEpisodeIndex;
    @FXML
    private CheckBox showTextBox;
    @FXML
    private CheckBox showImageBox;
    @FXML
    private CheckBox showVideoBox;
    @FXML
    private CheckBox showAudioBox;
    @FXML
    private CheckBox latestOnly;
    @FXML
    private ListView<MediaList> lists;
    @FXML
    private ListView<Medium> listMedia;
    @FXML
    private ListView<DisplayEpisode> mediumContent;
    @FXML
    private Text infoText;
    private TaskController taskController;

    public void initialize() {
        this.readFilter.setConverter(new DisplayConverter<>(ReadFilter.values()));
        this.savedFilter.setConverter(new DisplayConverter<>(SavedFilter.values()));
        this.minEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));
        this.maxEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));
        this.taskController = new TaskController();
    }

    private enum ReadFilter implements DisplayValue {
        IGNORE("Ignore Read Filter"),
        READ_ONLY("Read only"),
        UNREAD_ONLY("Unread only");

        private final String display;

        ReadFilter(String display) {
            this.display = display;
        }

        @Override
        public String getDisplayValue() {
            return this.display;
        }
    }


    private enum SavedFilter implements DisplayValue {
        IGNORE("Ignore Saved Filter"),
        SAVED_ONLY("Saved only"),
        UNSAVED_ONLY("Unsaved only");

        private final String display;

        SavedFilter(String display) {
            this.display = display;
        }

        @Override
        public String getDisplayValue() {
            return this.display;
        }
    }

    public interface DisplayValue {
        String getDisplayValue();
    }

    public class DisplayConverter<T extends DisplayValue> extends StringConverter<T> {
        private final T[] values;

        public DisplayConverter(List<T> values) {
            //noinspection unchecked
            this.values = (T[]) values.toArray();
        }

        public DisplayConverter(T[] values) {
            this.values = values;
        }

        @Override
        public String toString(T object) {
            return object.getDisplayValue();
        }

        @Override
        public T fromString(String string) {
            for (T value : this.values) {
                if (Objects.equals(value.getDisplayValue(), string)) {
                    return value;
                }
            }
            return null;
        }
    }
}
