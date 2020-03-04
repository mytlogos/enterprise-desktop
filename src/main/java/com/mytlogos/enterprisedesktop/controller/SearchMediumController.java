package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.Utils;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;
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

    public void initialize() {
        this.resultsView.setCellFactory(param -> new ResultCell());
        this.resultsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        final ContextMenu value = new ContextMenu();
        final MenuItem openItem = new MenuItem("Open Selected");
        openItem.setOnAction(event -> {
            for (SearchResponse item : this.resultsView.getSelectionModel().getSelectedItems()) {
                try {
                    Desktop.getDesktop().browse(URI.create(item.link));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        final MenuItem addItem = new MenuItem("Add Selected");
        addItem.setOnAction(event -> ApplicationConfig.getRepository().getInternLists()
                .firstElement()
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
                    int failCount = 0;
                    for (Boolean aBoolean : booleans) {
                        if (aBoolean == null || !aBoolean) {
                            failCount++;
                        }
                    }
                    if (failCount > 1) {
                        System.out.printf("%d failed Additions%n", failCount);
                    } else {
                        System.out.println("Successfully added all Items");
                    }
                }));
        value.getItems().addAll(openItem, addItem);
        this.resultsView.setContextMenu(value);

        this.mediumChoiceBox.getItems().addAll(MediumType.TEXT, MediumType.AUDIO, MediumType.VIDEO, MediumType.IMAGE);
        this.mediumChoiceBox.getSelectionModel().selectFirst();
        this.mediumChoiceBox.setConverter(new StringConverter<Integer>() {
            private Map<String, Integer> map = new HashMap<>();

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
    }

    @Override
    public void onAttach() {
        final ObjectBinding<SearchRequest> requestBinding = Bindings.createObjectBinding(
                () -> new SearchRequest(this.searchField.getText(), this.mediumChoiceBox.getValue()),
                this.mediumChoiceBox.valueProperty(),
                this.searchField.textProperty()
        );
        this.debouncedRequest = JavaFxObservable.valuesOf(requestBinding)
                .debounce(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .map(searchRequest -> {
                    if (searchRequest.title.isEmpty()) {
                        return Collections.<SearchResponse>emptyList();
                    } else {
                        return ApplicationConfig.getRepository().requestSearch(searchRequest);
                    }
                })
                .subscribe(list -> this.resultsView.getItems().setAll(list), Throwable::printStackTrace);
    }

    @Override
    public void onDetach() {
        this.debouncedRequest.dispose();
    }

    private static class ResultCell extends ListCell<SearchResponse> {
        private HBox root = new HBox();
        private ImageView cover = new ImageView();
        private Text text = new Text();
        private Text link = new Text();
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
