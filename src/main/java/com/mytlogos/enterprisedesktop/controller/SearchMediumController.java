package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.Utils;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.controlsfx.control.Notifications;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class SearchMediumController implements Attachable {
    @FXML
    private TextField searchField;
    @FXML
    private ListView<SearchResponse> resultsView;
    @FXML
    private ChoiceBox<Integer> mediumChoiceBox;
    private Disposable debouncedRequest;
    private ObjectBinding<SearchRequest> requestBinding;

    public void initialize() {
        this.resultsView.setCellFactory(param -> new ResultCell());
        this.resultsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        final ContextMenu value = new ContextMenu();
        final MenuItem openItem = new MenuItem("Open Selected");
        openItem.setOnAction(event -> {
            for (SearchResponse item : this.resultsView.getSelectionModel().getSelectedItems()) {
                ControllerUtils.openUrl(item.link);
            }
        });
        final MenuItem addItem = new MenuItem("Add Selected");
        addItem.setOnAction(event -> ApplicationConfig.getRepository().getInternLists()
                .firstNonNullElement()
                .thenCompose(mediaLists -> {
                    MediaList list = null;
                    for (MediaList mediaList : mediaLists) {
                        if (Objects.equals(mediaList.getName(), "Standard")) {
                            list = mediaList;
                            break;
                        }
                    }

                    if (list == null) {
                        return CompletableFuture.completedFuture(Collections.emptyList());
                    }
                    List<CompletableFuture<Boolean>> futures = new ArrayList<>();
                    for (SearchResponse item : this.resultsView.getSelectionModel().getSelectedItems()) {
                        final MediumInWaitImpl mediumInWait = new MediumInWaitImpl(item.title, this.mediumChoiceBox.getValue(), item.coverUrl);
                        futures.add(ApplicationConfig.getRepository().createMedium(mediumInWait, Collections.emptyList(), list));
                    }
                    return Utils.finishAll(futures);
                })
                .whenComplete((booleans, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        return;
                    }
                    final long succeeded = booleans.stream().filter(success -> success != null && success).count();
                    final long failed = booleans.size() - succeeded;

                    final String title;
                    if (failed > 0 && succeeded > 0) {
                        title = String.format("Add Selected: %d succeeded, %d failed", succeeded, failed);
                    } else if (failed > 0) {
                        title = "Add Selected: all failed";
                    } else {
                        title = "Add Selected: all succeeded";
                    }
                    Platform.runLater(() -> Notifications.create().title(title).show());
                }));
        value.getItems().addAll(openItem, addItem);
        this.resultsView.setContextMenu(value);

        this.mediumChoiceBox.getItems().addAll(MediumType.TEXT, MediumType.AUDIO, MediumType.VIDEO, MediumType.IMAGE);
        this.mediumChoiceBox.getSelectionModel().selectFirst();
        this.mediumChoiceBox.setConverter(new StringConverter<Integer>() {
            private final Map<String, Integer> map = new HashMap<>();

            {
                map.put("Text", MediumType.TEXT);
                map.put("Image", MediumType.IMAGE);
                map.put("Video", MediumType.VIDEO);
                map.put("Audio", MediumType.AUDIO);
            }

            @Override
            public String toString(Integer object) {
                for (Map.Entry<String, Integer> entry : this.map.entrySet()) {
                    if (Objects.equals(entry.getValue(), object)) {
                        return entry.getKey();
                    }
                }
                return null;
            }

            @Override
            public Integer fromString(String string) {
                return this.map.get(string);
            }
        });
        this.requestBinding = Bindings.createObjectBinding(
                () -> new SearchRequest(this.searchField.getText(), this.mediumChoiceBox.getValue()),
                this.mediumChoiceBox.valueProperty(),
                this.searchField.textProperty()
        );
    }

    @Override
    public void onAttach() {
        this.debouncedRequest = JavaFxObservable.valuesOf(this.requestBinding)
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .map(searchRequest -> {
                    if (searchRequest.title.isEmpty()) {
                        return Collections.<SearchResponse>emptyList();
                    } else {
                        final Thread thread = Thread.currentThread();
                        System.out.printf(
                                "Request Searching on Fx Thread: %b, Thread Id %d, Thread Name: %s%n",
                                Platform.isFxApplicationThread(),
                                thread.getId(),
                                thread.getName()
                        );
                        return ApplicationConfig.getRepository().requestSearch(searchRequest);
                    }
                })
                .observeOn(JavaFxScheduler.platform())
                .subscribe(list -> {
                    final Thread thread = Thread.currentThread();
                    System.out.printf(
                            "Finished Searching on Fx Thread: %b, Thread Id %d, Thread Name: %s%n",
                            Platform.isFxApplicationThread(),
                            thread.getId(),
                            thread.getName()
                    );
                    this.resultsView.getItems().setAll(list);
                }, Throwable::printStackTrace);
    }

    @Override
    public void onDetach() {
        this.debouncedRequest.dispose();
    }

    private static class ResultCell extends ListCell<SearchResponse> {
        private final HBox root = new HBox();
        private final ImageView cover = new ImageView();
        private final Text text = new Text();
        private final Text link = new Text();
        private boolean init = false;

        @Override
        protected void updateItem(SearchResponse item, boolean empty) {
            super.updateItem(item, empty);

            if (!this.init) {
                this.init = true;
                VBox vBox = new VBox();
                vBox.getChildren().addAll(this.text, this.link);
                vBox.setSpacing(10);

                this.root.setSpacing(5);
                this.root.setPadding(new Insets(5));
                this.root.getChildren().addAll(this.cover, vBox);

                this.cover.setPreserveRatio(true);
                this.cover.setFitHeight(50);
            }

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(this.root);

                this.text.setText(item.title);
                this.link.setText(item.link);

                if (item.coverUrl != null && !item.coverUrl.isEmpty()) {
                    this.cover.setImage(new Image(item.coverUrl, true));
                } else {
                    this.cover.setImage(null);
                }
            }
        }
    }
}
