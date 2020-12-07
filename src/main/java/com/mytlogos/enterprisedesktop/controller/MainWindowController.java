package com.mytlogos.enterprisedesktop.controller;

import java.io.IOException;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.RepositoryProvider;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;

public class MainWindowController {
    @FXML
    private ProgressIndicator loading;
    @FXML
    private StackPane root;

    public void initialize() {
        this.showLoading();
        Initializer initializer = new Initializer();
        initializer.setOnSucceeded(ev -> {
            this.hideLoading();

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

    private void listenUserState() {
        ApplicationConfig.getRepository().getUser().observe(user -> {
            if (user == null) {
                this.showLogin();
            } else {
                this.showMain();
            }
        });
    }

    private void showLoading() {
        if (this.loading.getParent() == null) {
            this.root.getChildren().setAll(loading);
        }
    }

    private void hideLoading() {
        this.root.getChildren().remove(this.loading);
    }

    private void showLogin() {
        Parent loginRoot;
        try {
            loginRoot = FXMLLoader.load(getClass().getResource("/login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.root.getChildren().setAll(loginRoot);
    }

    private void showMain() {
        Parent mainRoot;
        try {
            mainRoot = FXMLLoader.load(getClass().getResource("/main.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.root.getChildren().setAll(mainRoot);
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
