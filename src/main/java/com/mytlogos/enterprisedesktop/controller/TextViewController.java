package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.model.SimpleEpisode;
import com.mytlogos.enterprisedesktop.model.SimpleMedium;
import com.mytlogos.enterprisedesktop.tools.FileTools;
import com.mytlogos.enterprisedesktop.tools.TextContentTool;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.util.StringConverter;
import netscape.javascript.JSObject;
import org.controlsfx.control.Notifications;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 */
public class TextViewController {

    @FXML
    private WebView browser;
    @FXML
    private Spinner<Integer> zoom;
    @FXML
    private ToggleButton fitToPageBtn;
    @FXML
    private Label title;
    @FXML
    private ChoiceBox<SimpleEpisode> episodeBox;
    @FXML
    private Button previousBtn;
    @FXML
    private Button nextBtn;
    private List<SimpleEpisode> simpleEpisodes;
    private Map<Integer, String> episodePagePaths;
    private final EventHandler<KeyEvent> keyEventEventHandler = event -> {
        if (event.isControlDown()) {
            if (event.getCode() == KeyCode.MINUS) {
                this.zoom.decrement();
            } else if (event.getCode() == KeyCode.PLUS) {
                this.zoom.increment();
            } else if (event.getCode() == KeyCode.F) {
                this.fitToPageBtn.setSelected(!this.fitToPageBtn.isSelected());
            } else if (event.getCode() == KeyCode.LEFT) {
                this.episodeBox.getSelectionModel().selectPrevious();
            } else if (event.getCode() == KeyCode.RIGHT) {
                this.episodeBox.getSelectionModel().selectNext();
            }
        } else if (event.getCode().isArrowKey()) {
            this.updateProgress();
            // TODO 08.3.2020: scroll down, up, right, left a fixed amount (especially horizontal) scripts?
        } else if (event.getCode().isNavigationKey()) {
            // TODO 08.3.2020: work on navigation with scripts?
//            if (event.getCode() == KeyCode.PAGE_DOWN) {
//                final int lastIndex = this.list.getItems().size() - 1;
//                if (latestPage < lastIndex) {
//                    this.list.scrollTo(latestPage + 1);
//                }
//            } else if (event.getCode() == KeyCode.UP) {
//                if (latestPage > 0) {
//                    this.list.scrollTo(latestPage - 1);
//                }
//            } else if (event.getCode() == KeyCode.HOME) {
//                this.list.scrollTo(0);
//            } else if (event.getCode() == KeyCode.END) {
//                this.list.scrollTo(this.list.getItems().size() - 1);
//            }
        }
    };

    public void open(int mediumId, int currentEpisode) {
        final Repository repository = ApplicationConfig.getRepository();

        final TextContentTool contentTool = FileTools.getTextContentTool();
        final SimpleMedium simpleMedium = repository.getSimpleMedium(mediumId);
        final List<Integer> episodes = repository.getSavedEpisodes(mediumId);
        this.title.setText(simpleMedium.getTitle());
        final String itemPath = contentTool.getItemPath(mediumId);

        if (itemPath == null || itemPath.isEmpty()) {
            Notifications.create().title("No Medium Directory available").show();
            return;
        }
        this.episodePagePaths = contentTool.getEpisodePaths(itemPath);
        episodes.retainAll(this.episodePagePaths.keySet());
        this.simpleEpisodes = repository.getSimpleEpisodes(episodes);

        this.episodeBox.getItems().setAll(this.simpleEpisodes);

        for (SimpleEpisode episode : this.simpleEpisodes) {
            if (episode.getEpisodeId() == currentEpisode) {
                this.episodeBox.getSelectionModel().select(episode);
                break;
            }
        }
    }

