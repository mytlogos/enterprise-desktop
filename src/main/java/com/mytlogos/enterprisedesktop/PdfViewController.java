package com.mytlogos.enterprisedesktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 */
public class PdfViewController extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        final WebView web = new WebView();
        WebEngine engine = web.getEngine();
        String url = getClass().getResource("/web/viewer.html").toExternalForm();

        // connect CSS styles to customize pdf.js appearance
        engine.setUserStyleSheetLocation(getClass().getResource("/web.css").toExternalForm());

        engine.setJavaScriptEnabled(true);
        engine.load(url);

        final Scene scene = new Scene(web, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
