package com.mytlogos.enterprisedesktop.controller;

import java.io.IOException;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.RepositoryProvider;

import javafx.animation.FadeTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MainWindowController {
    @FXML
    private ProgressIndicator loading;
    @FXML
    private StackPane root;

    public void initialize() {
        Initializer initializer = new Initializer();
        initializer.setOnSucceeded(ev -> {
            Boolean loggedIn = initializer.getValue();

            if (loggedIn != null && loggedIn) {
                this.showMain();
            } else {
                this.showLogin();
            }
            this.listenUserState();
        });
        initializer.setOnFailed(ev -> {
            System.out.println("Failed Initializer");
            initializer.getException().printStackTrace();
        });
        initializer.start();
    }

    private void transition(Node from, Node to) {
        FadeTransition fadeOut = new FadeTransition();
        fadeOut.setDuration(Duration.seconds(2));
        fadeOut.setNode(from);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(ev -> this.root.getChildren().remove(from));
        
        FadeTransition fadeIn = new FadeTransition();
        fadeIn.setDuration(Duration.seconds(2));
        fadeIn.setNode(to);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        fadeOut.play();
        fadeIn.play();
    }

    private void listenUserState() {
        ApplicationConfig.getRepository().getUser().observe(user -> {
            if (user == null) {
                this.showLogin();
            } else {
                this.showMain();
            }
        });
    }

    private void showLogin() {
        Parent loginRoot;
        try {
            loginRoot = FXMLLoader.load(getClass().getResource("/login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Node current = this.root.getChildren().get(this.root.getChildren().size() - 1);
        this.root.getChildren().add(loginRoot);
        this.transition(current, loginRoot);
    }

    private void showMain() {
        Parent mainRoot;
        try {
            mainRoot = FXMLLoader.load(getClass().getResource("/main.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Node current = this.root.getChildren().get(this.root.getChildren().size() - 1);
        this.root.getChildren().add(mainRoot);
        this.transition(current, mainRoot);
    }

    private class Initializer extends Service<Boolean> {

        @Override
        protected Task<Boolean> createTask() {
            return new Task<>() {

                @Override
                protected Boolean call() throws Exception {
                    // initialize the repository
                    final Repository repository = new RepositoryProvider().provide();
                    // make it available globally
                    ApplicationConfig.initialize(repository);
                    // check if user is currently logged in by default
                    return repository.getUser().firstElement().get() != null;
                }
            };
        }
    }
}
