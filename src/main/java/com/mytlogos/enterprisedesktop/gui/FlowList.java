package com.mytlogos.enterprisedesktop.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.FocusModel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;

/**
 *
 */
public class FlowList<T> extends FlowPane {
    private ObservableList<T> items = FXCollections.observableArrayList();
    private ObjectProperty<MultipleSelectionModel<T>> selectionModel = new SimpleObjectProperty<>();
    private ObjectProperty<FocusModel<T>> focusModel = new SimpleObjectProperty<>();
    private ObjectProperty<Callback<FlowList<T>, ListCell<T>>> cellFactory = new SimpleObjectProperty<>();

    public ObservableList<T> getItems() {
        return items;
    }

    public void setItems(ObservableList<T> items) {
        this.items = items;
    }

    public FocusModel getFocusModel() {
        return focusModel.get();
    }

    public ObjectProperty<FocusModel<T>> focusModelProperty() {
        return focusModel;
    }

    public void setFocusModel(FocusModel<T> focusModel) {
        this.focusModel.set(focusModel);
    }

    public MultipleSelectionModel getSelectionModel() {
        return selectionModel.get();
    }

    public void setSelectionModel(MultipleSelectionModel<T> selectionModel) {
        this.selectionModel.set(selectionModel);
    }

    public ObjectProperty<MultipleSelectionModel<T>> selectionModelProperty() {
        return selectionModel;
    }
}
