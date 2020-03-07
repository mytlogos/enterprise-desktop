package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.model.ChapterPage;
import com.mytlogos.enterprisedesktop.model.SimpleEpisode;
import com.mytlogos.enterprisedesktop.model.SimpleMedium;
import com.mytlogos.enterprisedesktop.tools.FileTools;
import com.mytlogos.enterprisedesktop.tools.ImageContentTool;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private final int mediumId;
    @FXML
    private ListView<ChapterPage> list;
    @FXML
    private Text title;
    @FXML
    private ChoiceBox<SimpleEpisode> episodeBox;
    @FXML
    private Button previousBtn;
    @FXML
    private Button nextBtn;
    private int currentEpisode;

    public ImageViewController(int mediumId, int currentEpisode) {
        this.mediumId = mediumId;
        this.currentEpisode = currentEpisode;
    }

    public void initialize() {
        final Repository repository = ApplicationConfig.getRepository();
        final SimpleMedium simpleMedium = repository.getSimpleMedium(this.mediumId);
        final List<Integer> episodes = repository.getSavedEpisodes(this.mediumId);

        this.title.setText(simpleMedium.getTitle());

        final ImageContentTool contentTool = FileTools.getImageContentTool();
        final String itemPath = contentTool.getItemPath(this.mediumId);

        if (itemPath == null || itemPath.isEmpty()) {
            Notifications.create().title("No Medium Directory available").show();
            return;
        }
        final Map<Integer, Set<ChapterPage>> episodePagePaths = contentTool.getEpisodePagePaths(itemPath);
        episodes.retainAll(episodePagePaths.keySet());
        final List<SimpleEpisode> simpleEpisodes = repository.getSimpleEpisodes(episodes);

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
        this.episodeBox.getItems().setAll(simpleEpisodes);
        this.episodeBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            this.list.getItems().setAll(episodePagePaths.get(newValue.getEpisodeId()));
        });

        for (SimpleEpisode episode : simpleEpisodes) {
            if (episode.getEpisodeId() == this.currentEpisode) {
                this.episodeBox.getSelectionModel().select(episode);
                break;
            }
        }
    }

    private static class ImageCell extends ListCell<ChapterPage> {
        private ImageView imageView;

        @Override
        protected void updateItem(ChapterPage item, boolean empty) {
            super.updateItem(item, empty);

            if (this.imageView == null) {
                this.imageView = new ImageView();
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
