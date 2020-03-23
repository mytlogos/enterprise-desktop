package com.mytlogos.enterprisedesktop.controller;

import com.sun.javafx.webkit.WebConsoleListener;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 */
public class PdfViewController extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        final Parent web = renderWithWebView();
        final Scene scene = new Scene(web, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private WebView renderWithWebView() {
        WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> {
            System.out.println(message + "[at " + lineNumber + "]");
        });
        final WebView web = new WebView();
        WebEngine engine = web.getEngine();
        String url = getClass().getResource("/pdfjs/web/viewer.html").toExternalForm();

        // connect CSS styles to customize pdf.js appearance
//        engine.setUserStyleSheetLocation(getClass().getResource("/web.css").toExternalForm());

        engine.setJavaScriptEnabled(true);
        engine.load(url);
        engine.setOnError(event -> event.getException().printStackTrace());
        engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != Worker.State.SUCCEEDED) {
                return;
            }
            String file = "file://D:\\\\Uni\\\\Chemie\\\\Bücher\\\\Laborpraxis Band 2_Messmethoden.pdf";
            web.getEngine().executeScript("PDFViewerApplication.open({url: \"" + file + "\", originalUrl: \"Laborpraxis Band 2_Messmethoden.pdf\"})");
        });
        return web;
    }

    private Parent renderWithImages() throws IOException {
        final PDDocument load = PDDocument.load(
                new File("D:\\Uni\\Chemie\\Bücher\\Biochemie Stryer.pdf")
        );
        final PDPageTree tree = load.getDocumentCatalog().getPages();
        final PDDocumentOutline outline = load.getDocumentCatalog().getDocumentOutline();
        final ArrayList<PDOutlineItem> list = new ArrayList<>();
        outline.children().forEach(list::add);
        final PDOutlineItem outlineItem = list.get(0);
        final String title = outlineItem.getTitle();
        final PDDestination destination = outlineItem.getDestination();
        final int index = load.getPages().indexOf(outlineItem.findDestinationPage(load));
        final PDFRenderer pdfRenderer = new PDFRenderer(load);
        final BufferedImage image = pdfRenderer.renderImage(0);
        final PDFTextStripper stripper = new PDFTextStripper();
        stripper.setAddMoreFormatting(true);
        stripper.setEndPage(30);
        final String text = stripper.getText(load);
        load.close();
        final WritableImage writableImage = SwingFXUtils.toFXImage(image, null);
        return new Pane(new ImageView(writableImage));
    }
}
