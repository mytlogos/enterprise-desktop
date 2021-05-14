package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.model.Medium;
import com.mytlogos.enterprisedesktop.model.Release;
import com.mytlogos.enterprisedesktop.model.TocEpisode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Comparator;
import java.util.List;

/**
 *
 */
class TocEpisodeCell extends ListCell<TocEpisode> {
    private final Image lockedImage;
    private final Image readImage;
    private final Image onlineImage;
    private final Image localImage;
    private final ObjectProperty<Medium> currentMedium = new SimpleObjectProperty<>();
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

    TocEpisodeCell(Image lockedImage, Image readImage, Image onlineImage, Image localImage) {
        this.lockedImage = lockedImage;
        this.readImage = readImage;
        this.onlineImage = onlineImage;
        this.localImage = localImage;
        this.setOnMouseClicked(event -> {
            final TocEpisode item = this.getItem();
            if (!event.getButton().equals(MouseButton.PRIMARY) || event.getClickCount() < 2) {
                return;
            }
            final Medium mediumItem = currentMedium.get();

            if (mediumItem == null) {
                return;
            }
            ControllerUtils.openEpisode(item, mediumItem.getMedium(), mediumItem.getMediumId());
        });
        this.listViewProperty().addListener((observable, oldValue, newValue) -> {
            this.prefWidthProperty().unbind();
            if (newValue != null) {
                this.prefWidthProperty().bind(newValue.widthProperty().subtract(20));
            }
        });
    }

    public ObjectProperty<Medium> currentMediumProperty() {
        return currentMedium;
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
        } else if (this.loadFailed) {
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

            final boolean locked = releases.stream().allMatch(Release::isLocked) && !releases.isEmpty();
            final boolean hasOnline = releases.stream().map(Release::getUrl).anyMatch(s -> s != null && !s.isEmpty());

            this.topLeftContent.setText(Formatter.format(item));
            this.topRightContent.setText(latestRelease);
            this.content.setText(title);
            this.lockedView.setVisible(locked);
            this.readView.setOpacity((item.getProgress() + 0.25) / (1.25));
            this.onlineView.setOpacity(hasOnline ? 1 : 0.25);
            this.localView.setOpacity(item.isSaved() ? 1 : 0.25);
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
