package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.model.DisplayRelease;
import com.mytlogos.enterprisedesktop.model.EpisodeFilter;
import com.mytlogos.enterprisedesktop.tools.BiConsumerEx;
import com.mytlogos.enterprisedesktop.tools.TriConsumerEx;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.reactivestreams.Publisher;

import java.util.*;
import java.util.function.BiConsumer;

/**
 *
 */
public class EpisodeViewController implements Attachable {
    private Disposable episodeDisposable;
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

    private ObjectBinding<ReleaseFilter> episodeFilterBinding;

    public void initialize() {
        this.savedFilter.setConverter(new MainController.DisplayConverter<>(SavedFilter.values()));
        this.readFilter.setConverter(new MainController.DisplayConverter<>(ReadFilter.values()));
        this.minEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));
        this.maxEpisodeIndex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 99999, -1));

        Image lockedImage = new Image("/ic_lock_dark.png", true);
        Image readImage = new Image("/ic_episode_read_icon.png", true);
        Image onlineImage = new Image("/ic_open_browser_icon.png", true);
        Image localImage = new Image("/ic_open_local_icon.png", true);

        this.setEpisodeViewContextMenu();
        this.episodeFilterBinding = Bindings.createObjectBinding(
                () -> new ReleaseFilter(
                        this.readFilter.getValue(),
                        this.savedFilter.getValue(),
                        ControllerUtils.getMedium(this.showTextBox, this.showImageBox, this.showVideoBox, this.showAudioBox),
                        this.minEpisodeIndex.getValue(),
                        this.maxEpisodeIndex.getValue(),
                        latestOnly.isSelected()
                ),
                this.readFilter.valueProperty(),
                this.savedFilter.valueProperty(),
                this.showAudioBox.selectedProperty(),
                this.showImageBox.selectedProperty(),
                this.showTextBox.selectedProperty(),
                this.showVideoBox.selectedProperty(),
                this.latestOnly.selectedProperty(),
                this.minEpisodeIndex.valueProperty(),
                this.maxEpisodeIndex.valueProperty()
        );
        this.episodes.setCellFactory(param -> new DisplayReleaseCell(lockedImage, readImage, onlineImage, localImage));
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
        this.episodes.setContextMenu(contextMenu);
    }

    private void doEpisodeRepoAction(BiConsumerEx<Repository, Integer> allConsumer, TriConsumerEx<Repository, Set<Integer>, Integer> idsConsumer) {
        final Repository repository = ApplicationConfig.getRepository();
        List<DisplayRelease> selectedItems = this.episodes.getSelectionModel().getSelectedItems();

        Map<Integer, Set<Integer>> mediumEpisodeIds = new HashMap<>();

        for (DisplayRelease item : selectedItems) {
            mediumEpisodeIds.computeIfAbsent(item.getMediumId(), integer -> new HashSet<>()).add(item.getEpisodeId());
        }
        for (Map.Entry<Integer, Set<Integer>> entry : mediumEpisodeIds.entrySet()) {
            if (entry.getValue().size() == this.episodes.getItems().size()) {
                try {
                    allConsumer.accept(repository, entry.getKey());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    idsConsumer.accept(repository, entry.getValue(), entry.getKey());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onAttach() {
        final Flowable<Repository> repositorySingle = ApplicationConfig.getFlowableRepository();

        this.episodeDisposable = repositorySingle
                .subscribeOn(Schedulers.io())
                .flatMap(t -> {
                    if (t == null) {
                        return Flowable.empty();
                    }

                    return ControllerUtils.combineLatest(
                            repositorySingle,
                            this.episodeFilterBinding,
                            (repository, episodeFilter) -> {
                                System.out.println("fetching all episodes");
                                return repository.getDisplayEpisodes(
                                        episodeFilter.savedFilter,
                                        episodeFilter.medium,
                                        episodeFilter.readFilter,
                                        episodeFilter.minEpisodeIndex,
                                        episodeFilter.maxEpisodeIndex,
                                        episodeFilter.selected
                                );
                            });
                })
                .flatMap(pagedListFlowable -> pagedListFlowable)
                .subscribeOn(JavaFxScheduler.platform())
                .subscribe(releases -> {
                    if (releases == null) {
                        this.episodes.getItems().clear();
                        return;
                    }
                    this.episodes.getItems().setAll(releases);
                }, Throwable::printStackTrace);
    }

    @Override
    public void onDetach() {
        if (this.episodeDisposable != null) {
            this.episodeDisposable.dispose();
        }
    }

    private <T, R> Disposable subscribePublisher(ObservableValue<T> value, Function<T, Publisher<R>> mapFunction, Consumer<R> consumer) {
        return JavaFxObservable
                .valuesOf(value)
                .toFlowable(BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .flatMap(t -> {
                    if (t == null) {
                        return Flowable.empty();
                    }
                    return mapFunction.apply(t);
                })
                .subscribeOn(JavaFxScheduler.platform())
                .subscribe(r -> {
                    if (r == null) {
                        return;
                    }
                    consumer.accept(r);
                }, Throwable::printStackTrace);
    }

    private static class DisplayReleaseCell extends ListCell<DisplayRelease> {
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

        private DisplayReleaseCell(Image lockedImage, Image readImage, Image onlineImage, Image localImage) {
            this.lockedImage = lockedImage;
            this.readImage = readImage;
            this.onlineImage = onlineImage;
            this.localImage = localImage;
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
                    final String title = item.getMediumTitle() + " - " + item.getTitle();

                    final String latestRelease = Formatter.format(item.getReleaseDate());

                    this.topLeftContent.setText(Formatter.format(item));

                    this.topRightContent.setText(latestRelease);
                    this.content.setText(title);

                    this.lockedView.setVisible(item.isLocked());
                    this.readView.setOpacity(item.isRead() ? 1 : 0.25);
                    this.onlineView.setOpacity(item.getUrl() != null && !item.getUrl().isEmpty() ? 1 : 0.25);
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

    private static class ReleaseFilter extends EpisodeFilter {
        public final int medium;
        final int minEpisodeIndex;
        final int maxEpisodeIndex;
        final boolean selected;

        ReleaseFilter(ReadFilter readFilter, SavedFilter savedFilter, int medium, int minEpisodeIndex, int maxEpisodeIndex, boolean selected) {
            super(readFilter, savedFilter);
            this.medium = medium;
            this.minEpisodeIndex = minEpisodeIndex;
            this.maxEpisodeIndex = maxEpisodeIndex;
            this.selected = selected;
        }

        public ReleaseFilter(byte readFilter, byte savedFilter, int medium, int minEpisodeIndex, int maxEpisodeIndex, boolean selected) {
            super(readFilter, savedFilter);
            this.medium = medium;
            this.minEpisodeIndex = minEpisodeIndex;
            this.maxEpisodeIndex = maxEpisodeIndex;
            this.selected = selected;
        }
    }
}
