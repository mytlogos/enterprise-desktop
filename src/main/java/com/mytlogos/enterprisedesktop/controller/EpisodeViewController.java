package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.sqlite.PagedList;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.background.sqlite.life.Observer;
import com.mytlogos.enterprisedesktop.model.DisplayRelease;
import com.mytlogos.enterprisedesktop.model.MediaList;
import com.mytlogos.enterprisedesktop.model.SimpleMedium;
import com.mytlogos.enterprisedesktop.tools.BiConsumerEx;
import com.mytlogos.enterprisedesktop.tools.Log;
import com.mytlogos.enterprisedesktop.tools.TriConsumerEx;
import com.mytlogos.enterprisedesktop.tools.Utils;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.Notifications;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 *
 */
public class EpisodeViewController implements Attachable {
    private final ObjectProperty<SavedFilter> savedFilterObjectProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<ReadFilter> readFilterObjectProperty = new SimpleObjectProperty<>();
    private final ObservableList<DisplayRelease> releases = FXCollections.observableArrayList();
    private final Observer<PagedList<DisplayRelease>> pagedListObserver = releases -> {
        Log.info("Receiving new Releases: %d", releases == null ? -1 : releases.size());
        if (releases == null) {
            this.releases.clear();
            return;
        }
        this.latestOnlyFilterNeeded.set(true);
    };
    private final Observer<List<Integer>> listItemsObserver = Utils.emptyObserver();
    @FXML
    private ToggleButton ignoreMedium;
    @FXML
    private ToggleButton ignoreLists;
    @FXML
    private RadioButton readOnly;
    @FXML
    private RadioButton notReadOnly;
    @FXML
    private RadioButton ignoreRead;
    @FXML
    private RadioButton savedOnly;
    @FXML
    private RadioButton notSavedOnly;
    @FXML
    private RadioButton ignoreSaved;
    @FXML
    private TextField listFilter;
    @FXML
    private ListView<MediaList> listFilterView;
    @FXML
    private TextField mediumFilter;
    @FXML
    private ListView<SimpleMedium> mediumFilterView;
    @FXML
    private ListView<DisplayRelease> episodes;
    @FXML
    private ToggleGroup readFilter;
    @FXML
    private ToggleGroup savedFilter;
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
    private ObjectBinding<ReleaseFilter> episodeFilterBinding;
    private LiveData<PagedList<DisplayRelease>> episodesLiveData;
    private LiveData<List<Integer>> listItemsLiveData;
    private LiveData<List<MediaList>> listLiveData;
    private LiveData<List<SimpleMedium>> mediumLiveData;
    private Observer<List<MediaList>> listObserver;
    private Observer<List<SimpleMedium>> mediumObserver;
    private final BooleanProperty latestOnlyFilterNeeded = new SimpleBooleanProperty();

    public void initialize() {
        this.latestOnly.selectedProperty().addListener(o -> this.latestOnlyFilterNeeded.set(true));
        this.latestOnlyFilterNeeded.addListener(o -> filterLatestOnly());
        final FilteredList<DisplayRelease> filteredList = new FilteredList<>(this.releases);

        filteredList.predicateProperty().bind(Bindings.createObjectBinding(
                () -> displayRelease -> {
                    final int mediumId = displayRelease.getMediumId();
                    if (this.listItemsLiveData == null) {
                        return true;
                    }
                    final List<Integer> value = this.listItemsLiveData.getValue();
                    if (value == null || this.listFilterView.getItems().isEmpty()) {
                        return true;
                    }
                    if (this.ignoreLists.isSelected()) {
                        return !value.contains(mediumId);
                    } else {
                        return value.contains(mediumId);
                    }
                },
                this.listFilterView.getItems(),
                this.ignoreLists.selectedProperty()
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

        this.mediumFilterView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                this.mediumFilterView.getItems().removeAll(this.mediumFilterView.getSelectionModel().getSelectedItems());
            }
        });

        this.mediumFilterView.setCellFactory(param -> {
            final ListCell<SimpleMedium> cell = new ListCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(
                    () -> cell.getItem() == null ? "" : cell.getItem().getTitle(),
                    cell.itemProperty())
            );
            return cell;
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

        this.episodes.setItems(filteredList);
        this.minEpisodeIndex.setTooltip(new Tooltip("Minimum Episode Index\n-1 or leave it empty to ignore"));
        this.maxEpisodeIndex.setTooltip(new Tooltip("Maximum Episode Index\n-1 or leave it empty to ignore"));
        this.minEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));
        this.maxEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));
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

        Image lockedImage = new Image("/ic_lock_dark.png", true);
        Image readImage = new Image("/ic_episode_read_icon.png", true);
        Image onlineImage = new Image("/ic_open_browser_icon.png", true);
        Image localImage = new Image("/ic_open_local_icon.png", true);

        this.setEpisodeViewContextMenu();
        this.episodeFilterBinding = Bindings.createObjectBinding(
                () -> new ReleaseFilter(
                        this.readFilterObjectProperty.getValue(),
                        this.savedFilterObjectProperty.getValue(),
                        ControllerUtils.getMedium(this.showTextBox, this.showImageBox, this.showVideoBox, this.showAudioBox),
                        this.minEpisodeIndex.getValue(),
                        this.maxEpisodeIndex.getValue(),
                        this.latestOnly.isSelected(),
                        this.listFilterView.getItems().stream().map(MediaList::getListId).collect(Collectors.toList()),
                        this.mediumFilterView.getItems().stream().map(SimpleMedium::getMediumId).collect(Collectors.toList()),
                        this.ignoreMedium.isSelected(),
                        this.ignoreLists.isSelected()
                ),
                this.readFilterObjectProperty,
                this.savedFilterObjectProperty,
                this.showAudioBox.selectedProperty(),
                this.showImageBox.selectedProperty(),
                this.showTextBox.selectedProperty(),
                this.showVideoBox.selectedProperty(),
                this.minEpisodeIndex.valueProperty(),
                this.maxEpisodeIndex.valueProperty(),
                this.ignoreMedium.selectedProperty(),
                this.mediumFilterView.getItems()
        );
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
                    this.episodeFilterBinding,
                    (repository, releaseFilter) -> {
                        Log.info("fetching all episodes");
                        return repository.getDisplayEpisodes(releaseFilter);
                    });
        }).flatMap(pagedListLiveData -> pagedListLiveData);

        listObserver = mediaLists -> {
        };
        this.listLiveData.observe(this.listObserver);
        mediumObserver = simpleMedia -> {
        };
        this.mediumLiveData.observe(this.mediumObserver);
        ControllerUtils.addAutoCompletionBinding(this.listFilter, this.listLiveData, MediaList::getName, mediaList -> this.listFilterView.getItems().add(mediaList));
        ControllerUtils.addAutoCompletionBinding(this.mediumFilter, this.mediumLiveData, SimpleMedium::getTitle, simpleMedium -> this.mediumFilterView.getItems().add(simpleMedium));
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

        contextMenu.getItems().addAll(setReadItem, setUnreadItem, downloadItem, deleteItem, reloadItem);
        this.episodes.setContextMenu(contextMenu);
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
