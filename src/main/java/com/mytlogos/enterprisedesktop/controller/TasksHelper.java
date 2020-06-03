package com.mytlogos.enterprisedesktop.controller;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;

import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;

/**
 *
 */
public class TasksHelper {
    private final Text text;
    private final ProgressBar progressBar;
    private final LinkedList<Service<?>> services = new LinkedList<>();
    private final Map<Service<?>, TaskBox> serviceTaskBoxMap = new WeakHashMap<>();
    private final VBox popupRoot;
    private final InvalidationListener progressListener = (observable) -> updateProgress();
    private final ObjectBinding<Point2D> anchorPosition;
    private final StackPane noTasksPane;
    private final InvalidationListener stateListener = (observable) -> updateTasks();

    public TasksHelper(Text text, ProgressBar progressBar) {
        this.text = text;
        this.progressBar = progressBar;
        Popup popup = new Popup();
        popup.setAutoHide(true);

        this.popupRoot = new VBox();
        this.popupRoot.setFillWidth(true);
        this.popupRoot.setPadding(new Insets(5));
        this.popupRoot.setSpacing(5);
        this.popupRoot.setMaxHeight(Double.MAX_VALUE);
        this.popupRoot.setMaxHeight(Double.MAX_VALUE);
        this.popupRoot.setBorder(new Border(new BorderStroke(
                Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM
        )));
        this.noTasksPane = new StackPane();
        this.noTasksPane.setPrefHeight(100);
        this.noTasksPane.setPrefWidth(200);
        final Text tasksAvailable = new Text("No Tasks available");
        this.noTasksPane.getChildren().add(tasksAvailable);
        StackPane.setAlignment(tasksAvailable, Pos.CENTER);

        final ChangeListener<Background> backgroundChangeListener = (o, old, newValue) -> this.popupRoot.setBackground(newValue);
        this.text.parentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof Region) {
                final Region parent = (Region) newValue;
                parent.backgroundProperty().addListener(backgroundChangeListener);
            }
            if (oldValue instanceof Region) {
                final Region parent = (Region) oldValue;
                parent.backgroundProperty().removeListener(backgroundChangeListener);
            }
        });
        this.popupRoot.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));
        popup.setOnShowing(event -> System.out.println("i am showing popup"));
        popup.setOnHiding(event -> System.out.println("i am hiding popup"));
        this.anchorPosition = Bindings.createObjectBinding(() -> {
            final Bounds screenBounds = this.text.localToScreen(this.text.getBoundsInLocal());

            if (screenBounds == null) {
                return new Point2D(0, 0);
            }

            final double anchorX = screenBounds.getMinX();
            double anchorY = screenBounds.getMinY() - this.popupRoot.getHeight() - 5;
            return new Point2D(anchorX, anchorY);
        }, this.text.boundsInLocalProperty(), this.popupRoot.heightProperty());

        this.anchorPosition.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            popup.setAnchorX(newValue.getX());
            popup.setAnchorY(newValue.getY());
        });
        this.progressBar.setOnMouseClicked(event -> {
            final Point2D anchor = this.anchorPosition.get();
            double anchorX = anchor.getX();
            double anchorY = anchor.getY();
            popup.show(this.text, anchorX, anchorY);
        });
        this.text.setOnMouseClicked(event -> {
            final Point2D anchor = this.anchorPosition.get();
            double anchorX = anchor.getX();
            double anchorY = anchor.getY();
            popup.show(this.text, anchorX, anchorY);
        });
        popup.getContent().add(this.popupRoot);
        this.updateProgress();
        this.updateTasks();
    }

    private void updateProgress() {
        double totalProgress = 0;

        for (Service<?> service : this.services) {
            if (service.getState() != Worker.State.RUNNING) {
                totalProgress += 1;
            } else if (service.getProgress() >= 0) {
                totalProgress += service.getProgress();
            }
        }
        if (this.services.isEmpty()) {
            this.progressBar.setProgress(0);
        } else {
            this.progressBar.setProgress(totalProgress / this.services.size());
        }
    }

    private void updateTasks() {
        int running = 0;
        for (Service<?> service : this.services) {
            if (service.getState() == Worker.State.RUNNING) {
                running++;
            }
        }
        final ObservableList<Node> children = this.popupRoot.getChildren();
        if (running == 0) {
            this.text.setText("No Tasks are running");

            if (children.isEmpty()) {
                children.add(this.noTasksPane);
            }
        } else {
            if (running == 1) {
                this.text.setText("1 Task is running");
            } else {
                this.text.setText(String.format("%d Tasks are running", running));
            }
        }
    }

    void addService(Service<?> service) {
        if (!this.services.contains(service)) {
            this.services.add(service);
        }
        if (!this.serviceTaskBoxMap.containsKey(service)) {
            final TaskBox value = new TaskBox();

            service.stateProperty().addListener(this.stateListener);
            service.progressProperty().addListener(this.progressListener);

            service.progressProperty().addListener(observable -> {
                final double progress = service.getProgress();
                if (service.getState() == Worker.State.RUNNING) {
                    value.progressProperty().set(progress);
                } else {
                    value.progressProperty().set(1);
                }
            });
            service.messageProperty().addListener(observable -> {
                final String message = service.getMessage();
                if (service.getState() == Worker.State.RUNNING) {
                    value.messageProperty().set(message);
                }
            });
            service.titleProperty().addListener(observable -> {
                final String title = service.getTitle();
                if (service.getState() == Worker.State.RUNNING) {
                    value.titleProperty().set(title);
                }
            });

            final ObservableList<Node> children = this.popupRoot.getChildren();

            if (children.size() == 1 && children.get(0) == this.noTasksPane) {
                children.remove(0);
            }
            children.add(value);
            this.serviceTaskBoxMap.put(service, value);
        }
        if (service.getState() == Worker.State.READY) {
            service.start();
        } else if (isFinished(service)) {
            service.reset();
            service.start();
        }
        this.updateProgress();
        this.updateTasks();
    }

    boolean isFinished(Service<?> service) {
        return service.getState() == Worker.State.FAILED
                || service.getState() == Worker.State.CANCELLED
                || service.getState() == Worker.State.SUCCEEDED;
    }

    void removeService(Service<?> service) {
        final TaskBox taskBox = this.serviceTaskBoxMap.remove(service);
        taskBox.messageProperty().unbind();
        taskBox.progressProperty().unbind();
        final ObservableList<Node> children = this.popupRoot.getChildren();

        children.remove(taskBox);

        if (children.isEmpty()) {
            children.add(this.noTasksPane);
        }
        this.services.remove(service);
        service.stateProperty().removeListener(this.stateListener);
        service.progressProperty().removeListener(this.progressListener);
        this.updateProgress();
        this.updateTasks();
    }

    private static class TaskBox extends VBox {
        private final Text message;
        private final Text title;
        private final ProgressBar progressBar;

        public TaskBox() {
            this.message = new Text();
            this.title = new Text();
            this.progressBar = new ProgressBar();
            this.progressBar.setPrefHeight(20);
            this.progressBar.setMaxWidth(Double.MAX_VALUE);
            this.getChildren().addAll(this.title, this.progressBar, this.message);
            this.setSpacing(5);
            this.setPadding(new Insets(2));
            this.setBorder(new Border(new BorderStroke(
                    Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THIN
            )));
            VBox.setVgrow(this.message, Priority.ALWAYS);
            VBox.setVgrow(this.title, Priority.ALWAYS);
            this.setWidth(300);
        }

        StringProperty titleProperty() {
            return this.title.textProperty();
        }

        StringProperty messageProperty() {
            return this.message.textProperty();
        }

        DoubleProperty progressProperty() {
            return this.progressBar.progressProperty();
        }
    }
}
