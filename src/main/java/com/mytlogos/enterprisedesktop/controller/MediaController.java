package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.sqlite.PagedList;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.background.sqlite.life.Observer;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.BiConsumerEx;
import com.mytlogos.enterprisedesktop.tools.Log;
import com.mytlogos.enterprisedesktop.tools.TriConsumerEx;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Notifications;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class MediaController implements Attachable {
    private final ObjectProperty<SavedFilter> savedFilterObjectProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<ReadFilter> readFilterObjectProperty = new SimpleObjectProperty<>();
    private final TextFormatter<Double> scrollToEpisodeFormatter = ControllerUtils.doubleTextFormatter();
    @FXML
    private StackPane mediaLoadPane;
    @FXML
    private StackPane mediumLoadPane;
    @FXML
    private VBox mediumDisplay;
    @FXML
    private RadioButton readOnly;
    @FXML
    private ToggleGroup readFilter;
    @FXML
    private RadioButton notReadOnly;
    @FXML
    private RadioButton ignoreRead;
    @FXML
    private RadioButton savedOnly;
    @FXML
    private ToggleGroup savedFilter;
    @FXML
    private RadioButton notSavedOnly;
    @FXML
    private RadioButton ignoreSaved;
    @FXML
    private ScrollPane detailPane;
    @FXML
    private ListView<TocEpisode> mediumContentView;
    private final Observer<PagedList<TocEpisode>> episodesObserver = episodes -> {
        if (episodes != null) {
            this.mediumContentView.getItems().setAll(episodes);
        }
    };
    @FXML
    private TextField scrollToEpisodeField;
    @FXML
    private ToggleButton episodeAscendingBtn;
    @FXML
    private ChoiceBox<EpisodeSorting> episodeSorting;
    @FXML
    private ListView<Medium> mediaView;
    private final Observer<List<MediumItem>> mediaItemsObserver = mediumItems -> {
        this.mediumContentView.getItems().clear();
        if (mediumItems != null) {
            this.mediaLoadPane.setVisible(false);
            this.mediaView.getItems().setAll(mediumItems);
        }
    };
    @FXML
    private ToggleButton mediumAscendingBtn;
    @FXML
    private ChoiceBox<MediumSorting> mediumSorting;
    @FXML
    private CheckBox showAudioBox;
    @FXML
    private CheckBox showVideoBox;
    @FXML
    private CheckBox showImageBox;
    @FXML
    private CheckBox showTextBox;
    @FXML
    private Spinner<Integer> maxEpisodeIndex;
    @FXML
    private Spinner<Integer> minEpisodeIndex;
    @FXML
    private TextField nameFilter;
    @FXML
    private MediumDisplayController mediumDisplayController;
    private final Observer<MediumSetting> settingObserver = mediumSetting -> this.mediumDisplayController.setMedium(mediumSetting);
    private ObjectBinding<EpisodeFilter> episodeFilterBinding;
    private LiveData<PagedList<TocEpisode>> episodesLiveData;
    private LiveData<MediumSetting> mediumSettingLiveData;
    private final InvalidationListener mediumListener = observable -> selectMedium();
    private LiveData<List<MediumItem>> mediaItems;

    public void initialize() {
        this.mediaView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.mediumContentView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.setMediaViewContextMenu();
        this.setEpisodeViewContextMenu();

        this.mediaView.setCellFactory(param -> new MediumCell());
        this.mediumSorting.setConverter(new MainController.DisplayConverter<>(MediumSorting.values()));
        this.mediumSorting.getItems().setAll(MediumSorting.values());
        this.mediumSorting.getSelectionModel().selectFirst();

        Image lockedImage = new Image("/ic_lock_dark.png", true);
        Image readImage = new Image("/ic_episode_read_icon.png", true);
        Image onlineImage = new Image("/ic_open_browser_icon.png", true);
        Image localImage = new Image("/ic_open_local_icon.png", true);

        this.mediumContentView.setCellFactory(param -> new TocEpisodeCell(lockedImage, readImage, onlineImage, localImage));

        this.scrollToEpisodeField.setTextFormatter(this.scrollToEpisodeFormatter);
        this.minEpisodeIndex.setTooltip(new Tooltip("Minimum Episode Index\n-1 or leave it empty to ignore"));
        this.maxEpisodeIndex.setTooltip(new Tooltip("Maximum Episode Index\n-1 or leave it empty to ignore"));
        this.minEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));
        this.maxEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));

        this.episodeSorting.setConverter(new MainController.DisplayConverter<>(EpisodeSorting.values()));
        this.episodeSorting.getItems().setAll(EpisodeSorting.values());
        this.episodeSorting.getSelectionModel().selectFirst();

        this.readFilter.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == readOnly) {
                this.readFilterObjectProperty.set(ReadFilter.READ_ONLY);
            } else if (newValue == notReadOnly) {
                this.readFilterObjectProperty.set(ReadFilter.UNREAD_ONLY);
            } else {
                this.readFilterObjectProperty.set(ReadFilter.IGNORE);
            }
        });
        this.savedFilter.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == savedOnly) {
                this.savedFilterObjectProperty.set(SavedFilter.SAVED_ONLY);
            } else if (newValue == notSavedOnly) {
                this.savedFilterObjectProperty.set(SavedFilter.UNSAVED_ONLY);
            } else {
                this.savedFilterObjectProperty.set(SavedFilter.IGNORE);
            }
        });
        this.episodeFilterBinding = Bindings.createObjectBinding(
                () -> new EpisodeFilter(this.readFilterObjectProperty.getValue(), this.savedFilterObjectProperty.getValue()),
                this.readFilterObjectProperty,
                this.savedFilterObjectProperty
        );
    }

    private void setMediaViewContextMenu() {

    }

    private void setEpisodeViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem setReadItem = new MenuItem();
        setReadItem.setText("Set Read");
        setReadItem.setOnAction(event -> doEpisodeRepoAction(
                "Set Read",
                (repository, mediumId) -> repository.updateAllRead(mediumId, true),
                (repository, ids, mediumId) -> repository.updateRead(mediumId, true))
        );

        MenuItem setUnreadItem = new MenuItem();
        setUnreadItem.setText("Set Unread");
        setUnreadItem.setOnAction(event -> doEpisodeRepoAction(
                "Set Unread",
                (repository, mediumId) -> repository.updateAllRead(mediumId, false),
                (repository, ids, mediumId) -> repository.updateRead(mediumId, false))
        );

        MenuItem downloadItem = new MenuItem();
        downloadItem.setText("Download");
        downloadItem.setOnAction(event -> doEpisodeRepoAction("Download", Repository::downloadAll, Repository::download));

        MenuItem deleteItem = new MenuItem();
        deleteItem.setText("Delete local");
        deleteItem.setOnAction(event -> doEpisodeRepoAction("Delete local", Repository::deleteAllLocalEpisodes, Repository::deleteLocalEpisodes));

        MenuItem reloadItem = new MenuItem();
        reloadItem.setText("Reload");
        reloadItem.setOnAction(event -> doEpisodeRepoAction(
                "Reload",
                Repository::reloadAll,
                (repository, ids, mediumId) -> repository.reload(ids)
        ));

        contextMenu.getItems().addAll(setReadItem, setUnreadItem, downloadItem, deleteItem, reloadItem);
        this.mediumContentView.setContextMenu(contextMenu);
    }

    private void doEpisodeRepoAction(String description, BiConsumerEx<Repository, Integer> allConsumer, TriConsumerEx<Repository, Set<Integer>, Integer> idsConsumer) {
        final Repository repository = ApplicationConfig.getRepository();
        List<TocEpisode> selectedItems = this.mediumContentView.getSelectionModel().getSelectedItems();

        final int mediumId = this.mediaView.getSelectionModel().getSelectedItem().getMediumId();
        CompletableFuture.runAsync(() -> {
            if (selectedItems.size() == this.mediumContentView.getItems().size()) {
                try {
                    allConsumer.accept(repository, mediumId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                Set<Integer> ids = new HashSet<>();
                for (TocEpisode item : selectedItems) {
                    ids.add(item.getEpisodeId());
                }
                try {
                    idsConsumer.accept(repository, ids, mediumId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).whenComplete((aVoid, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
            Platform.runLater(() -> Notifications
                    .create()
                    .title(description + (throwable == null ? "failed" : " succeeded"))
                    .show()
            );
        });
    }

    @Override
    public void onAttach() {
        if (this.episodeFilterBinding == null) {
            Log.severe("onAttach called before initialize");
            return;
        }
        this.mediaLoadPane.setVisible(true);
        final LiveData<Repository> repositorySingle = ApplicationConfig.getLiveDataRepository();

        this.mediaItems = repositorySingle.flatMap(repository -> repository.getAllMedia(this.mediumSorting.getValue()));
        this.mediaItems.observe(this.mediaItemsObserver);

        // first remove, then add to ensure that it is registered only once
        this.mediaView.getSelectionModel().selectedItemProperty().removeListener(this.mediumListener);
        this.mediaView.getSelectionModel().selectedItemProperty().addListener(this.mediumListener);
    }

    @Override
    public void onDetach() {
        if (this.episodesLiveData != null) {
            this.episodesLiveData.removeObserver(this.episodesObserver);
        }
        if (this.mediumSettingLiveData != null) {
            this.mediumSettingLiveData.removeObserver(this.settingObserver);
        }
        if (this.mediaItems != null) {
            this.mediaItems.removeObserver(this.mediaItemsObserver);
        }
        this.mediaView.getSelectionModel().selectedItemProperty().removeListener(this.mediumListener);
    }

    private void selectMedium() {
        LiveData<Repository> repositorySingle = ApplicationConfig.getLiveDataRepository();
        final Medium selectedItem = this.mediaView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        this.mediumLoadPane.setVisible(false);

        if (this.episodesLiveData != null) {
            this.episodesLiveData.removeObserver(this.episodesObserver);
        }
        if (this.mediumSettingLiveData != null) {
            this.mediumSettingLiveData.removeObserver(this.settingObserver);
        }
        this.episodesLiveData = ControllerUtils.combineLatest(
                repositorySingle,
                this.episodeFilterBinding,
                this.episodeSorting.getSelectionModel().selectedItemProperty(),
                (repository, episodeFilter, episodeSorting) -> {
                    System.out.println("fetching new episodes");
                    return repository.getToc(
                            selectedItem.getMediumId(),
                            episodeSorting,
                            episodeFilter.readFilter,
                            episodeFilter.savedFilter
                    );
                }
        ).flatMap(pagedListLiveData -> pagedListLiveData);
        this.episodesLiveData.observe(this.episodesObserver);

        this.mediumSettingLiveData = repositorySingle.flatMap(repository -> {
            final int mediumId = selectedItem.getMediumId();
            return repository.getMediumSettings(mediumId);
        });
        this.mediumSettingLiveData.observe(this.settingObserver);
    }

    @FXML
    private void scrollToEpisode() {

    }

    private static class MediumCell extends ListCell<Medium> {
        MediumCell() {

        }

        @Override
        protected void updateItem(Medium item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.getTitle());
            }
        }
    }

    private class MediumFilter {
        public MediumFilter(int medium, int minEpisodeIndex, int maxEpisodeIndex) {

        }
    }
}
