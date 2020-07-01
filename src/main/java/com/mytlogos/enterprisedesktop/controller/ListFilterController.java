package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.background.sqlite.life.Observer;
import com.mytlogos.enterprisedesktop.model.MediaList;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class ListFilterController {
    private final FilterController<MediaList> listFilterController;
    private final ObservableList<Integer> observableFilterListItems = FXCollections.observableArrayList();
    private final ObservableList<Integer> observableIgnoreListItems = FXCollections.observableArrayList();
    private final Observer<List<Integer>> filterListItemsObserver = mediaIds -> this.observableFilterListItems.setAll(mediaIds == null ? Collections.emptyList() : mediaIds);
    private final Observer<List<Integer>> ignoreListItemsObserver = mediaIds -> this.observableIgnoreListItems.setAll(mediaIds == null ? Collections.emptyList() : mediaIds);
    private LiveData<List<Integer>> filterListItemsLiveData;
    private LiveData<List<Integer>> ignoredListItemsLiveData;


    public ListFilterController(TextField field, BooleanProperty ignoredProperty, ObservableList<Node> children, LiveData<List<MediaList>> listLiveData) {
        this.listFilterController = new FilterController<>(field, ignoredProperty, listLiveData, MediaList::getName, children);
        this.listFilterController.addFilteredListener(observable -> {
            List<Integer> listIds = new ArrayList<>();

            for (MediaList list : this.listFilterController.getFiltered()) {
                listIds.add(list.getListId());
            }

            if (this.filterListItemsLiveData != null) {
                this.filterListItemsLiveData.removeObserver(this.filterListItemsObserver);
            }
            this.filterListItemsLiveData = ApplicationConfig.getRepository().getListItems(listIds);
            this.filterListItemsLiveData.observe(this.filterListItemsObserver);
        });
        this.listFilterController.addIgnoredListener(observable -> {
            List<Integer> listIds = new ArrayList<>();

            for (MediaList list : this.listFilterController.getIgnored()) {
                listIds.add(list.getListId());
            }

            if (this.ignoredListItemsLiveData != null) {
                this.ignoredListItemsLiveData.removeObserver(this.ignoreListItemsObserver);
            }
            this.ignoredListItemsLiveData = ApplicationConfig.getRepository().getListItems(listIds);
            this.ignoredListItemsLiveData.observe(this.ignoreListItemsObserver);
        });
    }

    Observable[] observables() {
        return new Observable[]{
                this.observableFilterListItems,
                this.observableIgnoreListItems
        };
    }

    boolean isIgnored(int mediumId) {
        return !this.observableIgnoreListItems.isEmpty() && this.observableIgnoreListItems.contains(mediumId);
    }

    boolean isFiltered(int mediumId) {
        return this.observableFilterListItems.isEmpty() || this.observableFilterListItems.contains(mediumId);
    }
}