    public void initialize() {
        final Repository repository = ApplicationConfig.getRepository();
        final JSObject window = (JSObject) this.browser.getEngine().executeScript("window");
        this.browser.getEngine().setOnError(System.out::println);
        this.browser.getEngine().documentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            final NodeList body = newValue.getElementsByTagName("body");
            for (int i = 0; i < body.getLength(); i++) {
                final Element item = (Element) body.item(i);
                item.setAttribute("style", "padding: 5px");
            }
        });

        this.browser.setOnScroll(event -> updateProgress());
        this.browser.setOnScrollStarted(event -> System.out.println("i started scrolling"));
        this.browser.setOnScrollFinished(event -> System.out.println("i finished scrolling"));
        window.setMember("progress", (Consumer<Double>) aDouble -> {
            System.out.println("progress: " + aDouble);
            final SimpleEpisode episode = this.episodeBox.getSelectionModel().getSelectedItem();
            if (episode == null || this.episodePagePaths == null || this.episodePagePaths.isEmpty() || this.browser.getEngine().getLocation().isEmpty()) {
                return;
            }
            float progress = aDouble.floatValue();
            if (episode.getProgress() < progress) {
                repository.updateProgress(episode.getEpisodeId(), progress);
            }
        });
//        ((JSObject) this.browser.getEngine().executeScript("window")).call("addEventListener", "scroll", (Runnable) () -> System.out.println("i am scrolling"));
//        this.browser.getEngine().executeScript(
//                "window.addEventListener(\"scroll\", () => { " +
//                        "console.log(\"I am scrolling\");"+
//                        "var progress =(window.innerHeight + window.pageYOffset) / document.body.scrollHeight;"+
//                        "progress.accept(progress);" +
//                        "});"
//        );
        this.episodeBox.setConverter(new StringConverter<SimpleEpisode>() {
            @Override
            public String toString(SimpleEpisode object) {
                return object == null ? "" : object.getFormattedTitle();
            }

            @Override
            public SimpleEpisode fromString(String string) {
                if (simpleEpisodes == null) {
                    return null;
                }
                for (SimpleEpisode episode : simpleEpisodes) {
                    if (episode.getFormattedTitle().equals(string)) {
                        return episode;
                    }
                }
                return null;
            }
        });
        this.episodeBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || this.episodePagePaths == null) {
                this.previousBtn.setDisable(true);
                this.nextBtn.setDisable(true);
                return;
            }
            final int index = this.episodeBox.getSelectionModel().getSelectedIndex();
            this.previousBtn.setDisable(index != 0);
            this.nextBtn.setDisable(index < this.episodeBox.getItems().size() - 1);
            String episode = this.episodePagePaths.get(newValue.getEpisodeId());

            if (!episode.startsWith("file:")) {
                episode = "file:" + episode;
            }
            this.browser.getEngine().load(episode);
        });
        this.zoom.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200, 100, 10));
        this.browser.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.setOnKeyPressed(this.keyEventEventHandler);
            }
            if (oldValue != null) {
                oldValue.removeEventHandler(KeyEvent.KEY_PRESSED, this.keyEventEventHandler);
            }
        });
        this.zoom.getValueFactory().setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return object == null ? "0%" : object + "%";
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string.replace("%", ""));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return 100;
                }
            }
        });
        this.zoom.valueProperty().addListener(observable -> zoomBrowser());
        this.fitToPageBtn.selectedProperty().addListener(observable -> zoomBrowser());
        this.previousBtn.setOnAction(event -> this.episodeBox.getSelectionModel().selectPrevious());
        this.nextBtn.setOnAction(event -> this.episodeBox.getSelectionModel().selectNext());

    }

    private void updateProgress() {
        final Repository repository = ApplicationConfig.getRepository();
        final SimpleEpisode episode = this.episodeBox.getSelectionModel().getSelectedItem();
        if (episode == null || this.episodePagePaths == null || this.episodePagePaths.isEmpty() || this.browser.getEngine().getLocation().isEmpty()) {
            return;
        }
        final Object result = this.browser.getEngine().executeScript("(window.innerHeight + window.pageYOffset) / document.body.scrollHeight");

        if (!(result instanceof Double)) {
            return;
        }
        float progress = Math.min(((Double) result).floatValue() + 0.01f, 1);
        if (episode.getProgress() < progress) {
            System.out.println("progress: " + progress);
            repository.updateProgress(episode.getEpisodeId(), Math.min(progress, 1));
        }
    }

    private void zoomBrowser() {
        if (this.browser == null) {
            return;
        }
        final Integer zoomValue = zoom.getValue();
        this.browser.setZoom(zoomValue);
    }
}
