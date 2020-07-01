package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.TaskManager;
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
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private final ObservableSet<Integer> filterListIds = FXCollections.observableSet();
    private final ObservableSet<Integer> ignoredListIds = FXCollections.observableSet();
    private final ObservableList<FilterItem<MediaList>> listFilterItems = FXCollections.observableArrayList();
    private final ObservableList<Integer> observableFilterListItems = FXCollections.observableArrayList();
    private final ObservableList<Integer> observableIgnoreListItems = FXCollections.observableArrayList();
    private final Observer<List<MediaList>> listObserver = this::updateFilteredListView;
    private final Observer<List<Integer>> filterListItemsObserver = mediaIds -> this.observableFilterListItems.setAll(mediaIds == null ? Collections.emptyList() : mediaIds);
    private final Observer<List<Integer>> ignoreListItemsObserver = mediaIds -> this.observableIgnoreListItems.setAll(mediaIds == null ? Collections.emptyList() : mediaIds);
    private ObjectBinding<MediumFilter> mediumFilter;
    @FXML
    private TextField listFilter;
    @FXML
    private ToggleButton ignoreLists;
    @FXML
    private FlowPane listFlowFilter;
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
    private LiveData<List<MediaList>> listLiveData;
    private LiveData<List<Integer>> filterListItemsLiveData;
    private LiveData<List<Integer>> ignoredListItemsLiveData;

    public void initialize() {
        this.listFilterItems.addListener((ListChangeListener<? super FilterItem<MediaList>>) c -> {
            if (c.next()) {
                if (c.wasAdded()) {
                    for (FilterItem<MediaList> item : c.getAddedSubList()) {
                        item.setOnClose(() -> {
                            if (item.isIgnored()) {
                                this.ignoredListIds.remove(item.getValue().getListId());
                            } else {
                                this.filterListIds.remove(item.getValue().getListId());
                            }
                        });
                        this.listFlowFilter.getChildren().add(item.getRoot());
                    }
                }
                if (c.wasRemoved()) {
                    for (FilterItem<MediaList> item : c.getRemoved()) {
                        this.listFlowFilter.getChildren().remove(item.getRoot());
                    }
                }
            }
        });
        this.filterListIds.addListener((InvalidationListener) observable -> {
            List<Integer> listIds = new ArrayList<>(this.filterListIds);

            if (this.filterListItemsLiveData != null) {
                this.filterListItemsLiveData.removeObserver(this.filterListItemsObserver);
            }
            this.filterListItemsLiveData = ApplicationConfig.getRepository().getListItems(listIds);
            this.filterListItemsLiveData.observe(this.filterListItemsObserver);
        });
        this.ignoredListIds.addListener((InvalidationListener) observable -> {
            List<Integer> listIds = new ArrayList<>(this.ignoredListIds);

            if (this.ignoredListItemsLiveData != null) {
                this.ignoredListItemsLiveData.removeObserver(this.ignoreListItemsObserver);
            }
            this.ignoredListItemsLiveData = ApplicationConfig.getRepository().getListItems(listIds);
            this.ignoredListItemsLiveData.observe(this.ignoreListItemsObserver);
        });
        this.filterListIds.addListener((InvalidationListener) observable -> updateFilteredListView(this.listLiveData.getValue()));
        this.ignoredListIds.addListener((InvalidationListener) observable -> updateFilteredListView(this.listLiveData.getValue()));
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

                        if (!this.observableIgnoreListItems.isEmpty() && this.observableIgnoreListItems.contains(mediumId)) {
                            return false;
                        }
                        return this.observableFilterListItems.isEmpty() || this.observableFilterListItems.contains(mediumId);
                    };
                },
                this.observableFilterListItems,
                this.observableIgnoreListItems,
                this.mediumFilter
        ));
        this.listLiveData = ApplicationConfig.getLiveDataRepository().flatMap(Repository::getInternLists);
        this.listLiveData.observe(this.listObserver);
        ControllerUtils.addAutoCompletionBinding(
                this.listFilter,
                this.listLiveData,
                MediaList::getName,
                mediaList -> {
                    if (this.ignoreLists.isSelected()) {
                        this.ignoredListIds.add(mediaList.getListId());
                    } else {
                        this.filterListIds.add(mediaList.getListId());
                    }
                }
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

    private void updateFilteredListView(List<MediaList> mediaLists) {
        if (mediaLists == null) {
            return;
        }
        List<MediaList> filterLists = new ArrayList<>(this.filterListIds.size());
        List<MediaList> ignoreLists = new ArrayList<>(this.ignoredListIds.size());

        for (MediaList mediaList : mediaLists) {
            if (this.filterListIds.contains(mediaList.getListId())) {
                filterLists.add(mediaList);
            }
            if (this.ignoredListIds.contains(mediaList.getListId())) {
                ignoreLists.add(mediaList);
            }
        }

        final List<FilterItem<MediaList>> removeItems = new ArrayList<>();

        for (FilterItem<MediaList> item : this.listFilterItems) {
            final MediaList value = item.getValue();

            if (item.isIgnored()) {
                if (!ignoreLists.remove(value)) {
                    removeItems.add(item);
                }
            } else if (!filterLists.remove(value)) {
                removeItems.add(item);
            }
        }
        final List<FilterItem<MediaList>> newItems = new ArrayList<>();

        for (MediaList mediaList : filterLists) {
            FilterItem<MediaList> mediaListFilterItem = new FilterItem<>(mediaList.getName(), mediaList);
            mediaListFilterItem.setIgnored(false);
            newItems.add(mediaListFilterItem);
        }

        for (MediaList mediaList : ignoreLists) {
            FilterItem<MediaList> mediaListFilterItem = new FilterItem<>(mediaList.getName(), mediaList);
            mediaListFilterItem.setIgnored(true);
            newItems.add(mediaListFilterItem);
        }
        this.listFilterItems.removeAll(removeItems);
        this.listFilterItems.addAll(newItems);
    }

    private void setMediaViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        Menu menu = ControllerUtils.getAddToListMenu(this.listLiveData, this.mediaView.getSelectionModel());
        contextMenu.getItems().add(menu);
        this.mediaView.setContextMenu(contextMenu);
    }

    private void setEpisodeViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem openLinkItem = new MenuItem();
        openLinkItem.setText("Open in Browser");
        openLinkItem.setOnAction(event -> {
            OpenableEpisode selectedItem = this.mediumContentView.getSelectionModel().getSelectedItem();

            if (selectedItem == null) {
                Notifications.create().title("No Episode Selected!").show();
                return;
            }
            TaskManager.runCompletableTask(() -> {
                final int episodeId = selectedItem.getEpisodeId();
                final List<String> links = ApplicationConfig.getRepository().getReleaseLinks(episodeId);

                if (!links.isEmpty()) {
                    ControllerUtils.openUrl(links.get(0));
                }
                return null;
            }).whenComplete((o, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                    Platform.runLater(
                            () -> Notifications
                                    .create()
                                    .title("An Error occurred while trying to open Episode in Browser!")
                                    .show()
                    );
                }
            });
        });

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

        contextMenu.getItems().addAll(openLinkItem, setReadItem, setUnreadItem, downloadItem, deleteItem, reloadItem);
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
            this.setOnMouseClicked(event -> {
                if (ControllerUtils.isDoubleClick(event)) {
                    final MediumItem item = this.getItem();

                    if (item != null) {
                        ControllerUtils.openMedium(item.getMediumId());
                    }
                }
            });
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
