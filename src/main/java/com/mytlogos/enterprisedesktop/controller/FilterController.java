package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.background.sqlite.life.Observer;
import com.mytlogos.enterprisedesktop.model.MediaList;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

/**
 *
 */
public class FilterController<T> {
    private final ObservableList<FilterItem<T>> listFilterItems = FXCollections.observableArrayList();
    private final ObservableSet<T> filterListIds = FXCollections.observableSet(new HashSet<>());
    private final ObservableSet<T> ignoredListIds = FXCollections.observableSet(new HashSet<>());
    private final Function<T, String> converter;
    private final ObservableList<Node> children;
    private final Observer<List<T>> listObserver = this::updateFilteredListView;


    public FilterController(TextField field, BooleanProperty ignoredProperty, LiveData<List<T>> liveData, Function<T, String> converter, ObservableList<Node> children) {
        this.converter = converter;
        this.children = children;
        liveData.observe(this.listObserver);
        ControllerUtils.addAutoCompletionBinding(
                field,
                liveData,
                converter,
                mediaList -> {
                    if (ignoredProperty.get()) {
                        this.ignoredListIds.add(mediaList);
                    } else {
                        this.filterListIds.add(mediaList);
                    }
                }
        );
        this.filterListIds.addListener((InvalidationListener) observable -> updateFilteredListView(liveData.getValue()));
        this.ignoredListIds.addListener((InvalidationListener) observable -> updateFilteredListView(liveData.getValue()));
        this.listFilterItems.addListener((ListChangeListener<? super FilterItem<T>>) c -> {
            if (c.next()) {
                if (c.wasAdded()) {
                    for (FilterItem<T> item : c.getAddedSubList()) {
                        item.setOnClose(() -> {
                            if (item.isIgnored()) {
                                this.ignoredListIds.remove(item.getValue());
                            } else {
                                this.filterListIds.remove(item.getValue());
                            }
                        });
                        this.children.add(item.getRoot());
                    }
                }
                if (c.wasRemoved()) {
                    for (FilterItem<T> item : c.getRemoved()) {
                        this.children.remove(item.getRoot());
                    }
                }
            }
        });
    }

    Collection<T> getFiltered() {
        return this.filterListIds;
    }

    Collection<T> getIgnored() {
        return this.ignoredListIds;
    }

    void addFilteredListener(InvalidationListener listener) {
        this.filterListIds.addListener(listener);
    }

    void addIgnoredListener(InvalidationListener listener) {
        this.ignoredListIds.addListener(listener);
    }

    private void updateFilteredListView(List<T> mediaLists) {
        if (mediaLists == null) {
            return;
        }

        List<T> filterLists = new ArrayList<>(this.filterListIds.size());
        List<T> ignoreLists = new ArrayList<>(this.ignoredListIds.size());

        for (T mediaList : mediaLists) {
            if (this.filterListIds.contains(mediaList)) {
                filterLists.add(mediaList);
            }
            if (this.ignoredListIds.contains(mediaList)) {
                ignoreLists.add(mediaList);
            }
        }

        final List<FilterItem<T>> removeItems = new ArrayList<>();

        for (FilterItem<T> item : this.listFilterItems) {
            final T value = item.getValue();

            if (item.isIgnored()) {
                if (!ignoreLists.remove(value)) {
                    removeItems.add(item);
                }
            } else if (!filterLists.remove(value)) {
                removeItems.add(item);
            }
        }
        final List<FilterItem<T>> newItems = new ArrayList<>();

        for (T mediaList : filterLists) {
            FilterItem<T> mediaListFilterItem = new FilterItem<>(this.converter.apply(mediaList), mediaList);
            mediaListFilterItem.setIgnored(false);
            newItems.add(mediaListFilterItem);
        }

        for (T mediaList : ignoreLists) {
            FilterItem<T> mediaListFilterItem = new FilterItem<>(this.converter.apply(mediaList), mediaList);
            mediaListFilterItem.setIgnored(true);
            newItems.add(mediaListFilterItem);
        }
        this.listFilterItems.removeAll(removeItems);
        this.listFilterItems.addAll(newItems);
    }
}
