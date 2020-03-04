package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.BiConsumerEx;
import com.mytlogos.enterprisedesktop.tools.TriConsumerEx;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 */
public class ListViewController implements Attachable {
    public ChoiceBox<SavedFilter> savedFilterBox;
    public ChoiceBox<ReadFilter> readFilterBox;
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
    @FXML
    private ListView<Medium> listMediaView;
    @FXML
    private ListView<TocEpisode> mediumContentView;

    private TextFormatter<Double> scrollToEpisodeFormatter = ControllerUtils.doubleTextFormatter();
    private ObjectBinding<EpisodeFilter> episodeFilterBinding;
    private MediumDisplayController mediumDisplayController = null;
    private LiveData<Repository> repositoryData;

    public void initialize() {
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

        this.mediumContentView.setCellFactory(param -> new TocEpisodeCell(lockedImage, readImage, onlineImage, localImage));

        this.scrollToEpisodeField.setTextFormatter(this.scrollToEpisodeFormatter);
        this.episodeSorting.setConverter(new MainController.DisplayConverter<>(EpisodeSorting.values()));
        this.episodeSorting.getItems().setAll(EpisodeSorting.values());
        this.episodeSorting.getSelectionModel().selectFirst();
        this.episodeFilterBinding = Bindings.createObjectBinding(
                () -> new EpisodeFilter(this.readFilterBox.getValue(), this.savedFilterBox.getValue()),
                this.readFilterBox.valueProperty(),
                this.savedFilterBox.valueProperty()
        );
    }

    private void setListsViewContextMenu() {

    }

    private void setMediaViewContextMenu() {

    }

    private void setEpisodeViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem setReadItem = new MenuItem();
        setReadItem.setText("Set Read");
        setReadItem.setOnAction(event -> doEpisodeRepoAction(
                (repository, mediumId) -> repository.updateAllRead(mediumId, true),
                (repository, ids, mediumId) -> repository.updateRead(mediumId, true))
        );

        MenuItem setUnreadItem = new MenuItem();
        setUnreadItem.setText("Set Unread");
        setUnreadItem.setOnAction(event -> doEpisodeRepoAction(
                (repository, mediumId) -> repository.updateAllRead(mediumId, false),
                (repository, ids, mediumId) -> repository.updateRead(mediumId, false))
        );

        MenuItem downloadItem = new MenuItem();
        downloadItem.setText("Download");
        downloadItem.setOnAction(event -> doEpisodeRepoAction(Repository::downloadAll, Repository::download));

        MenuItem deleteItem = new MenuItem();
        deleteItem.setText("Delete local");
        deleteItem.setOnAction(event -> doEpisodeRepoAction(Repository::deleteAllLocalEpisodes, Repository::deleteLocalEpisodes));

        MenuItem reloadItem = new MenuItem();
        reloadItem.setText("Reload");
        reloadItem.setOnAction(event -> doEpisodeRepoAction(
                Repository::reloadAll,
                (repository, ids, mediumId) -> repository.reload(ids)
        ));

