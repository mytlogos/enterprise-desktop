package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.TaskManager;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.Utils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.controlsfx.control.Notifications;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class AnalyzeController implements Attachable {

    private final TaskService analyzeService = new TaskService();
    @FXML
    private TreeView<AnalyzeResult> problemView;
    @FXML
    private Pane displayPane;
    private MainController controller;

    public void initialize() {
        this.analyzeService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            final TreeItem<AnalyzeResult> root = new TreeItem<>(newValue);
            this.toTree(newValue, root);
            this.problemView.setRoot(root);
        });
        // noinspection ThrowableNotThrown
        this.analyzeService.setOnFailed(event -> event.getSource().getException().printStackTrace());
        this.problemView.setCellFactory(param -> new TreeCell<AnalyzeResult>() {
            private Node labeledText;

            @Override
            protected void updateItem(AnalyzeResult item, boolean empty) {
                super.updateItem(item, empty);
                if (this.labeledText == null) {
                    final Node node = this.lookup(".text");

                    if (node != null) {
                        node.styleProperty()
                                .bind(Bindings
                                        .when(this.itemProperty().isNotNull()
                                                .and(Bindings.selectBoolean(this.itemProperty(), "resolved")))
                                        .then("-fx-strikethrough: true").otherwise(""));
                        this.labeledText = node;
                    }
                }
                if (item == null || empty) {
                    this.setText(null);
                } else {
                    this.setText(item.getTitle());
                }
            }
        });
        this.problemView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                this.displayPane.getChildren().clear();
            } else {
                final Node display = newValue.getValue().getDisplay();
                if (display == null) {
                    this.displayPane.getChildren().clear();
                } else {
                    this.displayPane.getChildren().setAll(display);
                }
            }
        });
    }

    public void analyze() {
        this.controller.helper.addService(this.analyzeService);
    }

    @Override
    public void onAttach() {
        // nothing to do here
    }

    @Override
    public void onDetach() {
        // nothing to do here
    }

    @Override
    public void setParentController(MainController controller) {
        this.controller = controller;
    }

    private void toTree(AnalyzeResult parentResult, TreeItem<AnalyzeResult> parent) {
        for (AnalyzeResult result : parentResult.getSubResult()) {
            TreeItem<AnalyzeResult> item = new TreeItem<>(result);

            parent.getChildren().add(item);
            this.toTree(result, item);
        }
    }

    private static abstract class AnalyzeWork {
        abstract void work(AnalyzerTask resultTask) throws Exception;
    }

    public static abstract class AnalyzeResult {
        private final BooleanProperty resolved = new SimpleBooleanProperty();
        private Node display;

        public boolean isResolved() {
            return resolved.get();
        }

        public ReadOnlyBooleanProperty resolvedProperty() {
            return resolved;
        }

        int getIssueNumber() {
            return this.getSubResult().size();
        }

        Collection<? extends AnalyzeResult> getSubResult() {
            return Collections.emptyList();
        }

        void resolved() {
            TaskManager.runFxTask(() -> this.resolved.set(true));
        }

        void unresolved() {
            TaskManager.runFxTask(() -> this.resolved.set(false));
        }

        Node getDisplay() {
            if (this.display == null) {
                this.display = this.createDisplay();

                if (this.display != null) {
                    this.display.disableProperty().bind(this.resolved);
                }
            }
            return this.display;
        }

        abstract Node createDisplay();

        abstract String getTitle();
    }

    private static abstract class AnalyzeGroupResult extends AnalyzeResult {
        private final List<? extends AnalyzeResult> results;
        private final IntegerProperty resolvedCount = new SimpleIntegerProperty();

        protected AnalyzeGroupResult(List<? extends AnalyzeResult> results) {
            this.results = results;
            for (AnalyzeResult result : this.results) {
                result.resolved.addListener((observable, oldValue, newValue) -> {
                    final int value;
                    if (newValue) {
                        value = this.resolvedCount.get() + 1;
                    } else {
                        value = this.resolvedCount.get() - 1;
                    }
                    this.resolvedCount.set(value);

                    if (value == this.results.size()) {
                        this.resolved();
                    } else {
                        this.unresolved();
                    }
                });
            }
        }

        @Override
        Collection<? extends AnalyzeResult> getSubResult() {
            return this.results;
        }

        @Override
        Node createDisplay() {
            return null;
        }
    }

    private static class SimilarMediaTask extends AnalyzeWork {
        @Override
        void work(AnalyzerTask resultTask) throws Exception {
            final List<SimpleMedium> media = ApplicationConfig.getRepository().getSimpleMedium().firstNonNullElement()
                    .get();
            Set<SimilarMediaResult> results = new LinkedHashSet<>();

            for (SimpleMedium medium : media) {
                for (SimpleMedium simpleMedium : media) {
                    if (simpleMedium != medium && medium.getMedium() == simpleMedium.getMedium()
                            && similarity(medium.getTitle(), simpleMedium.getTitle()) > 0.5) {
                        results.add(new SimilarMediaResult(medium, simpleMedium));
                    }
                }
            }
            List<SimilarMediaResult> list = new ArrayList<>(results);
            list.sort(Comparator.comparing(SimilarMediaResult::getTitle));
            resultTask.update(new TotalSimilarMediaResult(list));
        }

        /**
         * Copied from https://stackoverflow.com/a/16018452/9492864.
         * <p>
         * Calculates the similarity (a number within 0 and 1) between two strings.
         */
        public static double similarity(String s1, String s2) {
            String longer = s1;
            String shorter = s2;
            if (s1.length() < s2.length()) { // longer should always have greater length
                longer = s2;
                shorter = s1;
            }
            int longerLength = longer.length();
            if (longerLength == 0) {
                return 1.0; /* both strings are zero length */
            }
            return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
        }

        /**
         * Copied from https://stackoverflow.com/a/16018452/9492864.
         */
        // Example implementation of the Levenshtein Edit Distance
        // See http://rosettacode.org/wiki/Levenshtein_distance#Java
        public static int editDistance(String s1, String s2) {
            s1 = s1.toLowerCase();
            s2 = s2.toLowerCase();

            int[] costs = new int[s2.length() + 1];
            for (int i = 0; i <= s1.length(); i++) {
                int lastValue = i;
                for (int j = 0; j <= s2.length(); j++) {
                    if (i == 0)
                        costs[j] = j;
                    else {
                        if (j > 0) {
                            int newValue = costs[j - 1];
                            if (s1.charAt(i - 1) != s2.charAt(j - 1))
                                newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                            costs[j - 1] = lastValue;
                            lastValue = newValue;
                        }
                    }
                }
                if (i > 0)
                    costs[s2.length()] = lastValue;
            }
            return costs[s2.length()];
        }

        private static class TotalSimilarMediaResult extends AnalyzeGroupResult {

            private TotalSimilarMediaResult(List<SimilarMediaResult> results) {
                super(results);
            }

            @Override
            Node createDisplay() {
                return null;
            }

            @Override
            String getTitle() {
                return "Total possible Similar Media: " + this.getIssueNumber();
            }
        }

        private static class SimilarMediaResult extends AnalyzeResult {
            public final SimpleMedium first;
            public final SimpleMedium second;

            private SimilarMediaResult(SimpleMedium first, SimpleMedium second) {
                this.first = first;
                this.second = second;
            }

            @Override
            public int hashCode() {
                int result = first != null ? first.hashCode() : 0;
                result = 31 * result + (second != null ? second.hashCode() : 0);
                return result;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o)
                    return true;
                if (!(o instanceof SimilarMediaResult))
                    return false;

                SimilarMediaResult that = (SimilarMediaResult) o;

                if (!Objects.equals(first, that.first))
                    return false;
                return Objects.equals(second, that.second);
            }

            @Override
            public String toString() {
                return "SimilarMediaResult{" + "first=" + first + ", second=" + second + '}';
            }

            @Override
            int getIssueNumber() {
                return 1;
            }

            @Override
            Node createDisplay() {
                return this.createRoot();
            }

            private Pane createRoot() {
                VBox root = new VBox();
                root.setSpacing(5);
                root.setPadding(new Insets(5));
                Button resolve = new Button("Merge Media");
                resolve.setOnAction(event -> {
                    Alert alert = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        String.format("Are you sure you want to merge '%s' into '%s'", second.getTitle(), first.getTitle()),
                        ButtonType.NO,
                        ButtonType.YES
                    );

                    alert.showAndWait().ifPresent(buttonType -> {
                        if (buttonType == ButtonType.YES) {
                            ApplicationConfig.getRepository()
                                    .mergeMedia(this.second.getMediumId(), this.first.getMediumId())
                                    .whenComplete((aBoolean, throwable) -> {
                                        if (throwable != null) {
                                            throwable.printStackTrace();
                                            aBoolean = Boolean.FALSE;
                                        }
                                        boolean finalABoolean = aBoolean;
                                        Platform.runLater(() -> Notifications.create()
                                                .title(String.format(
                                                    "Merging Media '%s' into '%s' %s",
                                                    this.second.getTitle(),
                                                    this.first.getTitle(),
                                                    finalABoolean ? "succeeded" : "failed"
                                                ))
                                                .show());
                                    });
                        }
                    });
                });
                root.getChildren().add(resolve);
                final MediumTypes controller = ControllerUtils.load("/mediumTypes.fxml", root);
                controller.setEditable(false);
                controller.setMedium(this.first.getMedium());

                Button showFirst = new Button("Show First");
                showFirst.setOnAction(event -> ControllerUtils.openMedium(this.first.getMediumId()));

                Button showSecond = new Button("Show Second");
                showSecond.setOnAction(event -> ControllerUtils.openMedium(this.second.getMediumId()));
                root.getChildren().addAll(showFirst, showSecond);
                return root;
            }

            @Override
            String getTitle() {
                return this.first.getTitle() + " similar to " + this.second.getTitle();
            }
        }

    }

    private static class DanglingMediumTask extends AnalyzeWork {
        @Override
        void work(AnalyzerTask resultTask) throws Exception {
            final Repository repository = ApplicationConfig.getRepository();
            final List<SimpleMedium> media = repository.getSimpleMedium().firstNonNullElement().get();
            final List<MediaList> lists = repository.getLists().firstNonNullElement().get();

            final List<Integer> listIds = lists.stream().filter(mediaList -> !(mediaList instanceof ExternalMediaList))
                    .map(MediaList::getListId).collect(Collectors.toList());

            final Set<Integer> externalListItems = lists.stream()
                    .filter(mediaList -> mediaList instanceof ExternalMediaList).map(MediaList::getListId)
                    .map(repository::getExternalListItems).flatMap(Collection::stream).collect(Collectors.toSet());

            final Set<Integer> mediaWithLists = new HashSet<>(
                    repository.getListItems(listIds).firstNonNullElement().get());
            mediaWithLists.addAll(externalListItems);

            List<MediumWithoutListResult> results = new ArrayList<>();

            for (SimpleMedium medium : media) {
                if (!mediaWithLists.contains(medium.getMediumId())) {
                    results.add(new MediumWithoutListResult(medium));
                }
            }
            resultTask.update(new TotalMediaWithoutListResult(results));
        }

        private static class TotalMediaWithoutListResult extends AnalyzeGroupResult {

            private TotalMediaWithoutListResult(List<MediumWithoutListResult> results) {
                super(results);
            }

            @Override
            String getTitle() {
                return String.format("Media without Lists: %d", this.getIssueNumber());
            }
        }

        private static class MediumWithoutListResult extends AnalyzeResult {
            private final SimpleMedium medium;
            @FXML
            private Button showButton;
            @FXML
            private Button addButton;
            @FXML
            private ComboBox<MediaList> listSelect;
            @FXML
            private Text title;
            private LiveData<List<MediaList>> liveData;

            public MediumWithoutListResult(SimpleMedium medium) {
                this.medium = medium;
            }

            @Override
            Node createDisplay() {
                Node root = ControllerUtils.load("/analyze/danglingResult.fxml", this);

                if (this.liveData == null) {
                    this.initialize();
                }
                return root;
            }

            @FXML
            public void initialize() {
                final Repository repository = ApplicationConfig.getRepository();
                this.liveData = repository.getInternLists();
                this.liveData.observe(mediaLists -> {
                    if (mediaLists == null) {
                        return;
                    }
                    this.listSelect.getItems().setAll(mediaLists);
                });

                this.title.setText(this.medium.getTitle());
                this.showButton.setOnAction(event -> ControllerUtils.openMedium(this.medium.getMediumId()));
                this.listSelect.setCellFactory(param -> new MediaListCell());
                this.listSelect.setButtonCell(new MediaListCell());
                this.listSelect.setConverter(new StringConverter<MediaList>() {
                    @Override
                    public String toString(MediaList object) {
                        return object == null ? null : object.getName();
                    }

                    @Override
                    public MediaList fromString(String string) {
                        final List<MediaList> value = MediumWithoutListResult.this.liveData.getValue();
                        if (value == null) {
                            return null;
                        }
                        for (MediaList list : value) {
                            if (list.getName().equalsIgnoreCase(string)) {
                                return list;
                            }
                        }
                        return null;
                    }
                });
                this.addButton.disableProperty().bind(this.listSelect.valueProperty().isNull());
                this.addButton.setOnAction(event -> repository
                        .addMediumToList(this.listSelect.getValue().getListId(),
                                Collections.singleton(this.medium.getMediumId()))
                        .whenComplete((aBoolean, throwable) -> {
                            if (throwable != null) {
                                throwable.printStackTrace();
                            }
                            if (aBoolean != null && aBoolean) {
                                this.resolved();
                            }
                        }));
            }

            @Override
            String getTitle() {
                return String.format("'%s'", this.medium.getTitle());
            }

            private static class MediaListCell extends ListCell<MediaList> {
                @Override
                protected void updateItem(MediaList item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        this.setText(null);
                    } else {
                        this.setText(item.getName());
                    }
                }
            }
        }

    }

    private static class TocMismatchTask extends AnalyzeWork {

        @Override
        void work(AnalyzerTask resultTask) throws Exception {
            final Repository repository = ApplicationConfig.getRepository();
            final List<Toc> tocs = repository.getAllTocs();

            Map<Integer, SimpleMedium> idMediumMap = new HashMap<>();
            final List<SimpleMedium> media = repository.getSimpleMedium().firstNonNullElement().get();

            for (SimpleMedium medium : media) {
                idMediumMap.put(medium.getMediumId(), medium);
            }
            Map<String, Map<Integer, Set<Integer>>> domainMediumMediaMap = new HashMap<>();

            for (Toc toc : tocs) {
                final String domain = Utils.getDomain(toc.getLink());

                final Map<Integer, Set<Integer>> map = domainMediumMediaMap.computeIfAbsent(domain,
                        s -> new HashMap<>());
                final SimpleMedium medium = idMediumMap.get(toc.getMediumId());

                if (medium == null) {
                    System.out.println("dangling toc detected: " + toc);
                    continue;
                }
                map.computeIfAbsent(medium.getMedium(), i -> new HashSet<>()).add(toc.getMediumId());
            }

            List<DomainMisMatchResult> domainMisMatchResults = new ArrayList<>();

            for (Map.Entry<String, Map<Integer, Set<Integer>>> domainEntry : domainMediumMediaMap.entrySet()) {
                final String domain = domainEntry.getKey();
                List<TocMisMatchResult> misMatchResults = new ArrayList<>();

                final Map<Integer, Set<Integer>> mediumMediaMap = domainEntry.getValue();

                double total = mediumMediaMap.values().stream().mapToInt(Set::size).sum();
                Map<Integer, Double> mediumProportions = new HashMap<>();

                double highestProportion = 0;
                int expectedMediumType = 0;

                for (Map.Entry<Integer, Set<Integer>> entry : mediumMediaMap.entrySet()) {
                    final Integer mediumType = entry.getKey();
                    final Set<Integer> value = entry.getValue();

                    final double proportion = value.size() / total;

                    if (highestProportion < proportion) {
                        highestProportion = proportion;
                        expectedMediumType = mediumType;
                    }
                    mediumProportions.put(mediumType, proportion);
                }

                for (Map.Entry<Integer, Set<Integer>> entry : mediumMediaMap.entrySet()) {
                    final Integer mediumType = entry.getKey();
                    final Set<Integer> value = entry.getValue();

                    final double proportion = mediumProportions.get(mediumType);

                    if (proportion < 0.1) {
                        for (Integer mediumId : value) {
                            final SimpleMedium medium = idMediumMap.get(mediumId);
                            misMatchResults.add(new TocMisMatchResult(medium.getMedium(), expectedMediumType, mediumId,
                                    medium.getTitle()));
                        }
                    }
                }
                domainMisMatchResults.add(new DomainMisMatchResult(domain, misMatchResults));
            }
            resultTask.update(new TotalMisMatchResult(domainMisMatchResults));
        }

        private static class TotalMisMatchResult extends AnalyzeGroupResult {

            private TotalMisMatchResult(List<DomainMisMatchResult> results) {
                super(results);
            }

            @Override
            int getIssueNumber() {
                return this.getSubResult().stream().mapToInt(AnalyzeResult::getIssueNumber).sum();
            }

            @Override
            String getTitle() {
                return String.format("Domain Mismatches: %d", this.getIssueNumber());
            }
        }

        private static class DomainMisMatchResult extends AnalyzeGroupResult {
            private final String domain;

            public DomainMisMatchResult(String domain, List<TocMisMatchResult> results) {
                super(results);
                this.domain = domain;
            }

            @Override
            Node createDisplay() {
                return null;
            }

            @Override
            String getTitle() {
                return String.format("'%s' Mismatches: %d", this.domain, this.getIssueNumber());
            }
        }

        private static class TocMisMatchResult extends AnalyzeResult {
            private final MediumType.Medium currentMediumType;
            private final MediumType.Medium expectedMediumType;
            private final int mediumId;
            private final String mediumTitle;

            private TocMisMatchResult(int currentMediumType, int expectedMediumType, int mediumId, String mediumTitle) {
                this.currentMediumType = MediumType.getMedium(currentMediumType);
                this.expectedMediumType = MediumType.getMedium(expectedMediumType);
                this.mediumId = mediumId;
                this.mediumTitle = mediumTitle;
            }

            @Override
            Node createDisplay() {
                return null;
            }

            @Override
            String getTitle() {
                return String.format("'%s': Expected MediumType: %s, but has MediumType: %s", this.mediumTitle,
                        this.expectedMediumType.getName(), this.currentMediumType.getName());
            }
        }
    }

    private static class TaskService extends Service<AnalyzerTask.TotalResult> {
        @Override
        protected Task<AnalyzerTask.TotalResult> createTask() {
            return new AnalyzerTask();
        }
    }

    private static class AnalyzerTask extends Task<AnalyzerTask.TotalResult> {
        private final WeakHashMap<Class<? extends AnalyzeResult>, AnalyzeResult> resultMap = new WeakHashMap<>();
        private TotalResult current;

        @Override
        protected AnalyzerTask.TotalResult call() throws Exception {
            new SimilarMediaTask().work(this);
            new TocMismatchTask().work(this);
            new DanglingMediumTask().work(this);
            new ReadGapTask().work(this);
            new EpisodeGapTask().work(this);
            return current;
        }

        private void update(AnalyzeResult result) {
            this.resultMap.put(result.getClass(), result);
            this.current = new TotalResult(new ArrayList<>(this.resultMap.values()));
            this.updateValue(this.current);
        }

        private static class TotalResult extends AnalyzeGroupResult {
            private TotalResult(List<AnalyzeResult> results) {
                super(results);
            }

            @Override
            int getIssueNumber() {
                return this.getSubResult().stream().mapToInt(AnalyzeResult::getIssueNumber).sum();
            }

            @Override
            String getTitle() {
                return "Total possible Issues: " + this.getIssueNumber();
            }
        }

        private static class ReadGapTask extends AnalyzeWork {
            @Override
            void work(AnalyzerTask resultTask) {
                final Repository repository = ApplicationConfig.getRepository();
                final List<MediumEpisode> mediumEpisodes = repository.getMediumEpisodes();
                Map<Integer, List<MediumEpisode>> mediumEpisodesMap = new HashMap<>();

                for (MediumEpisode episode : mediumEpisodes) {
                    mediumEpisodesMap.computeIfAbsent(episode.mediumId, i -> new ArrayList<>()).add(episode);
                }
                List<ReadGapResult> gapResults = new ArrayList<>();

                for (Map.Entry<Integer, List<MediumEpisode>> entry : mediumEpisodesMap.entrySet()) {
                    final Integer mediumId = entry.getKey();
                    SimpleMedium medium = null;
                    final List<MediumEpisode> episodes = entry.getValue();
                    episodes.sort(Comparator.comparingDouble(MediumEpisode::getCombiIndex));

                    double previousReadIndex = -1;
                    List<MediumEpisode> unreadEpisodes = new ArrayList<>();

                    for (MediumEpisode episode : episodes) {
                        if (!episode.isRead()) {
                            unreadEpisodes.add(episode);
                        } else {
                            if (!unreadEpisodes.isEmpty()) {
                                if (medium == null) {
                                    medium = repository.getSimpleMedium(mediumId);
                                }
                                gapResults.add(new ReadGapResult(previousReadIndex, episode.combiIndex, unreadEpisodes,
                                        medium.getTitle()));
                                unreadEpisodes.clear();
                            }
                            previousReadIndex = episode.combiIndex;
                        }
                    }
                }
                resultTask.update(new TotalReadGaps(gapResults));
            }

            private static class ReadGapResult extends AnalyzeResult {
                private final double previousRead;
                private final double afterRead;
                private final List<MediumEpisode> betweenUnread;
                private final String mediumTitle;

                private ReadGapResult(double previousRead, double afterRead, List<MediumEpisode> betweenUnread,
                        String mediumTitle) {
                    this.previousRead = previousRead;
                    this.afterRead = afterRead;
                    this.betweenUnread = new ArrayList<>(betweenUnread);
                    this.mediumTitle = mediumTitle;
                }

                @Override
                Node createDisplay() {
                    return null;
                }

                @Override
                String getTitle() {
                    return String.format("'%s' has a reading gap between %s and %s of %d Episodes", this.mediumTitle,
                            new BigDecimal(this.previousRead).stripTrailingZeros().toPlainString(),
                            new BigDecimal(this.afterRead).stripTrailingZeros().toPlainString(),
                            this.betweenUnread.size());
                }
            }

            private static class TotalReadGaps extends AnalyzeGroupResult {
                protected TotalReadGaps(List<ReadGapResult> results) {
                    super(results);
                }

                @Override
                String getTitle() {
                    return "Media with Reading Gaps: " + this.getIssueNumber();
                }
            }

        }

        private static class EpisodeGapTask extends AnalyzeWork {
            @Override
            void work(AnalyzerTask resultTask) {
                final Repository repository = ApplicationConfig.getRepository();
                final List<MediumEpisode> mediumEpisodes = repository.getMediumEpisodes();
                Map<Integer, List<MediumEpisode>> mediumEpisodesMap = new HashMap<>();

                for (MediumEpisode episode : mediumEpisodes) {
                    mediumEpisodesMap.computeIfAbsent(episode.mediumId, i -> new ArrayList<>()).add(episode);
                }
                List<EpisodeGapResult> gapResults = new ArrayList<>();

                for (Map.Entry<Integer, List<MediumEpisode>> entry : mediumEpisodesMap.entrySet()) {
                    final Integer mediumId = entry.getKey();
                    SimpleMedium medium = null;
                    final List<MediumEpisode> episodes = entry.getValue();
                    episodes.sort(Comparator.comparingDouble(MediumEpisode::getCombiIndex));

                    double previousIndex = -1;

                    for (MediumEpisode episode : episodes) {
                        if (previousIndex >= 0) {
                            if (Math.abs(previousIndex - episode.combiIndex) >= 2) {
                                if (medium == null) {
                                    medium = repository.getSimpleMedium(mediumId);
                                }
                                gapResults.add(
                                        new EpisodeGapResult(previousIndex, episode.combiIndex, medium.getTitle()));
                            }
                        }
                        previousIndex = episode.combiIndex;
                    }
                }
                resultTask.update(new TotalEpisodeGaps(gapResults));
            }

            private static class EpisodeGapResult extends AnalyzeResult {
                private final double previousIndex;
                private final double afterIndex;
                private final String mediumTitle;

                private EpisodeGapResult(double previousIndex, double afterIndex, String mediumTitle) {
                    this.previousIndex = previousIndex;
                    this.afterIndex = afterIndex;
                    this.mediumTitle = mediumTitle;
                }

                @Override
                Node createDisplay() {
                    return null;
                }

                @Override
                String getTitle() {
                    return String.format("'%s' has an episode gap between %s and %s", this.mediumTitle,
                            new BigDecimal(this.previousIndex).stripTrailingZeros().toPlainString(),
                            new BigDecimal(this.afterIndex).stripTrailingZeros().toPlainString());
                }
            }

            private static class TotalEpisodeGaps extends AnalyzeGroupResult {
                protected TotalEpisodeGaps(List<? extends AnalyzeResult> results) {
                    super(results);
                }

                @Override
                String getTitle() {
                    return "Media with Episode Gaps: " + this.getIssueNumber();
                }
            }

        }

    }
}
