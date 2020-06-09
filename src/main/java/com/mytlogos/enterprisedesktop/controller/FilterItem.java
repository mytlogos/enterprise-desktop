package com.mytlogos.enterprisedesktop.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;

import java.util.Objects;

/**
 *
 */
public class FilterItem<T> {
    private final Node root;
    private final T value;
    @FXML
    private Text text;
    private Runnable runnable;
    private boolean ignored;

    public FilterItem(String text, T value) {
        this.value = value;
        this.root = ControllerUtils.load("/filterItem.fxml", this);
        this.text.setText(text);
    }

    public T getValue() {
        return value;
    }

    public Node getRoot() {
        return root;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;

        if (ignored) {
            this.root.getStyleClass().add("ignore");
        } else {
            this.root.getStyleClass().remove("ignore");
        }
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterItem)) return false;

        FilterItem<?> that = (FilterItem<?>) o;

        return Objects.equals(value, that.value);
    }

    void setOnClose(Runnable runnable) {
        this.runnable = runnable;
    }

    @FXML
    public void close() {
        if (this.runnable != null) {
            this.runnable.run();
        }
    }
}
