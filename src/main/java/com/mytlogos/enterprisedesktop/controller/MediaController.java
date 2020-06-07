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
import com.mytlogos.enterprisedesktop.tools.Utils;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.Notifications;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class MediaController implements Attachable {
    private final ObjectProperty<SavedFilter> savedFilterProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<ReadFilter> readFilterProperty = new SimpleObjectProperty<>();
    private final TextFormatter<Double> scrollToEpisodeFormatter = ControllerUtils.doubleTextFormatter();
    private final ObservableList<MediumItem> observableMediaItems = FXCollections.observableArrayList();
    private final Observer<List<MediaList>> listObserver = Utils.emptyObserver();
    private final ObjectProperty<List<Integer>> listItemsProperty = new SimpleObjectProperty<>();
    private final Observer<List<Integer>> listItemsObserver = this.listItemsProperty::set;
    private ObjectBinding<MediumFilter> mediumFilter;
    @FXML
    private TextField listFilter;
    @FXML
    private ToggleButton ignoreLists;
    @FXML
    private ListView<MediaList> listFilterView;
    @FXML
    private StackPane mediaLoadPane;
    @FXML
    private StackPane mediumLoadPane;
    @FXML
    private VBox mediumDisplay;
    @FXML
    private HBox readFilterState;
    @FXML
    private ThreeStatesController readFilterStateController;
    @FXML
    private HBox savedFilterState;
    @FXML
    private ThreeStatesController savedFilterStateController;
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
    private HBox showMedium;
    @FXML
    private MediumTypes showMediumController;
    @FXML
    private TextField scrollToEpisodeField;
    @FXML
    private ToggleButton episodeAscendingBtn;
    @FXML
    private ChoiceBox<EpisodeSorting> episodeSorting;
    @FXML
    private ListView<MediumItem> mediaView;
    private final Observer<List<MediumItem>> mediaItemsObserver = mediumItems -> {
        if (mediumItems != null) {
            final MultipleSelectionModel<MediumItem> selectionModel = this.mediaView.getSelectionModel();
            final MediumItem selectedItem = selectionModel.getSelectedItem();

            this.mediaLoadPane.setVisible(false);
            ControllerUtils.setItems(selectionModel, this.observableMediaItems, mediumItems);

            if (!Objects.equals(selectedItem, selectionModel.getSelectedItem())) {
                this.mediumContentView.getItems().clear();
            }
        }
    };
    @FXML
    private ToggleButton mediumAscendingBtn;
    @FXML
    private ChoiceBox<MediumSorting> mediumSorting;
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
    private LiveData<List<Integer>> listItemsLiveData;
    private LiveData<List<MediaList>> listLiveData;

    public void initialize() {
        this.showMediumController.setMedium(0);
        this.mediumFilter = Bindings.createObjectBinding(() -> new MediumFilter(
                        this.nameFilter.getText(),
                        this.showMediumController.getMedium(),
                        -1,
                        -1
                ),
                this.nameFilter.textProperty(),
                this.showMediumController.mediumProperty()
        );

        FilteredList<MediumItem> itemFilteredList = new FilteredList<>(this.observableMediaItems);
        this.mediaView.setItems(itemFilteredList);
        itemFilteredList.predicateProperty().bind(Bindings.createObjectBinding(
                () -> {
                    final MediumFilter mediumFilter;
                    if (this.mediumFilter == null) {
                        mediumFilter = null;
                    } else {
                        mediumFilter = this.mediumFilter.get();
                    }
                    String title;
                    if (mediumFilter == null || mediumFilter.title == null) {
                        title = null;
                    } else {
                        title = mediumFilter.title.toLowerCase();
                    }
                    return mediumItem -> {
                        if (mediumFilter != null) {
                            if (mediumFilter.medium > 0 && !MediumType.intersect(mediumFilter.medium, mediumItem.getMedium())) {
                                return false;
                            }
                            if (title != null && !mediumItem.getTitle().toLowerCase().contains(title)) {
                                return false;
                            }
                        }
                        final int mediumId = mediumItem.getMediumId();
                        final List<Integer> value = this.listItemsProperty.getValue();

                        if (value == null || this.listFilterView.getItems().isEmpty()) {
                            return true;
                        }
                        if (this.ignoreLists.isSelected()) {
                            return !value.contains(mediumId);
                        } else {
                            return value.contains(mediumId);
                        }
                    };
                },
                this.listFilterView.getItems(),
                this.ignoreLists.selectedProperty(),
                this.mediumFilter,
                this.listItemsProperty
        ));
        this.listFilterView.setCellFactory(param -> {
            final ListCell<MediaList> cell = new ListCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(
                    () -> cell.getItem() == null ? "" : cell.getItem().getName(),
                    cell.itemProperty())
            );
            return cell;
        });

        this.listFilterView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                this.listFilterView.getItems().removeAll(this.listFilterView.getSelectionModel().getSelectedItems());
            }
        });

        this.listFilterView.getItems().addListener((InvalidationListener) observable -> {
            List<Integer> listIds = new ArrayList<>(this.listFilterView.getItems().size());

            for (MediaList item : this.listFilterView.getItems()) {
                listIds.add(item.getListId());
            }
            if (this.listItemsLiveData != null) {
                this.listItemsLiveData.removeObserver(this.listItemsObserver);
            }
            this.listItemsLiveData = ApplicationConfig.getRepository().getListItems(listIds);
            this.listItemsLiveData.observe(this.listItemsObserver);
        });
        this.listLiveData = ApplicationConfig.getLiveDataRepository().flatMap(Repository::getInternLists);
        this.listLiveData.observe(this.listObserver);
        ControllerUtils.addAutoCompletionBinding(
                this.listFilter,
                this.listLiveData,
                MediaList::getName,
                mediaList -> this.listFilterView.getItems().add(mediaList)
        );

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

        this.mediumContentView.setCellFactory(param -> {
            final TocEpisodeCell cell = new TocEpisodeCell(lockedImage, readImage, onlineImage, localImage);
            cell.currentMediumProperty().bind(this.mediaView.getSelectionModel().selectedItemProperty());
            return cell;
        });

        this.scrollToEpisodeField.setTextFormatter(this.scrollToEpisodeFormatter);
        this.minEpisodeIndex.setTooltip(new Tooltip("Minimum Episode Index\n-1 or leave it empty to ignore"));
        this.maxEpisodeIndex.setTooltip(new Tooltip("Maximum Episode Index\n-1 or leave it empty to ignore"));
        this.minEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));
        this.maxEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));

        this.episodeSorting.setConverter(new MainController.DisplayConverter<>(EpisodeSorting.values()));
        this.episodeSorting.getItems().setAll(EpisodeSorting.values());
        this.episodeSorting.getSelectionModel().select(EpisodeSorting.INDEX_DESC);
        ControllerUtils.initSavedController(this.savedFilterProperty, this.savedFilterStateController);
        ControllerUtils.initReadController(this.readFilterProperty, this.readFilterStateController);
        this.episodeFilterBinding = Bindings.createObjectBinding(
                () -> new EpisodeFilter(this.readFilterProperty.getValue(), this.savedFilterProperty.getValue()),
                this.readFilterProperty,
                this.savedFilterProperty
        );
        final LiveData<Repository> repositorySingle = ApplicationConfig.getLiveDataRepository();
        this.mediaLoadPane.setVisible(true);
        this.mediaItems = repositorySingle.flatMap(repository -> repository.getAllMedia(this.mediumSorting.getValue()));
    }

    private void setMediaViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        Menu menu = new Menu("Add To List");
        menu.setOnShowing(event -> {
            final List<MediaList> value = this.listLiveData.getValue();
            if (value != null) {
                for (int i = 0; i < value.size(); i++) {
                    final MediaList mediaList = value.get(i);
                    final MenuItem item = new MenuItem(mediaList.getName());
                    menu.getItems().add(i, item);
                    item.setOnAction(action -> doMediumRepoAction(
                            String.format("Added to List '%s'", mediaList.getName()),
                            (repository, mediumIds) -> repository.addMediumToList(mediaList.getListId(), mediumIds).get()
                    ));
                }
            }
        });
        menu.setOnHiding(event -> {
            // remove all items except the static "create new List" item
            for (Iterator<MenuItem> iterator = menu.getItems().iterator(); iterator.hasNext(); ) {
                iterator.next();

                if (iterator.hasNext()) {
                    iterator.remove();
                }
            }
        });
        MenuItem createList = new MenuItem();
        createList.setText("Create new List");
        createList.setOnAction(event -> {
            final Optional<MediaList> list = this.createMediaList();
            list.ifPresent(mediaList -> doMediumRepoAction(
                    String.format("Added to new List '%s'", mediaList.getName()),
                    (repository, mediumIds) -> {
                        final int listId = repository.addList(mediaList, false);
                        repository.addMediumToList(listId, mediumIds).get();
                    }));
        });
        menu.getItems().add(createList);
        contextMenu.getItems().add(menu);
        this.mediaView.setContextMenu(contextMenu);
    }

    private void setEpisodeViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem setReadItem = new MenuItem();
        setReadItem.setText("Set Read");
        setReadItem.setOnAction(event -> doEpisodeRepoAction(
                "Set Read",
                (repository, mediumId) -> repository.updateAllRead(mediumId, true),
                (repository, episodeIds, mediumId) -> repository.updateRead(episodeIds, true))
        );

        MenuItem setUnreadItem = new MenuItem();
        setUnreadItem.setText("Set Unread");
        setUnreadItem.setOnAction(event -> doEpisodeRepoAction(
                "Set Unread",
                (repository, mediumId) -> repository.updateAllRead(mediumId, false),
                (repository, episodeIds, mediumId) -> repository.updateRead(episodeIds, false))
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
                (repository, episodeIds, mediumId) -> repository.reload(episodeIds)
        ));

        contextMenu.getItems().addAll(setReadItem, setUnreadItem, downloadItem, deleteItem, reloadItem);
        this.mediumContentView.setContextMenu(contextMenu);
    }

    private void doMediumRepoAction(String description, BiConsumerEx<Repository, Set<Integer>> idsConsumer) {
        final Repository repository = ApplicationConfig.getRepository();
        List<MediumItem> selectedItems = this.mediaView.getSelectionModel().getSelectedItems();
        CompletableFuture.runAsync(() -> {
            Set<Integer> ids = new HashSet<>();
            for (MediumItem item : selectedItems) {
                ids.add(item.getMediumId());
            }
            try {
                idsConsumer.accept(repository, ids);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).whenComplete((aVoid, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
            Platform.runLater(() -> Notifications
                    .create()
                    .title(description + (throwable != null ? "failed" : " succeeded"))
                    .show()
            );
        });
    }

    private Optional<MediaList> createMediaList() {
        Dialog<MediaList> mediaListDialog = new Dialog<>();
        mediaListDialog.setHeaderText("Create Media List");
        final DialogPane pane = new DialogPane();
        final TextField nameField = new TextField();
        CheckBox showTextBox = new CheckBox("Text");
        CheckBox showImageBox = new CheckBox("Image");
        CheckBox showVideoBox = new CheckBox("Video");
        CheckBox showAudioBox = new CheckBox("Audio");

        pane.setContent(new VBox(
                5,
                new HBox(5, new Text("Name:"), nameField),
                new HBox(5, showTextBox, showImageBox, showVideoBox, showAudioBox)
        ));
        mediaListDialog.setDialogPane(pane);
        pane.getButtonTypes().addAll(ButtonType.CLOSE, ButtonType.FINISH);
        pane.lookupButton(ButtonType.FINISH).disableProperty().bind(
                nameField.textProperty().isEmpty()
                        .or(
                                showAudioBox.selectedProperty().not()
                                        .and(showImageBox.selectedProperty().not())
                                        .and(showTextBox.selectedProperty().not())
                                        .and(showVideoBox.selectedProperty().not())
                        ));
        mediaListDialog.setResultConverter(param -> {
            if (param.getButtonData() == ButtonBar.ButtonData.FINISH) {
                final int medium = ControllerUtils.getMedium(showTextBox, showImageBox, showVideoBox, showAudioBox);
                return new MediaListImpl(null, 0, nameField.getText(), medium, 0);
            } else {
                return null;
            }
        });
        return mediaListDialog.showAndWait();
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

        if (this.mediaItems != null) {
            this.mediaItems.observe(this.mediaItemsObserver);
        }

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

    private static class MediumCell extends ListCell<MediumItem> {
        MediumCell() {

        }

        @Override
        protected void updateItem(MediumItem item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.getTitle());
            }
        }
    }

    private static class MediumFilter {
        private final String title;
        private final int medium;
        private final int minEpisodeIndex;
        private final int maxEpisodeIndex;

        public MediumFilter(String title, int medium, int minEpisodeIndex, int maxEpisodeIndex) {
            this.title = title;
            this.medium = medium;
            this.minEpisodeIndex = minEpisodeIndex;
            this.maxEpisodeIndex = maxEpisodeIndex;
        }
    }
}
