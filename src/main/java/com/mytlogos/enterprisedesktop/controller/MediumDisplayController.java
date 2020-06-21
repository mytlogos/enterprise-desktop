package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.model.MediumSetting;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Text;

/**
 *
 */
public class MediumDisplayController {
    @FXML
    private Button saveMediumBtn;
    @FXML
    private ListView tocListView;
    @FXML
    private Button addTocBtn;
    @FXML
    private TextField urlField;
    @FXML
    private Parent root;
    @FXML
    private TextField titleField;
    @FXML
    private RadioButton textBtn;
    @FXML
    private RadioButton imageBtn;
    @FXML
    private RadioButton videoBtn;
    @FXML
    private RadioButton audioBtn;
    @FXML
    private ToggleButton autoDownloadBtn;
    @FXML
    private TextField seriesField;
    @FXML
    private TextField universeField;
    @FXML
    private Text lastSeenText;
    @FXML
    private Text lastEpisodeText;
    @FXML
    private Text lastUpdatedText;
    @FXML
    private Text averageReleaseText;
    @FXML
    private TextField authorField;
    @FXML
    private TextField artistField;
    @FXML
    private TextField tlStateField;
    @FXML
    private TextField stateCooField;
    @FXML
    private TextField cooField;
    @FXML
    private TextField langOfOriginField;
    @FXML
    private TextField languageField;
    @FXML
    private ToggleGroup mediumMedium;

    private final ObjectProperty<MediumSetting> mediumSetting = new SimpleObjectProperty<>();

    Parent getRoot() {
        return this.root;
    }

    public void initialize() {
        this.mediumSetting.addListener(observable -> {
            final MediumSetting setting = this.mediumSetting.get();
            if (setting == null) {
                return;
            }

            this.titleField.setText(setting.getTitle());
            this.artistField.setText(setting.getArtist());
            this.authorField.setText(setting.getAuthor());
            this.tlStateField.setText(setting.getStateTL().getDisplayValue());
            this.cooField.setText(setting.getCountryOfOrigin());
            this.stateCooField.setText(setting.getStateOrigin().getDisplayValue());
            this.langOfOriginField.setText(setting.getLanguageOfOrigin());
            this.seriesField.setText(setting.getSeries());
            this.universeField.setText(setting.getUniverse());
            this.languageField.setText(setting.getLang());
            this.lastSeenText.setText(setting.getCurrentReadEpisode() + "");
            this.lastEpisodeText.setText(setting.getLastEpisode() + "");
            this.lastUpdatedText.setText(Formatter.format(setting.getLastUpdated()));
            this.averageReleaseText.setText("-1");
            this.autoDownloadBtn.setSelected(setting.isToDownload());
            ControllerUtils.setMedium(setting.getMedium(), this.textBtn, this.imageBtn, this.videoBtn, this.audioBtn);
        });
    }

    public void setMedium(MediumSetting setting) {
        this.mediumSetting.set(setting);
    }

    @FXML
    private void addToc() {
        final String url = this.urlField.getText();
        System.out.println("adding url: " + url);
    }

    @FXML
    private void saveMedium() {
        System.out.println("saving medium");
    }
}
