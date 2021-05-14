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
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.Notifications;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class ListViewController implements Attachable {
    private final ObjectProperty<SavedFilter> savedFilterProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<ReadFilter> readFilterProperty = new SimpleObjectProperty<>();
    private final TextFormatter<Double> scrollToEpisodeFormatter = ControllerUtils.doubleTextFormatter();
    @FXML
    private HBox readFilterState;
    @FXML
    private ThreeStatesController readFilterStateController;
    @FXML
    private HBox savedFilterState;
    @FXML
    private ThreeStatesController savedFilterStateController;
    @FXML
    private StackPane listLoadPane;
    @FXML
    private StackPane listsLoadPane;
    @FXML
    private StackPane mediumLoadPane;
    @FXML
    private TextField scrollToEpisodeField;
    @FXML
    private ScrollPane detailPane;
    @FXML
    private ToggleButton episodeAscendingBtn;
    @FXML
    private ChoiceBox<EpisodeSorting> episodeSorting;
    @FXML
    private ToggleButton mediumAscendingBtn;
    @FXML
    private ChoiceBox<MediumSorting> mediumSorting;
    @FXML
    private ToggleButton enableSelectBtn;
    @FXML
    private ListView<MediaList> listsView;
    private final Observer<List<MediaList>> listObserver = mediaLists -> {
        if (mediaLists != null) {
            this.listsLoadPane.setVisible(false);
            ControllerUtils.setItems(this.listsView, mediaLists);
        }
    };
    @FXML
    private ListView<Medium> listMediaView;
    @FXML
    private ListView<TocEpisode> mediumContentView;
    private final Observer<PagedList<TocEpisode>> episodesObserver = episodes -> {
        if (episodes != null) {
            this.mediumLoadPane.setVisible(false);
            this.mediumContentView.getItems().setAll(episodes);
        }
    };
    private final Observer<List<SimpleMedium>> listItemsObserver = mediumItems -> {
        if (mediumItems != null) {
            this.listLoadPane.setVisible(false);
            if (!this.listMediaView.getItems().equals(mediumItems)) {
                this.mediumContentView.getItems().clear();
                this.listMediaView.getItems().setAll(mediumItems);
            }
        } else {
            this.mediumContentView.getItems().clear();
        }
    };
    private ObjectBinding<EpisodeFilter> episodeFilterBinding;
    private MediumDisplayController mediumDisplayController = null;
    private final Observer<MediumSetting> settingObserver = mediumSetting -> {
        if (this.mediumDisplayController == null) {
            this.mediumDisplayController = ControllerUtils.load("/mediumDisplay.fxml", node -> this.detailPane.setContent(node));
        }
        this.mediumDisplayController.setMedium(mediumSetting);
    };
    private LiveData<PagedList<TocEpisode>> episodesLiveData;
    private LiveData<MediumSetting> mediumSettingLiveData;
    private final InvalidationListener mediumListener = observable -> selectMedium();
    private LiveData<List<SimpleMedium>> listItemsLiveData;
    private final InvalidationListener listsListener = observable -> {
        final MediaList item = this.listsView.getSelectionModel().getSelectedItem();

        if (item == null) {
            return;
        }
        if (this.listItemsLiveData != null) {
            this.listItemsLiveData.removeObserver(this.listItemsObserver);
        }
        this.listItemsLiveData = ApplicationConfig
                .getLiveDataRepository()
                .flatMap(repository -> repository.getSimpleMediumItems(
                        item.getListId(),
                        item instanceof ExternalMediaList
                ));
        this.listLoadPane.setVisible(true);
        this.listItemsLiveData.observe(this.listItemsObserver);
    };
    private LiveData<List<MediaList>> listLiveData;

    public void initialize() {
        final LiveData<Repository> repositorySingle = ApplicationConfig.getLiveDataRepository();
        this.listLiveData = repositorySingle.flatMap(Repository::getLists);
        this.listsLoadPane.setVisible(true);

        this.listsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.listMediaView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.mediumContentView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.setListsViewContextMenu();
        this.setMediaViewContextMenu();
        this.setEpisodeViewContextMenu();

        this.listsView.setCellFactory(param -> new MediaListCell());
        this.listMediaView.setCellFactory(param -> new ListMediumCell());

        Image lockedImage = new Image("/ic_lock_dark.png", true);
        Image readImage = new Image("/ic_episode_read_icon.png", true);
        Image onlineImage = new Image("/ic_open_browser_icon.png", true);
        Image localImage = new Image("/ic_open_local_icon.png", true);

        this.mediumContentView.setCellFactory(param -> {
            final TocEpisodeCell cell = new TocEpisodeCell(lockedImage, readImage, onlineImage, localImage);
            cell.currentMediumProperty().bind(this.listMediaView.getSelectionModel().selectedItemProperty());
            return cell;
        });

        this.scrollToEpisodeField.setTextFormatter(this.scrollToEpisodeFormatter);
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
    }

    private void setListsViewContextMenu() {
        // TODO: implement
    }

    private void setMediaViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        Menu addToListMenu = ControllerUtils.getAddToListMenu(this.listLiveData, this.listMediaView.getSelectionModel());
        contextMenu.getItems().add(addToListMenu);

        Menu moveToListMenu = ControllerUtils.getMoveToListMenu(
                this.listLiveData,
                this.listsView.getSelectionModel(),
                this.listMediaView.getSelectionModel()
        );
        contextMenu.getItems().add(moveToListMenu);

        final MenuItem remove = new MenuItem("Remove from List");
        remove.setOnAction(event -> {
            final MediaList list = this.listsView.getSelectionModel().getSelectedItem();

            if (list == null) {
                System.out.println("Cannot remove from a List: No List selected");
                return;
            }
            ControllerUtils.doMediumRepoAction(
                    String.format("Removed from List %s", list.getName()),
                    (repository, mediumIds) -> repository.removeItemFromList(list.getListId(), mediumIds),
                    this.listMediaView.getSelectionModel()
            );
        });
        contextMenu.getItems().add(remove);

        this.listMediaView.setContextMenu(contextMenu);
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

        final int mediumId = this.listMediaView.getSelectionModel().getSelectedItem().getMediumId();
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
        if (this.listLiveData != null) {
            this.listLiveData.observe(this.listObserver);
        }

        // first remove, then add to ensure that it is registered only once
        this.listMediaView.getSelectionModel().selectedItemProperty().removeListener(this.mediumListener);
        this.listMediaView.getSelectionModel().selectedItemProperty().addListener(this.mediumListener);

        this.listsView.getSelectionModel().selectedItemProperty().removeListener(this.listsListener);
        this.listsView.getSelectionModel().selectedItemProperty().addListener(this.listsListener);
    }

    @Override
    public void onDetach() {
        if (this.episodesLiveData != null) {
            this.episodesLiveData.removeObserver(this.episodesObserver);
        }
        if (this.mediumSettingLiveData != null) {
            this.mediumSettingLiveData.removeObserver(this.settingObserver);
        }
        if (this.listLiveData != null) {
            this.listLiveData.removeObserver(this.listObserver);
        }
        if (this.listItemsLiveData != null) {
            this.listItemsLiveData.removeObserver(this.listItemsObserver);
        }
        this.listsView.getSelectionModel().selectedItemProperty().removeListener(this.listsListener);
        this.listMediaView.getSelectionModel().selectedItemProperty().removeListener(this.mediumListener);
    }

    private void selectMedium() {
        LiveData<Repository> repositorySingle = ApplicationConfig.getLiveDataRepository();
        final Medium selectedItem = this.listMediaView.getSelectionModel().getSelectedItem();
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
        TextFormatter<Integer> formatter;
    }

    private enum MediumSorting {
    }

    private static class MediaListCell extends ListCell<MediaList> {
        private static final Pattern urlPattern = Pattern.compile("^https?://(www\\.)?(.+?)\\.\\w+/?.+$");

        @Override
        protected void updateItem(MediaList item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                String prefix = "Internal";
                if (item instanceof ExternalMediaList) {
                    prefix = ((ExternalMediaList) item).getUrl();
                    final Matcher matcher = urlPattern.matcher(prefix);

                    if (matcher.matches()) {
                        prefix = matcher.group(2);

                        if (prefix.length() > 1) {
                            prefix = Character.toUpperCase(prefix.charAt(0)) + prefix.substring(1);
                        }
                    }
                }
                setText(prefix + " - " + item.getName());
            }
        }
    }

    private static class ListMediumCell extends ListCell<Medium> {
        ListMediumCell() {

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

}
