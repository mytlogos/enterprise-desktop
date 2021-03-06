package com.mytlogos.enterprisedesktop.controller;

import com.dlsc.preferencesfx.PreferencesFx;
import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.preferences.MainPreferences;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 *
 */
public class MainController {
    private final TaskController taskController = ApplicationConfig.getTaskController();
    private final MainPreferences mainPreferences = ApplicationConfig.getMainPreferences();
    private final Map<Tab, TabController> tabTabControllerMap = new WeakHashMap<>();
    TasksHelper helper;
    @FXML
    private ProgressBar taskProgress;
    @FXML
    private Tab analyzeTab;
    @FXML
    private Tab mediaTab;
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

    public void initialize() {
        this.initTabMaps();
        this.helper = new TasksHelper(this.infoText, this.taskProgress);
        this.taskController.setTasksHelper(this.helper);
        this.tabPane.getSelectionModel().selectedItemProperty().addListener((LiveData, oldValue, newValue) -> {
            detachTab(oldValue);
            attachTab(newValue);
        });
        this.attachTab(this.tabPane.getSelectionModel().getSelectedItem());
    }

    private void initTabMaps() {
        this.tabTabControllerMap.put(this.episodeTab, new TabController("/episodeListView.fxml"));
        this.tabTabControllerMap.put(this.listsTab, new TabController("/listsView.fxml"));
        this.tabTabControllerMap.put(this.mediumInWaitTab, new TabController("/mediumInWaitListView.fxml"));
        this.tabTabControllerMap.put(this.searchTab, new TabController("/searchMedium.fxml"));
        this.tabTabControllerMap.put(this.mediaTab, new TabController("/media.fxml"));
        this.tabTabControllerMap.put(this.analyzeTab, new TabController("/analyze.fxml"));
    }

    private void detachTab(Tab oldValue) {
        System.out.println("old tab: " + oldValue);
        final TabController tabController = this.tabTabControllerMap.get(oldValue);

        if (tabController != null) {
            tabController.getAttachable(oldValue).onDetach();
        }
    }

    private void attachTab(Tab newValue) {
        System.out.println("new tab: " + newValue);

        final TabController tabController = this.tabTabControllerMap.get(newValue);

        if (tabController != null) {
            tabController.getAttachable(newValue).onAttach();
        }
    }

    public void openSettings() {
        PreferencesFx preferencesFx = this.mainPreferences.getPreferences();
        preferencesFx.show();
    }

    @FXML
    private void startSynchronize() {
        this.taskController.startSynchronizeTask();
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

    private class TabController {
        private final String fxml;
        private Attachable attachable;

        private TabController(String fxml) {
            this.fxml = fxml;
        }

        private Attachable getAttachable(Tab tab) {
            if (this.attachable == null) {
                this.attachable = ControllerUtils.load(this.fxml, tab::setContent);
                this.attachable.setParentController(MainController.this);
            }
            return this.attachable;
        }
    }
}
