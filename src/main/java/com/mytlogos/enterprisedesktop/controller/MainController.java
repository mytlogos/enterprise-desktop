package com.mytlogos.enterprisedesktop.controller;

import com.dlsc.preferencesfx.PreferencesFx;
import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.preferences.MainPreferences;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Objects;

/**
 *
 */
public class MainController {
    @FXML
    private Tab mediumInWaitTab;
    @FXML
    private Tab episodeTab;
    @FXML
    private Tab searchTab;
    @FXML
    private Tab listsTab;
    @FXML
    private Tab statisticsTab;
    @FXML
    private Tab externalUserTab;
    @FXML
    private TabPane tabPane;
    @FXML
    private Text infoText;
    private TaskController taskController = ApplicationConfig.getTaskController();
    private EpisodeViewController episodeViewController;
    private ListViewController listViewController;
    private MediaInWaitController mediaInWaitController;
    private SearchMediumController searchController;
    private MainPreferences mainPreferences = ApplicationConfig.getMainPreferences();

    public void initialize() {
        this.taskController.setInfoTextProperty(this.infoText.textProperty());
        this.tabPane.getSelectionModel().selectedItemProperty().addListener((Flowable, oldValue, newValue) -> {
            detachTab(oldValue);
            attachTab(newValue);
        });
        this.attachTab(this.tabPane.getSelectionModel().getSelectedItem());
    }

    private void detachTab(Tab oldValue) {
        System.out.println("old tab: " + oldValue);
        if (oldValue == this.episodeTab) {
            this.detachTab(this.episodeViewController);
        } else if (oldValue == this.listsTab) {
            this.detachTab(this.listViewController);
        } else if (oldValue == this.externalUserTab) {
            this.detachTab((Attachable) null);
        } else if (oldValue == this.mediumInWaitTab) {
            this.detachTab(this.mediaInWaitController);
        } else if (oldValue == this.searchTab) {
            this.detachTab(this.searchController);
        } else if (oldValue == this.statisticsTab) {
            this.detachTab((Attachable) null);
        }
    }

    private void attachTab(Tab newValue) {
        System.out.println("new tab: " + newValue);

        if (newValue == this.episodeTab) {
            this.attachEpisodeTab();
        } else if (newValue == this.listsTab) {
            this.attachListsTab();
        } else if (newValue == this.externalUserTab) {
            this.attachExternalUserTab();
        } else if (newValue == this.statisticsTab) {
            this.attachStatisticsTab();
        } else if (newValue == this.mediumInWaitTab) {
            this.attachMediumInWaitTab();
        } else if (newValue == this.searchTab) {
            this.attachSearchTab();
        }
    }

    private void detachTab(Attachable attachable) {
        if (attachable == null) {
            return;
        }
        attachable.onDetach();
    }

    private void attachEpisodeTab() {
        if (this.episodeViewController == null) {
            this.episodeViewController = ControllerUtils.load("/episodeListView.fxml", node -> this.episodeTab.setContent(node));
        }
        this.episodeViewController.onAttach();
    }

    private void attachListsTab() {
        if (this.listViewController == null) {
            this.listViewController = ControllerUtils.load("/listsView.fxml", node -> this.listsTab.setContent(node));
        }
        this.listViewController.onAttach();
    }

    private void attachExternalUserTab() {

    }

    private void attachStatisticsTab() {

    }

    private void attachSearchTab() {
        if (this.searchController == null) {
            this.searchController = ControllerUtils.load("/searchMedium.fxml", node -> this.searchTab.setContent(node));
        }
        this.searchController.onAttach();
    }

    private void attachMediumInWaitTab() {
        if (this.mediaInWaitController == null) {
            this.mediaInWaitController = ControllerUtils.load("/mediumInWaitListView.fxml", node -> this.mediumInWaitTab.setContent(node));
        }
        this.mediaInWaitController.onAttach();
    }

    public void openSettings() {
        PreferencesFx preferencesFx = this.mainPreferences.getPreferences();
        preferencesFx.show();
    }

    public interface DisplayValue {
        String getDisplayValue();
    }

    public static class DisplayConverter<T extends DisplayValue> extends StringConverter<T> {
        private final T[] values;

        public DisplayConverter(List<T> values) {
            //noinspection unchecked
            this.values = (T[]) values.toArray();
        }

        public DisplayConverter(T[] values) {
            this.values = values;
        }

        @Override
        public String toString(T object) {
            return object.getDisplayValue();
        }

        @Override
        public T fromString(String string) {
            for (T value : this.values) {
                if (Objects.equals(value.getDisplayValue(), string)) {
                    return value;
                }
            }
            return null;
        }
    }
}
