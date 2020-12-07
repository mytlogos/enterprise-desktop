package com.mytlogos.enterprisedesktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 */
public class Starter extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/global.fxml"));
        primaryStage.setTitle("Enterprise Desktop");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.getIcons().add(new Image("/ext_icon_active_32.png"));
        primaryStage.getIcons().add(new Image("/ext_icon_active_48.png"));
        primaryStage.show();
    }
}
