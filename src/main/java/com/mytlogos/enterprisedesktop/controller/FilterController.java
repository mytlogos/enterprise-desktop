package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
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
    private final ObservableList<FilterItem<T>> filterItems = FXCollections.observableArrayList();
    private final ObservableSet<T> filtered = FXCollections.observableSet(new HashSet<>());
    private final ObservableSet<T> ignored = FXCollections.observableSet(new HashSet<>());
    private final Function<T, String> converter;
    private final ObservableList<Node> children;

    public FilterController(TextField field, BooleanProperty ignoredProperty, LiveData<List<T>> liveData, Function<T, String> converter, ObservableList<Node> children) {
        this.converter = converter;
        this.children = children;
        liveData.observe(this::updateFiltered);
        ControllerUtils.addAutoCompletionBinding(
                field,
                liveData,
                converter,
                mediaList -> {
                    if (ignoredProperty.get()) {
                        this.ignored.add(mediaList);
                    } else {
                        this.filtered.add(mediaList);
                    }
                }
        );
        this.filtered.addListener((InvalidationListener)observable -> updateFiltered(liveData.getValue()));
        this.ignored.addListener((InvalidationListener) observable -> updateFiltered(liveData.getValue()));
        this.filterItems.addListener((ListChangeListener<? super FilterItem<T>>) c -> {
            if (c.next()) {
                if (c.wasAdded()) {
                    for (FilterItem<T> item : c.getAddedSubList()) {
                        item.setOnClose(() -> {
                            if (item.isIgnored()) {
                                this.ignored.remove(item.getValue());
                            } else {
                                this.filtered.remove(item.getValue());
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

    private void updateFiltered(List<T> list) {
        if (list == null) {
            return;
        }

        List<T> filter = new ArrayList<>(this.filtered.size());
        List<T> ignore = new ArrayList<>(this.ignored.size());

        for (T value : list) {
            if (this.filtered.contains(value)) {
                filter.add(value);
            }
            if (this.ignored.contains(value)) {
                ignore.add(value);
            }
        }

        final List<FilterItem<T>> removeItems = new ArrayList<>();

        for (FilterItem<T> item : this.filterItems) {
            final T value = item.getValue();

            if (item.isIgnored()) {
                if (!ignore.remove(value)) {
                    removeItems.add(item);
                }
            } else if (!filter.remove(value)) {
                removeItems.add(item);
            }
        }
        final List<FilterItem<T>> newItems = new ArrayList<>();

        for (T value : filter) {
            FilterItem<T> mediaListFilterItem = new FilterItem<>(this.converter.apply(value), value);
            mediaListFilterItem.setIgnored(false);
            newItems.add(mediaListFilterItem);
        }

        for (T value : ignore) {
            FilterItem<T> mediaListFilterItem = new FilterItem<>(this.converter.apply(value), value);
            mediaListFilterItem.setIgnored(true);
            newItems.add(mediaListFilterItem);
        }
        this.filterItems.removeAll(removeItems);
        this.filterItems.addAll(newItems);
    }

    Collection<T> getFiltered() {
        return this.filtered;
    }

    Collection<T> getIgnored() {
        return this.ignored;
    }

    void addFilteredListener(InvalidationListener listener) {
        this.filtered.addListener(listener);
    }

    void addIgnoredListener(InvalidationListener listener) {
        this.ignored.addListener(listener);
    }
}
