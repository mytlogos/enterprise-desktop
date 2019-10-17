package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.RepositoryProvider;
import com.mytlogos.enterprisedesktop.background.sqlite.PagedList;
import com.mytlogos.enterprisedesktop.model.DisplayEpisode;
import com.mytlogos.enterprisedesktop.model.DisplayRelease;
import com.mytlogos.enterprisedesktop.model.MediaList;
import com.mytlogos.enterprisedesktop.model.Medium;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
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
    private Tab episodeTab;
    @FXML
    private Tab listsTab;
    @FXML
    private Tab statisticsTab;
    @FXML
    private Tab externalUserTab;
    @FXML
    private TabPane tabPane;
    @FXML
    private ListView<DisplayRelease> episodes;
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
    private Disposable episodeDisposable;

    public void initialize() {
        this.readFilter.setConverter(new DisplayConverter<>(ReadFilter.values()));
        this.savedFilter.setConverter(new DisplayConverter<>(SavedFilter.values()));
        this.minEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));
        this.maxEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));
        this.taskController = new TaskController();
        this.tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            detachTab(oldValue);
            attachTab(newValue);
        });
        this.attachTab(this.tabPane.getSelectionModel().getSelectedItem());
        this.episodes.setCellFactory(param -> new DisplayReleaseCell());
    }

    private void detachTab(Tab oldValue) {
        System.out.println("old tab: " + oldValue);
        if (oldValue == this.episodeTab) {
            this.detachEpisodeTab();
        } else if (oldValue == this.listsTab) {
            this.detachListsTab();
        } else if (oldValue == this.externalUserTab) {
            this.detachExternalUserTab();
        } else if (oldValue == this.statisticsTab) {
            this.detachStatisticsTab();
        }
    }

    private void attachTab(Tab newValue) {
        System.out.println("new tab: " + newValue);

        if (newValue == this.episodeTab) {
            this.attachEpisodeTab();
        } else if (newValue == this.listsTab) {
            this.attachListsTab();
        } else if (newValue == this.externalUserTab) {
            this.attachExternalUserTab();
        } else if (newValue == this.statisticsTab) {
            this.attachStatisticsTab();
        }
    }

    private void detachEpisodeTab() {
        if (this.episodeDisposable != null) {
            this.episodeDisposable.dispose();
        }
    }

    private void detachListsTab() {

    }

    private void detachExternalUserTab() {

    }

    private void detachStatisticsTab() {

    }

    private void attachEpisodeTab() {
        final Repository repository = new RepositoryProvider().provide();
        final Observable<PagedList<DisplayRelease>> observable = repository.getDisplayEpisodes(-1, 0, -1, -1, -1, false);

        this.episodeDisposable = observable
                .subscribeOn(JavaFxScheduler.platform())
                .subscribe(displayReleases -> this.episodes.getItems().setAll(displayReleases), Throwable::printStackTrace);
    }

    private void attachListsTab() {

    }

    private void attachExternalUserTab() {

    }

    private void attachStatisticsTab() {

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

    private class DisplayReleaseCell extends ListCell<DisplayRelease> {

        @Override
        protected void updateItem(DisplayRelease item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.getMediumTitle() + " #" + item.getTotalIndex() + "." + item.getPartialIndex());
            }
        }
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
