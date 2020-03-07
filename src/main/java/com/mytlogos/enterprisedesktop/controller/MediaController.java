package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.model.Medium;
import com.mytlogos.enterprisedesktop.model.TocEpisode;
import com.mytlogos.enterprisedesktop.tools.Sorting;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 *
 */
public class MediaController implements Attachable {
    @FXML
    private RadioButton readOnly;
    @FXML
    private ToggleGroup readFilter;
    @FXML
    private RadioButton notReadOnly;
    @FXML
    private RadioButton ignoreRead;
    @FXML
    private RadioButton savedOnly;
    @FXML
    private ToggleGroup savedFilter;
    @FXML
    private RadioButton notSavedOnly;
    @FXML
    private RadioButton ignoreSaved;
    @FXML
    private ScrollPane detailPane;
    @FXML
    private ListView<TocEpisode> mediumContentView;
    @FXML
    private TextField scrollToEpisodeField;
    @FXML
    private ToggleButton episodeAscendingBtn;
    @FXML
    private ChoiceBox<Sorting> episodeSorting;
    @FXML
    private ListView<Medium> mediaView;
    @FXML
    private ToggleButton mediumAscendingBtn;
    @FXML
    private ChoiceBox<Sorting> mediumSorting;
    @FXML
    private CheckBox showAudioBox;
    @FXML
    private CheckBox showVideoBox;
    @FXML
    private CheckBox showImageBox;
    @FXML
    private CheckBox showTextBox;
    @FXML
    private Spinner<Integer> maxEpisodeIndex;
    @FXML
    private Spinner<Integer> minEpisodeIndex;
    @FXML
    private TextField nameFilter;
    @FXML
    private MediumDisplayController mediumDisplayController;

    public void initialize() {

    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }

    @FXML
    private void scrollToEpisode() {

    }
}
