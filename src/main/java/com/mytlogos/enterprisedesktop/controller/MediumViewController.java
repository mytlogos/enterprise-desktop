package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.Formatter;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.sqlite.PagedList;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.background.sqlite.life.Observer;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.profile.DisplayEpisodeProfileBuilder;
import com.mytlogos.enterprisedesktop.tools.Utils;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class MediumViewController {
    private final XYChart.Series<Number, Number> series = new XYChart.Series<>();
    @FXML
    private LineChart<Number, Number> releaseChart;
    @FXML
    private ListView<MediaList> listsView;
    private final Observer<List<MediaList>> listsObserver = mediaLists -> {
        if (mediaLists == null) {
            return;
        }
        this.listsView.getItems().setAll(mediaLists);
    };
    @FXML
    private TextField listNameField;
    @FXML
    private Button addToListBtn;
    @FXML
    private HBox showMedium;
    @FXML
    private MediumTypes showMediumController;
    @FXML
    private Button saveMediumBtn;
    @FXML
    private ListView<String> tocListView;
    private final Observer<List<String>> tocsObserver = tocs -> {
        if (tocs == null) {
            return;
        }
        this.tocListView.getItems().setAll(tocs);
    };
    @FXML
    private Button addTocBtn;
    @FXML
    private TextField urlField;
    @FXML
    private Parent root;
    @FXML
    private TextField titleField;
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
    private final Observer<MediumSetting> settingsObserver = mediumSetting -> {
        if (mediumSetting == null) {
            return;
        }

        this.titleField.setText(mediumSetting.getTitle());
        this.artistField.setText(mediumSetting.getArtist());
        this.authorField.setText(mediumSetting.getAuthor());
        this.tlStateField.setText(mediumSetting.getStateTL().getDisplayValue());
        this.cooField.setText(mediumSetting.getCountryOfOrigin());
        this.stateCooField.setText(mediumSetting.getStateOrigin().getDisplayValue());
        this.langOfOriginField.setText(mediumSetting.getLanguageOfOrigin());
        this.seriesField.setText(mediumSetting.getSeries());
        this.universeField.setText(mediumSetting.getUniverse());
        this.languageField.setText(mediumSetting.getLang());
        this.lastSeenText.setText(mediumSetting.getCurrentReadEpisode() + "");
        this.lastEpisodeText.setText(mediumSetting.getLastEpisode() + "");
        this.lastUpdatedText.setText(Formatter.format(mediumSetting.getLastUpdated()));
        this.averageReleaseText.setText("-1");
        this.autoDownloadBtn.setSelected(mediumSetting.isToDownload());
        this.showMediumController.setMedium(mediumSetting.getMedium());
        this.updateReleaseChart(this.releaseLiveData.getValue());
    };
    private int mediumId;
    private LiveData<MediumSetting> mediumSettings;
    private final Observer<PagedList<DisplayRelease>> releaseObserver = this::updateReleaseChart;
    private LiveData<List<String>> toc;
    private LiveData<List<MediaList>> parentLists;
    private LiveData<PagedList<DisplayRelease>> releaseLiveData;

    public void initialize() {
        this.listsView.setCellFactory(param -> new ListCell<MediaList>() {
            @Override
            protected void updateItem(MediaList item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    this.setText(null);
                } else {
                    this.setText(item.getName());
                }
            }
        });
        final Repository repository = ApplicationConfig.getRepository();
        this.tocListView.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                final String item = this.tocListView.getSelectionModel().getSelectedItem();

                if (item == null || item.isEmpty()) {
                    return;
                }
                if (!repository.removeToc(this.mediumId, item)) {
                    System.out.println("Failed removing Toc");
                }
            }
        });
        this.listsView.setOnKeyTyped(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                final MediaList item = this.listsView.getSelectionModel().getSelectedItem();

                if (item == null) {
                    return;
                }
                repository.removeItemFromList(item.getListId(), this.mediumId).whenComplete((aBoolean, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                    }
                    if (aBoolean != null && !aBoolean) {
                        System.out.println("Failed removing Item");
                    }
                });
            }
        });
        this.releaseChart.getData().add(this.series);
    }

    void setMediumId(int mediumId) {
        this.mediumId = mediumId;
        final Repository repository = ApplicationConfig.getRepository();

        if (this.mediumSettings != null) {
            this.mediumSettings.removeObserver(this.settingsObserver);
        }
        this.mediumSettings = repository.getMediumSettings(mediumId);
        this.mediumSettings.observe(this.settingsObserver);

        if (this.toc != null) {
            this.toc.removeObserver(this.tocsObserver);
        }
        this.toc = repository.getToc(mediumId);
        this.toc.observe(this.tocsObserver);

        if (this.parentLists != null) {
            this.parentLists.removeObserver(this.listsObserver);
        }
        this.parentLists = repository.getParentLists(mediumId);
        this.parentLists.observe(this.listsObserver);

        this.releaseLiveData = repository.getDisplayEpisodes(new DisplayEpisodeProfileBuilder()
                .setFilterMediumIds(Collections.singletonList(this.mediumId))
                .create()
        );
        this.releaseLiveData.observe(this.releaseObserver);
    }

    private void updateReleaseChart(List<DisplayRelease> displayReleases) {
        if (displayReleases == null) {
            return;
        }
        Map<Integer, DisplayRelease> idValueMap = new HashMap<>();

        for (DisplayRelease release : displayReleases) {
            final DisplayRelease otherRelease = idValueMap.get(release.getEpisodeId());

            if (otherRelease == null || otherRelease.getReleaseDate().isAfter(release.getReleaseDate())) {
                idValueMap.put(release.getEpisodeId(), release);
            }
        }

        List<DisplayRelease> releases = new ArrayList<>(idValueMap.values());
        releases.sort(Comparator.comparing(DisplayRelease::getReleaseDate));

        Duration dataThreshold = Duration.ofHours(1);
        this.series.getData().clear();

        final MediumSetting setting = this.mediumSettings.getValue();

        for (int i = 1, point = 0; i < releases.size(); i++) {
            final DisplayRelease release = releases.get(i);
            final DisplayRelease previousRelease = releases.get(i - 1);

            final Duration duration = Duration.between(previousRelease.getReleaseDate(), release.getReleaseDate());

            if (duration.compareTo(dataThreshold) > 0) {
                this.series.getData().add(new XYChart.Data<>(point++, duration.toHours() / 24D));
            }

            if ((i + 1) == releases.size() && (setting == null || !setting.getStateTL().isEnd())) {
                final Duration current = Duration.between(release.getReleaseDate(), LocalDateTime.now());

                if (current.compareTo(dataThreshold) > 0) {
                    this.series.getData().add(new XYChart.Data<>(point++, current.toHours() / 24D));
                }
            }
        }
    }

    @FXML
    private void mergeWith() {
        Dialog<SimpleMedium> mediumDialog = new Dialog<>();

        final DialogPane pane = new DialogPane();
        pane.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.FINISH);
        pane.setHeaderText(String.format("Merge another Medium with '%s'", this.titleField.getText()));
        final TextField item = new TextField();

        final Repository repository = ApplicationConfig.getRepository();
        ObjectProperty<SimpleMedium> property = new SimpleObjectProperty<>();
        final Integer medium = this.showMediumController.getMedium();
        final LiveData<List<SimpleMedium>> data = repository.getSimpleMedium().map(list -> {
            if (list == null) {
                return null;
            }
            return list.stream().filter(simpleMedium -> MediumType.is(simpleMedium.getMedium(), medium)).collect(Collectors.toList());
        });
        ControllerUtils.addAutoCompletionBinding(item, data, SimpleMedium::getTitle, property::setValue);
        data.observe(Utils.emptyObserver());
        pane.setContent(item);

        mediumDialog.setDialogPane(pane);
        mediumDialog.setResultConverter(param -> {
            if (param == ButtonType.FINISH) {
                return property.getValue();
            } else {
                return null;
            }
        });
        mediumDialog.showAndWait().ifPresent(simpleMedium -> {
            repository.mergeMedia(simpleMedium.getMediumId(), this.mediumId).whenComplete((aBoolean, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                }
                if (aBoolean == null || !aBoolean) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Could not merge Media", ButtonType.CLOSE);
                    Platform.runLater(alert::show);
                }
            });
        });
    }

    @FXML
    private void saveMedium() {

    }

    @FXML
    private void addToc() {
        final String link = this.urlField.getText();
        if (!ApplicationConfig.getRepository().addToc(this.mediumId, link)) {
            System.out.println("Failed");
        }
    }
}
