package com.mytlogos.enterprisedesktop;

import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.RepositoryProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class Starter extends Application {

    public static void main(String[] args) {
        initializeAsync();
        launch(args);
    }

    private static void initializeAsync() {
//        ExecutorService service = Executors.newSingleThreadExecutor();
        final Repository repository = new RepositoryProvider().provide();
        ApplicationConfig.initialize(repository);
//        ApplicationConfig.setInitializeFuture(service.submit(() -> {
//        }));
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("Enterprise Desktop");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.getIcons().add(new Image("/ext_icon_active_32.png"));
        primaryStage.getIcons().add(new Image("/ext_icon_active_48.png"));
        primaryStage.show();
    }
}
