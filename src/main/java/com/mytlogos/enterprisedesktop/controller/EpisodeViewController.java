package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.TaskManager;
import com.mytlogos.enterprisedesktop.background.sqlite.PagedList;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.background.sqlite.life.Observer;
import com.mytlogos.enterprisedesktop.model.DisplayRelease;
import com.mytlogos.enterprisedesktop.model.MediaList;
import com.mytlogos.enterprisedesktop.model.OpenableEpisode;
import com.mytlogos.enterprisedesktop.model.SimpleMedium;
import com.mytlogos.enterprisedesktop.preferences.ProfilePreferences;
import com.mytlogos.enterprisedesktop.profile.DisplayEpisodeProfile;
import com.mytlogos.enterprisedesktop.tools.BiConsumerEx;
import com.mytlogos.enterprisedesktop.tools.Log;
import com.mytlogos.enterprisedesktop.tools.TriConsumerEx;
import com.mytlogos.enterprisedesktop.tools.Utils;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.Notifications;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class EpisodeViewController implements Attachable {
    private final ObjectProperty<SavedFilter> savedFilterObjectProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<ReadFilter> readFilterObjectProperty = new SimpleObjectProperty<>();
    private final ObservableList<DisplayRelease> releases = FXCollections.observableArrayList();
    private final Observer<List<Integer>> emptyListItemsObserver = Utils.emptyObserver();
    private final BooleanProperty latestOnlyFilterNeeded = new SimpleBooleanProperty();
    private final Observer<PagedList<DisplayRelease>> pagedListObserver = releases -> {
        Log.info("Receiving new Releases: %d", releases == null ? -1 : releases.size());
        if (releases == null) {
            this.releases.clear();
            return;
        }
        this.latestOnlyFilterNeeded.set(true);
    };
    private final ObservableSet<Integer> filterListIds = FXCollections.observableSet();
    private final ObservableSet<Integer> ignoredListIds = FXCollections.observableSet();
    private final ObservableSet<Integer> filterMediumIds = FXCollections.observableSet();
    private final ObservableSet<Integer> ignoreMediumIds = FXCollections.observableSet();
    private final ObservableList<FilterItem<MediaList>> listFilterItems = FXCollections.observableArrayList();
    private final ObservableList<FilterItem<SimpleMedium>> mediumFilterItems = FXCollections.observableArrayList();
    private final Observer<List<MediaList>> listObserver = this::updateFilteredListView;
    private final Observer<List<SimpleMedium>> mediumObserver = this::updateFilteredMediaView;
    @FXML
    private FlowPane mediumFlowFilter;
    @FXML
    private FlowPane listFlowFilter;
    @FXML
    private HBox savedFilterState;
    @FXML
    private ThreeStatesController savedFilterStateController;
    @FXML
    private HBox readFilterState;
    @FXML
    private ThreeStatesController readFilterStateController;
    @FXML
    private HBox showMedium;
    @FXML
    private MediumTypes showMediumController;
    @FXML
    private ToggleButton ignoreMedium;
    @FXML
    private ToggleButton ignoreLists;
    @FXML
    private TextField listFilter;
    @FXML
    private TextField mediumFilter;
    @FXML
    private ListView<DisplayRelease> episodes;
    @FXML
    private Spinner<Integer> minEpisodeIndex;
    @FXML
    private Spinner<Integer> maxEpisodeIndex;
    @FXML
    private ToggleButton latestOnly;
    @FXML
    private ToggleButton ignoreLocked;
    private LiveData<PagedList<DisplayRelease>> episodesLiveData;
    private LiveData<List<Integer>> filterListItemsLiveData;
    private LiveData<List<Integer>> ignoredListItemsLiveData;
    private LiveData<List<MediaList>> listLiveData;
    private LiveData<List<SimpleMedium>> mediumLiveData;

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
        this.mediumFilterItems.addListener((ListChangeListener<? super FilterItem<SimpleMedium>>) c -> {
            if (c.next()) {
                if (c.wasAdded()) {
                    for (FilterItem<SimpleMedium> item : c.getAddedSubList()) {
                        item.setOnClose(() -> {
                            if (item.isIgnored()) {
                                this.ignoreMediumIds.remove(item.getValue().getMediumId());
                            } else {
                                this.filterMediumIds.remove(item.getValue().getMediumId());
                            }
                        });
                        this.mediumFlowFilter.getChildren().add(item.getRoot());
                    }
                }
                if (c.wasRemoved()) {
                    for (FilterItem<SimpleMedium> item : c.getRemoved()) {
                        this.mediumFlowFilter.getChildren().remove(item.getRoot());
                    }
                }
            }
        });
        this.latestOnly.selectedProperty().addListener(o -> this.latestOnlyFilterNeeded.set(true));
        this.latestOnlyFilterNeeded.addListener(o -> filterLatestOnly());
        final FilteredList<DisplayRelease> filteredList = new FilteredList<>(this.releases);

        filteredList.predicateProperty().bind(Bindings.createObjectBinding(
                () -> displayRelease -> {
                    final int mediumId = displayRelease.getMediumId();

                    if (this.ignoredListItemsLiveData != null) {
                        final List<Integer> value = this.ignoredListItemsLiveData.getValue();

                        if (value != null && !this.ignoredListIds.isEmpty() && value.contains(mediumId)) {
                            return false;
                        }
                    }

                    if (!this.ignoreMediumIds.isEmpty() && this.ignoreMediumIds.contains(mediumId)) {
                        return false;
                    }

                    if (this.filterListItemsLiveData != null) {
                        final List<Integer> value = this.filterListItemsLiveData.getValue();

                        if (value != null && !this.filterListIds.isEmpty()) {
                            return value.contains(mediumId);
                        }
                    }
                    return true;
                },
                this.filterListIds,
                this.ignoredListIds,
                this.ignoreMediumIds
        ));
        filteredList.addListener((ListChangeListener<? super DisplayRelease>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    System.out.println(String.format("Added to Filtered %d", change.getAddedSize()));
                } else if (change.wasPermutated()) {
                    System.out.println("permutated in filtered");
                } else if (change.wasRemoved()) {
                    System.out.println("removed in filtered");
                } else if (change.wasReplaced()) {
                    System.out.println("replaced in filtered");
                }
            }
        });

        this.episodes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.filterListIds.addListener((InvalidationListener) observable -> {
            List<Integer> listIds = new ArrayList<>(this.filterListIds);

            if (this.filterListItemsLiveData != null) {
                this.filterListItemsLiveData.removeObserver(this.emptyListItemsObserver);
            }
            this.filterListItemsLiveData = ApplicationConfig.getRepository().getListItems(listIds);
            this.filterListItemsLiveData.observe(this.emptyListItemsObserver);
        });
        this.ignoredListIds.addListener((InvalidationListener) observable -> {
            List<Integer> listIds = new ArrayList<>(this.ignoredListIds);

            if (this.ignoredListItemsLiveData != null) {
                this.ignoredListItemsLiveData.removeObserver(this.emptyListItemsObserver);
            }
            this.ignoredListItemsLiveData = ApplicationConfig.getRepository().getListItems(listIds);
            this.ignoredListItemsLiveData.observe(this.emptyListItemsObserver);
        });

        this.episodes.setItems(filteredList);
        this.minEpisodeIndex.setTooltip(new Tooltip("Minimum Episode Index\n-1 or leave it empty to ignore"));
        this.maxEpisodeIndex.setTooltip(new Tooltip("Maximum Episode Index\n-1 or leave it empty to ignore"));
        this.minEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));
        this.maxEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));

        Image lockedImage = new Image("/ic_lock_dark.png", true);
        Image readImage = new Image("/ic_episode_read_icon.png", true);
        Image onlineImage = new Image("/ic_open_browser_icon.png", true);
        Image localImage = new Image("/ic_open_local_icon.png", true);

        this.setEpisodeViewContextMenu();
        this.episodes.setCellFactory(param -> new DisplayReleaseCell(lockedImage, readImage, onlineImage, localImage));

        final LiveData<Repository> repositorySingle = ApplicationConfig.getLiveDataRepository();
        this.listLiveData = repositorySingle.flatMap(Repository::getInternLists);
        this.mediumLiveData = repositorySingle.flatMap(Repository::getSimpleMedium);

        this.episodesLiveData = repositorySingle.flatMap(t -> {
            if (t == null) {
                return LiveData.empty();
            }

            return ControllerUtils.combineLatest(
                    repositorySingle,
                    getProfilePreferences().displayEpisodeProfileProperty(),
                    (repository, releaseFilter) -> {
                        Log.info("fetching all episodes");
                        return repository.getDisplayEpisodes(releaseFilter);
                    });
        }).flatMap(pagedListLiveData -> pagedListLiveData);

        // todo save list and medium ids as fields and read,write changes to these lists to the corresponding list views
        this.listLiveData.observe(this.listObserver);
        this.mediumLiveData.observe(this.mediumObserver);
        this.filterListIds.addListener((InvalidationListener) observable -> updateFilteredListView(this.listLiveData.getValue()));
        this.ignoredListIds.addListener((InvalidationListener) observable -> updateFilteredListView(this.listLiveData.getValue()));
        this.filterMediumIds.addListener((InvalidationListener) observable -> updateFilteredMediaView(this.mediumLiveData.getValue()));
        this.ignoreMediumIds.addListener((InvalidationListener) observable -> updateFilteredMediaView(this.mediumLiveData.getValue()));
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
        ControllerUtils.addAutoCompletionBinding(
                this.mediumFilter,
                this.mediumLiveData,
                SimpleMedium::getTitle,
                medium -> {
                    if (this.ignoreMedium.isSelected()) {
                        this.ignoreMediumIds.add(medium.getMediumId());
                    } else {
                        this.filterMediumIds.add(medium.getMediumId());
                    }
                }
        );
        ControllerUtils.initReadController(this.readFilterObjectProperty, this.readFilterStateController);
        ControllerUtils.initSavedController(this.savedFilterObjectProperty, this.savedFilterStateController);
        this.updateFromProfile(getProfilePreferences().getDisplayEpisodeProfile());
        ControllerUtils.listen(
                () -> getProfilePreferences().setDisplayEpisodeProfile(
                        new DisplayEpisodeProfile(
                                this.showMediumController.getMedium(),
                                this.minEpisodeIndex.getValue(),
                                this.maxEpisodeIndex.getValue(),
                                this.latestOnly.isSelected(),
                                this.ignoreLocked.isSelected(),
                                new ArrayList<>(this.filterListIds),
                                new ArrayList<>(this.ignoredListIds),
                                new ArrayList<>(this.filterMediumIds),
                                new ArrayList<>(this.ignoreMediumIds),
                                this.readFilterObjectProperty.getValue(),
                                this.savedFilterObjectProperty.getValue()
                        )),
                this.latestOnly.selectedProperty(),
                this.ignoreLocked.selectedProperty(),
                this.readFilterObjectProperty,
                this.savedFilterObjectProperty,
                this.showMediumController.mediumProperty(),
                this.minEpisodeIndex.valueProperty(),
                this.maxEpisodeIndex.valueProperty(),
                this.ignoredListIds,
                this.filterListIds,
                this.filterMediumIds,
                this.ignoreMediumIds
        );
    }

    private void filterLatestOnly() {
        if (!this.latestOnlyFilterNeeded.get()) {
            return;
        }
        this.latestOnlyFilterNeeded.set(false);

        final PagedList<DisplayRelease> list = this.episodesLiveData.getValue();

        if (list == null) {
            return;
        }
        if (this.latestOnly.isSelected()) {
            Set<Integer> episodeIds = new HashSet<>();
            List<DisplayRelease> latestUnique = new LinkedList<>();
            // only simply iterate, the latest releases come the earliest as its in descending order
            for (DisplayRelease release : list) {
                if (episodeIds.add(release.getEpisodeId())) {
                    latestUnique.add(release);
                }
            }
            this.releases.setAll(latestUnique);
        } else {
            this.releases.setAll(list);
        }
    }

    private void setEpisodeViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem openLinkItem = new MenuItem();
        openLinkItem.setText("Open in Browser");
        openLinkItem.setOnAction(event -> {
            OpenableEpisode selectedItem = this.episodes.getSelectionModel().getSelectedItem();

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
                (repository, ids, mediumId) -> repository.updateRead(ids, true))
        );

        MenuItem setUnreadItem = new MenuItem();
        setUnreadItem.setText("Set Unread");
        setUnreadItem.setOnAction(event -> doEpisodeRepoAction(
                "Set Unread",
                (repository, mediumId) -> repository.updateAllRead(mediumId, false),
                (repository, ids, mediumId) -> repository.updateRead(ids, false))
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

        contextMenu.getItems().addAll(openLinkItem, setReadItem, setUnreadItem, downloadItem, deleteItem, reloadItem);
        this.episodes.setContextMenu(contextMenu);
    }

    private ProfilePreferences getProfilePreferences() {
        return ApplicationConfig.getMainPreferences().getProfilePreferences();
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

    private void updateFilteredMediaView(List<SimpleMedium> mediaList) {
        if (mediaList == null) {
            return;
        }
        List<SimpleMedium> filterLists = new ArrayList<>(this.filterMediumIds.size());
        List<SimpleMedium> ignoreLists = new ArrayList<>(this.ignoreMediumIds.size());

        for (SimpleMedium medium : mediaList) {
            if (this.filterMediumIds.contains(medium.getMediumId())) {
                filterLists.add(medium);
            }
            if (this.ignoreMediumIds.contains(medium.getMediumId())) {
                ignoreLists.add(medium);
            }
        }

        final List<FilterItem<SimpleMedium>> removeItems = new ArrayList<>();

        for (FilterItem<SimpleMedium> item : this.mediumFilterItems) {
            final SimpleMedium value = item.getValue();

            if (item.isIgnored()) {
                if (!ignoreLists.remove(value)) {
                    removeItems.add(item);
                }
            } else if (!filterLists.remove(value)) {
                removeItems.add(item);
            }
        }
        final List<FilterItem<SimpleMedium>> newItems = new ArrayList<>();

        for (SimpleMedium medium : filterLists) {
            FilterItem<SimpleMedium> mediaListFilterItem = new FilterItem<>(medium.getTitle(), medium);
            mediaListFilterItem.setIgnored(false);
            newItems.add(mediaListFilterItem);
        }

        for (SimpleMedium medium : ignoreLists) {
            FilterItem<SimpleMedium> mediaListFilterItem = new FilterItem<>(medium.getTitle(), medium);
            mediaListFilterItem.setIgnored(true);
            newItems.add(mediaListFilterItem);
        }
        this.mediumFilterItems.removeAll(removeItems);
        this.mediumFilterItems.addAll(newItems);
    }

    private void updateFromProfile(DisplayEpisodeProfile profile) {
        this.showMediumController.setMedium(profile.medium);
        this.minEpisodeIndex.getValueFactory().setValue(profile.minEpisodeIndex);
        this.maxEpisodeIndex.getValueFactory().setValue(profile.maxEpisodeIndex);
        this.latestOnly.setSelected(profile.latestOnly);
        this.filterListIds.clear();
        this.filterListIds.addAll(profile.filterListIds);
        this.ignoredListIds.clear();
        this.ignoredListIds.addAll(profile.ignoreListIds);
        this.filterMediumIds.clear();
        this.filterMediumIds.addAll(profile.filterMediumIds);
        this.ignoreMediumIds.clear();
        this.ignoreMediumIds.addAll(profile.ignoreMediumIds);
        this.readFilterObjectProperty.set(ReadFilter.getValue(profile.readFilter));
        this.savedFilterObjectProperty.set(SavedFilter.getValue(profile.savedFilter));
    }

    private void doEpisodeRepoAction(String description, BiConsumerEx<Repository, Integer> allConsumer, TriConsumerEx<Repository, Set<Integer>, Integer> idsConsumer) {
        final Repository repository = ApplicationConfig.getRepository();
        List<DisplayRelease> selectedItems = this.episodes.getSelectionModel().getSelectedItems();

        Map<Integer, Set<Integer>> mediumEpisodeIds = new HashMap<>();

        for (DisplayRelease item : selectedItems) {
            mediumEpisodeIds.computeIfAbsent(item.getMediumId(), integer -> new HashSet<>()).add(item.getEpisodeId());
        }
        List<CompletableFuture<Boolean>> futures = new LinkedList<>();
        for (Map.Entry<Integer, Set<Integer>> entry : mediumEpisodeIds.entrySet()) {
            // TODO 06.3.2020: for now disable actions for a whole medium
//            if (entry.getValue().size() == this.episodes.getItems().size()) {
//                try {
//                    allConsumer.accept(repository, entry.getKey());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    idsConsumer.accept(repository, entry.getValue(), entry.getKey());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).handle((aVoid, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                    return false;
                }
                return true;
            }));
