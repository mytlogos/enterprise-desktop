package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.model.ChapterPage;
import com.mytlogos.enterprisedesktop.model.SimpleEpisode;
import com.mytlogos.enterprisedesktop.model.SimpleMedium;
import com.mytlogos.enterprisedesktop.tools.FileTools;
import com.mytlogos.enterprisedesktop.tools.ImageContentTool;
import com.mytlogos.enterprisedesktop.tools.TextContentTool;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.controlsfx.control.Notifications;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class ImageViewController {

    @FXML
    private Spinner<Integer> zoom;
    @FXML
    private ToggleButton fitToPageBtn;
    @FXML
    private ListView<ChapterPage> list;
    @FXML
    private Label title;
    @FXML
    private ChoiceBox<SimpleEpisode> episodeBox;
    @FXML
    private Button previousBtn;
    @FXML
    private Button nextBtn;
    private IntegerProperty latestVisiblePage = new SimpleIntegerProperty(0);
    private EventHandler<KeyEvent> keyEventEventHandler = event -> {
        if (event.isControlDown()) {
            if (event.getCode() == KeyCode.MINUS) {
                this.zoom.decrement();
            } else if (event.getCode() == KeyCode.PLUS) {
                this.zoom.increment();
            } else if (event.getCode() == KeyCode.F) {
                this.fitToPageBtn.setSelected(!this.fitToPageBtn.isSelected());
            } else if (event.getCode() == KeyCode.LEFT) {
                this.episodeBox.getSelectionModel().selectPrevious();
            } else if (event.getCode() == KeyCode.RIGHT) {
                this.episodeBox.getSelectionModel().selectNext();
            }
        } else if (event.getCode().isArrowKey()) {
            // TODO 08.3.2020: scroll down, up, right, left a fixed amount (especially horizontal)
        } else if (event.getCode().isNavigationKey()) {
            final int latestPage = this.latestVisiblePage.get();

            if (event.getCode() == KeyCode.PAGE_DOWN) {
                final int lastIndex = this.list.getItems().size() - 1;
                if (latestPage < lastIndex) {
                    this.list.scrollTo(latestPage + 1);
                }
            } else if (event.getCode() == KeyCode.UP) {
                if (latestPage > 0) {
                    this.list.scrollTo(latestPage - 1);
                }
            } else if (event.getCode() == KeyCode.HOME) {
                this.list.scrollTo(0);
            } else if (event.getCode() == KeyCode.END) {
                this.list.scrollTo(this.list.getItems().size() - 1);
            }
        }
    };
    private Map<Integer, Set<ChapterPage>> episodePagePaths;
    private List<SimpleEpisode> simpleEpisodes;

    public void open(int mediumId, int currentEpisode) {
        final Repository repository = ApplicationConfig.getRepository();

        final ImageContentTool contentTool = FileTools.getImageContentTool();
        final SimpleMedium simpleMedium = repository.getSimpleMedium(mediumId);
        final List<Integer> episodes = repository.getSavedEpisodes(mediumId);
        this.title.setText(simpleMedium.getTitle());
        final String itemPath = contentTool.getItemPath(mediumId);

        if (itemPath == null || itemPath.isEmpty()) {
            Notifications.create().title("No Medium Directory available").show();
            return;
        }
        this.episodePagePaths = contentTool.getEpisodePagePaths(itemPath);
        episodes.retainAll(this.episodePagePaths.keySet());
        this.simpleEpisodes = repository.getSimpleEpisodes(episodes);

        this.episodeBox.getItems().setAll(this.simpleEpisodes);

        for (SimpleEpisode episode : this.simpleEpisodes) {
            if (episode.getEpisodeId() == currentEpisode) {
                this.episodeBox.getSelectionModel().select(episode);
                break;
            }
        }
    }

    public void initialize() {
        final Repository repository = ApplicationConfig.getRepository();
        this.latestVisiblePage.addListener((observable, oldValue, newValue) -> {
            final SimpleEpisode episode = this.episodeBox.getSelectionModel().getSelectedItem();
            if (episode == null || this.list.getItems().isEmpty()) {
                return;
            }
            float progress = this.latestVisiblePage.get() / ((float) this.list.getItems().size());

            if (episode.getProgress() < progress) {
                repository.updateProgress(episode.getEpisodeId(), progress);
            }
        });
        this.zoom.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200, 100, 10));
        this.list.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.setOnKeyPressed(this.keyEventEventHandler);
            }
            if (oldValue != null) {
                oldValue.removeEventHandler(KeyEvent.KEY_PRESSED, this.keyEventEventHandler);
            }
        });
        this.zoom.getValueFactory().setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return object == null ? "0%" : object + "%";
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string.replace("%", ""));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return 100;
                }
            }
        });

        this.episodeBox.setConverter(new StringConverter<SimpleEpisode>() {
            @Override
            public String toString(SimpleEpisode object) {
                return object == null ? "" : object.getFormattedTitle();
            }

            @Override
            public SimpleEpisode fromString(String string) {
                for (SimpleEpisode episode : simpleEpisodes) {
                    if (episode.getFormattedTitle().equals(string)) {
                        return episode;
                    }
                }
                return null;
            }
        });
        this.list.setCellFactory(param -> new ImageCell());

        this.previousBtn.setOnAction(event -> this.episodeBox.getSelectionModel().selectPrevious());
        this.nextBtn.setOnAction(event -> this.episodeBox.getSelectionModel().selectNext());

        this.episodeBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                this.previousBtn.setDisable(true);
                this.nextBtn.setDisable(true);
                return;
            }
            this.latestVisiblePage.set(0);
            final int index = this.episodeBox.getSelectionModel().getSelectedIndex();
            this.previousBtn.setDisable(index != 0);
            this.nextBtn.setDisable(index < this.episodeBox.getItems().size() - 1);
            this.list.getItems().setAll(episodePagePaths.get(newValue.getEpisodeId()));
        });
    }

    private class ImageCell extends ListCell<ChapterPage> {
        private ImageView imageView;

        public ImageCell() {
            ImageViewController.this.zoom.valueProperty().addListener(observable -> zoomImage());
            ImageViewController.this.fitToPageBtn.selectedProperty().addListener(observable -> zoomImage());
            ImageViewController.this.list.widthProperty().addListener(observable -> zoomImage());
            Bindings.selectBoolean(this.sceneProperty(), "window", "showing").addListener((observable, oldValue, newValue) -> {
                if (newValue != null && newValue) {
                    this.zoomImage();
                    if (this.getItem() != null) {
                        latestVisiblePage.set(this.getItem().getPage());
                    }
                }
            });
        }

        private void zoomImage() {
            if (this.imageView == null) {
                return;
            }
            if (fitToPageBtn.isSelected()) {
                final double width = list.getWidth();
                this.imageView.setFitWidth(width);
                return;
            }
            final Integer zoomValue = zoom.getValue();
            final Image image = this.imageView.getImage();

            if (image == null) {
                return;
            }

            final double width = image.getWidth();
            final double percent = zoomValue / 100d;
            this.imageView.setFitWidth(width * percent);
        }

        @Override
        protected void updateItem(ChapterPage item, boolean empty) {
            super.updateItem(item, empty);

            if (this.imageView == null) {
                this.imageView = new ImageView();
                this.imageView.setPreserveRatio(true);
            }

            setText(null);

            if (empty || item == null) {
                setGraphic(null);
                this.imageView.setImage(null);
            } else {
                String path = item.getPath();
                if (!path.startsWith("file:")) {
                    path = "file:" + path;
                }
                this.imageView.setImage(new Image(path, true));
                setGraphic(this.imageView);
            }
        }
    }

}
