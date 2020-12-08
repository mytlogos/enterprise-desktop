package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.MediumSetting;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.List;

/**
 *
 */
public class MediumDisplayController {
    private final ObjectProperty<MediumSetting> mediumSetting = new SimpleObjectProperty<>();
    @FXML
    private HBox showMedium;
    @FXML
    private MediumTypes showMediumController;
    @FXML
    private ListView<String> tocListView;
    @FXML
    private Parent root;
    @FXML
    private Text titleField;
    @FXML
    private ToggleButton autoDownloadBtn;
    @FXML
    private Text seriesField;
    @FXML
    private Text universeField;
    @FXML
    private Text lastSeenText;
    @FXML
    private Text lastEpisodeText;
    @FXML
    private Text lastUpdatedText;
    @FXML
    private Text averageReleaseText;
    @FXML
    private Text authorField;
    @FXML
    private Text artistField;
    @FXML
    private Text tlStateField;
    @FXML
    private Text stateCooField;
    @FXML
    private Text cooField;
    @FXML
    private Text langOfOriginField;
    @FXML
    private Text languageField;
    private LiveData<List<String>> liveTocs;

    public void initialize() {
        this.showMediumController.setEditable(false);
        this.mediumSetting.addListener(observable -> {
            final MediumSetting setting = this.mediumSetting.get();
            if (setting == null) {
                return;
            }
            this.liveTocs = ApplicationConfig.getRepository().getToc(setting.getMediumId());
            this.liveTocs.observe(tocs -> {
                if (tocs == null) {
                    return;
                }
                this.tocListView.getItems().setAll(tocs);
            });

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
            this.showMediumController.setMedium(setting.getMedium());
        });
    }

    public void setMedium(MediumSetting setting) {
        this.mediumSetting.set(setting);
    }
}