//            }
        }
        Utils.finishAll(futures).whenComplete((booleans, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
            if (booleans != null) {
                final long succeeded = booleans.stream().filter(Boolean::booleanValue).count();
                final long failed = booleans.size() - succeeded;

                final String title;
                if (failed > 0 && succeeded > 0) {
                    title = String.format("%s: %d succeeded, %d failed", description, succeeded, failed);
                } else if (failed > 0) {
                    title = String.format("%s: all failed", description);
                } else {
                    title = String.format("%s: all succeeded", description);
                }
                Platform.runLater(() -> Notifications.create().title(title).show());
            }
        });
    }

    @Override
    public void onAttach() {
        if (this.episodesLiveData != null) {
            this.episodesLiveData.removeObserver(this.pagedListObserver);
            this.episodesLiveData.observe(this.pagedListObserver);
        }
        if (this.listLiveData != null) {
            this.listLiveData.removeObserver(this.listObserver);
            this.listLiveData.observe(this.listObserver);
        }
        if (this.mediumLiveData != null) {
            this.mediumLiveData.removeObserver(this.mediumObserver);
            this.mediumLiveData.observe(this.mediumObserver);
        }
    }

    @Override
    public void onDetach() {
        if (this.episodesLiveData != null) {
            this.episodesLiveData.removeObserver(this.pagedListObserver);
        }
        if (this.listLiveData != null) {
            this.listLiveData.removeObserver(this.listObserver);
        }
        if (this.mediumLiveData != null) {
            this.mediumLiveData.removeObserver(this.mediumObserver);
        }
    }

    private boolean containsSameEpisodeRelease(DisplayRelease release) {
        for (DisplayRelease item : this.episodes.getItems()) {
            if (item.getEpisodeId() == release.getEpisodeId()) {
                return true;
            }
        }
        return false;
    }

    private static class DisplayReleaseCell extends ListCell<DisplayRelease> {
        private final Image lockedImage;
        private final Image readImage;
        private final Image onlineImage;
        private final Image localImage;
        private VBox root;
        @FXML
        private Label content;
        @FXML
        private Text topLeftContent;
        @FXML
        private Text topRightContent;
        @FXML
        private ImageView lockedView;
        @FXML
        private ImageView readView;
        @FXML
        private ImageView onlineView;
        @FXML
        private ImageView localView;
        private boolean loadFailed;

        private DisplayReleaseCell(Image lockedImage, Image readImage, Image onlineImage, Image localImage) {
            this.lockedImage = lockedImage;
            this.readImage = readImage;
            this.onlineImage = onlineImage;
            this.localImage = localImage;
            this.setPrefWidth(0);
            this.setOnMouseClicked(event -> {
                final DisplayRelease item = this.getItem();
                if (!event.getButton().equals(MouseButton.PRIMARY) || event.getClickCount() < 2) {
                    return;
                }
                final int mediumType = ApplicationConfig.getRepository().getMediumType(item.getMediumId());
                ControllerUtils.openEpisode(item, mediumType, item.getMediumId());
            });
        }

        public void initialize() {
            System.out.println("initializing item");
        }

        @Override
        protected void updateItem(DisplayRelease item, boolean empty) {
            super.updateItem(item, empty);

            if (this.root == null && !this.loadFailed) {
                this.init();
            }
            if (empty || item == null) {
                this.setText(null);
                this.setGraphic(null);
            } else {
                if (this.loadFailed) {
                    this.setGraphic(null);
                    this.setText("Could not load Item Graphic");
                } else {
                    this.setGraphic(this.root);
                    final String title = item.getTitle();

                    final String latestRelease = Formatter.format(item.getReleaseDate());

                    this.topLeftContent.setText(item.getCombiIndex());

                    this.topRightContent.setText(latestRelease);
                    this.content.setText(title);

                    this.lockedView.setVisible(item.isLocked());
                    this.readView.setOpacity(item.isRead() ? 1 : 0.25);
                    this.onlineView.setOpacity(1);
                    this.localView.setOpacity(item.isSaved() ? 1 : 0.25);
                }
            }
        }

        private void init() {
            this.root = ControllerUtils.load("/tocEpisodeItem.fxml", this);
            if (this.root == null) {
                this.loadFailed = true;
            } else {
                this.lockedView.setImage(this.lockedImage);
                this.readView.setImage(this.readImage);
                this.onlineView.setImage(this.onlineImage);
                this.localView.setImage(this.localImage);
                this.loadFailed = false;
            }
        }
    }
}
