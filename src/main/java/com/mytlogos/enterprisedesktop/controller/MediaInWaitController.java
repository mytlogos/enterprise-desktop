package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.model.MediumInWait;
import io.reactivex.disposables.Disposable;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

/**
 *
 */
public class MediaInWaitController implements Attachable {
    private Disposable mediumInWaitDisposable;
    @FXML
    private ListView<MediumInWait> mediumInWaitListView;
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
    private TextField nameFilter;
    @FXML
    private TextField hostFilter;

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }
}