        contextMenu.getItems().addAll(setReadItem, setUnreadItem, downloadItem, deleteItem, reloadItem);
        this.mediumContentView.setContextMenu(contextMenu);
    }

    private void doEpisodeRepoAction(BiConsumerEx<Repository, Integer> allConsumer, TriConsumerEx<Repository, Set<Integer>, Integer> idsConsumer) {
        final Repository repository = ApplicationConfig.getRepository();
        List<TocEpisode> selectedItems = this.mediumContentView.getSelectionModel().getSelectedItems();

        final int mediumId = this.listMediaView.getSelectionModel().getSelectedItem().getMediumId();
        if (selectedItems.size() == this.mediumContentView.getItems().size()) {
            try {
                allConsumer.accept(repository, mediumId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Set<Integer> ids = new HashSet<>();
            for (TocEpisode item : selectedItems) {
                ids.add(item.getEpisodeId());
            }
            try {
                idsConsumer.accept(repository, ids, mediumId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach() {
        if (this.episodeFilterBinding == null) {
            System.err.println("onAttach called before initialize");
            return;
        }
        final LiveData<Repository> repositorySingle = ApplicationConfig.getLiveDataRepository();

        this.listMediaView.getSelectionModel().selectedItemProperty().addListener(LiveData -> selectMedium(repositorySingle));

        final ReadOnlyObjectProperty<EpisodeSorting> selectedEpisodeSorting = this.episodeSorting.getSelectionModel().selectedItemProperty();
        repositorySingle.flatMap(Repository::getLists).observe(mediaLists -> this.listsView.getItems().setAll(mediaLists));

        subscribePublisher(
                this.listsView.getSelectionModel().selectedItemProperty(),
                mediaList -> repositorySingle.flatMap(repository ->
                        repository.getMediumItems(
                                mediaList.getListId(),
                                mediaList instanceof ExternalMediaList
                        )
                ),
                mediumItems -> {
                    this.mediumContentView.getItems().clear();
                    this.listMediaView.getItems().setAll(mediumItems);
                }
        );
        subscribePublisher(
                this.listMediaView.getSelectionModel().selectedItemProperty(),
                medium -> ControllerUtils.combineLatest(
                        repositorySingle,
                        this.episodeFilterBinding,
                        selectedEpisodeSorting,
                        (repository, episodeFilter, episodeSorting) -> {
                            System.out.println("fetching new episodes");
                            return repository.getToc(
                                    medium.getMediumId(),
                                    episodeSorting,
                                    episodeFilter.readFilter,
                                    episodeFilter.savedFilter
                            );
                        }
                ).flatMap(pagedListLiveData -> pagedListLiveData),
                episodes -> this.mediumContentView.getItems().setAll(episodes)
        );
    }

    private void selectMedium(LiveData<Repository> repositorySingle) {
        final Medium selectedItem = this.listMediaView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        this.repositoryData = repositorySingle;
        repositorySingle
                .flatMap(repository -> {
                    final int mediumId = selectedItem.getMediumId();
                    return repository.getMediumSettings(mediumId);
                })
                .observe(mediumSetting -> {
                    if (this.mediumDisplayController == null) {
                        this.mediumDisplayController = ControllerUtils.load("/mediumDisplay.fxml");
                    }
                    this.mediumDisplayController.setMedium(mediumSetting);
                    this.detailPane.setContent(this.mediumDisplayController.getRoot());
                });
    }

    private <T, R> void subscribePublisher(ObservableValue<T> value, Function<T, LiveData<R>> mapFunction, Consumer<R> consumer) {
        ControllerUtils.fxObservableToLiveData(value).flatMap(mapFunction).observe(consumer::accept);
    }

    @Override
    public void onDetach() {

    }

    @FXML
    private void scrollToEpisode() {
        TextFormatter<Integer> formatter;
    }

    private enum MediumSorting {
    }

    private static class MediaListCell extends ListCell<MediaList> {
        @Override
        protected void updateItem(MediaList item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                String url = "Internal";
                if (item instanceof ExternalMediaList) {
                    url = ((ExternalMediaList) item).getUrl();
                }
                setText(url + " - " + item.getName());
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

    private static class TocEpisodeCell extends ListCell<TocEpisode> {
        private final Image lockedImage;
        private final Image readImage;
        private final Image onlineImage;
        private final Image localImage;
        private VBox root;
        @FXML
        private Text content;
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

        private TocEpisodeCell(Image lockedImage, Image readImage, Image onlineImage, Image localImage) {
            this.lockedImage = lockedImage;
            this.readImage = readImage;
            this.onlineImage = onlineImage;
            this.localImage = localImage;
        }

        public void initialize() {
            System.out.println("initializing item");
        }

        @Override
        protected void updateItem(TocEpisode item, boolean empty) {
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
                    final List<Release> releases = item.getReleases();
                    final String title = releases
                            .stream()
                            .max(Comparator.comparingInt(release -> release.getTitle().length()))
                            .map(Release::getTitle)
                            .orElse("N/A");

                    final String latestRelease = releases
                            .stream()
                            .min(Comparator.comparing(Release::getReleaseDate))
                            .map(Release::getReleaseDate)
                            .map(Formatter::format)
                            .orElse("N/A");

                    this.topLeftContent.setText(Formatter.format(item));
                    this.topRightContent.setText(latestRelease);
                    this.content.setText(title);
                    final boolean locked = releases.stream().allMatch(Release::isLocked) && !releases.isEmpty();
                    final boolean hasOnline = releases.stream().map(Release::getUrl).anyMatch(s -> s != null && !s.isEmpty());
                    this.lockedView.setVisible(locked);
                    this.readView.setOpacity((item.getProgress() + 0.25) / (1.25));
                    this.onlineView.setOpacity(hasOnline ? 1 : 0.25);
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
