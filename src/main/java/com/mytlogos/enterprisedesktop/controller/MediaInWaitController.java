package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.sqlite.PagedList;
import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.background.sqlite.life.Observer;
import com.mytlogos.enterprisedesktop.model.MediaList;
import com.mytlogos.enterprisedesktop.model.MediumInWait;
import com.mytlogos.enterprisedesktop.model.SimpleMedium;
import com.mytlogos.enterprisedesktop.tools.AllSortings;
import com.mytlogos.enterprisedesktop.tools.Utils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.controlsfx.control.Notifications;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class MediaInWaitController implements Attachable {
    @FXML
    private CheckBox groupByName;
    @FXML
    private Text title;
    @FXML
    private TextField selectList;
    @FXML
    private TextField selectMedium;
    @FXML
    private TextField searchSimilar;
    @FXML
    private ListView<MediumInWait> similarMedia;
    @FXML
    private Button finish;
    @FXML
    private TextField ignoreNameFilter;
    @FXML
    private ListView<MediumInWait> mediumInWaitListView;
    private final Observer<List<MediumInWait>> mediumInWaitObserver = this::setItems;
    @FXML
    private HBox showMedium;
    @FXML
    private MediumTypes showMediumController;
    @FXML
    private TextField nameFilter;
    @FXML
    private TextField hostFilter;
    private LiveData<PagedList<MediumInWait>> mediumInWaitLiveData;
    private MediaList selectedList;
    private SimpleMedium selectedMedium;
    private boolean running;
    private MediumInWait mediumInWait;
    private ObjectBinding<LiveData<PagedList<MediumInWait>>> binding;

    public void initialize() {
        this.showMediumController.setMedium(0);
        final Repository repository = ApplicationConfig.getRepository();
        this.binding = Bindings.createObjectBinding(
                () -> repository.getMediaInWaitBy(
                        this.nameFilter.getText(),
                        this.showMediumController.getMedium(),
                        this.hostFilter.getText(),
                        AllSortings.TITLE_AZ
                ),
                this.hostFilter.textProperty(),
                this.nameFilter.textProperty(),
                this.showMediumController.mediumProperty(),
                this.groupByName.selectedProperty()
        );
        final ChangeListener<LiveData<PagedList<MediumInWait>>> changeListener = (observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.removeObserver(this.mediumInWaitObserver);
            }
            newValue.observe(this.mediumInWaitObserver);
            this.mediumInWaitLiveData = newValue;
        };
        changeListener.changed(binding, null, binding.getValue());
        binding.addListener(changeListener);
        this.mediumInWaitListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                this.title.setText("");
                this.mediumInWait = null;
                return;
            }
            this.mediumInWait = newValue;
            this.title.setText(newValue.getTitle());
            final List<MediumInWait> similarMediaInWait = repository.getSimilarMediaInWait(newValue);
            this.similarMedia.getItems().setAll(similarMediaInWait);
        });
        this.selectList.setOnKeyPressed(event -> {
            this.selectMedium.clear();
            this.finish.setText("Add as Medium to List");
        });
        this.selectMedium.setOnKeyPressed(event -> {
            this.selectList.clear();
            this.finish.setText("Add as Toc to Medium");
        });
        this.finish.setDefaultButton(true);
        this.finish.setOnAction(event -> this.process(repository));
        ControllerUtils.addDeleteHandler(this.similarMedia);

        final LiveData<List<SimpleMedium>> media = repository.getSimpleMedium();
        media.observe(Utils.emptyObserver());

        final LiveData<List<MediaList>> lists = repository.getInternLists();
        lists.observe(Utils.emptyObserver());

        ControllerUtils.addAutoCompletionBinding(
                this.selectList,
                lists,
                MediaList::getName,
                mediaList -> this.selectedList = mediaList
        );

        ControllerUtils.addAutoCompletionBinding(
                this.selectMedium,
                media,
                SimpleMedium::getTitle,
                medium -> this.selectedMedium = medium
        );

        final Callback<ListView<MediumInWait>, ListCell<MediumInWait>> cellFactory = param -> {
            final ListCell<MediumInWait> cell = new ListCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(
                    () -> {
                        final MediumInWait item = cell.getItem();
                        if (item == null) {
                            return "";
                        }
                        final String domain = Utils.getDomain(item.getLink());
                        final String medium = Utils.mediumToString(item.getMedium());
                        return String.format("%s [%s - %s]", item.getTitle(), medium, domain);
                    },
                    cell.itemProperty()
                    )
            );
            return cell;
        };
        this.mediumInWaitListView.setCellFactory(cellFactory);
        this.similarMedia.setCellFactory(cellFactory);
    }

    private void process(Repository repository) {
        if (this.running) {
            return;
        }
        this.running = true;
        if (this.mediumInWait == null) {
            ControllerUtils.showTooltip(this.finish, "No Medium selected");
        }

        if (this.selectedList != null) {
            List<MediumInWait> similarMediumInWaits = new ArrayList<>(this.similarMedia.getItems());
            similarMediumInWaits.add(this.mediumInWait);
            MediaList item = this.selectedList;

            CompletableFuture<Boolean> success = repository.createMedium(this.mediumInWait, similarMediumInWaits, item);
            success.whenComplete((aBoolean, throwable) -> {
                String msg;
                if (aBoolean == null || !aBoolean || throwable != null) {
                    msg = "Could not create Medium";
                } else {
                    msg = "Created a Medium and consumed " + similarMediumInWaits.size() + " other unused Media";
                }
                Platform.runLater(() -> {
                    this.running = false;
                    this.selectedList = null;
                    this.selectList.clear();
                    this.mediumInWaitListView.getItems().clear();
                    Notifications.create().title(msg).show();
                });
            });
        } else if (this.selectedMedium != null) {
            List<MediumInWait> similarMediumInWaits = new ArrayList<>(this.similarMedia.getItems());
            similarMediumInWaits.add(this.mediumInWait);

            CompletableFuture<Boolean> success = repository.consumeMediumInWait(this.selectedMedium, similarMediumInWaits);
            success.whenComplete((aBoolean, throwable) -> {
                String msg;
                if (aBoolean == null || !aBoolean || throwable != null) {
                    msg = "Could not process Media";
                } else {
                    msg = "Consumed " + similarMediumInWaits.size() + " Media";
                }
                Platform.runLater(() -> {
                    this.running = false;
                    this.selectedMedium = null;
                    this.selectMedium.clear();
                    this.mediumInWaitListView.getItems().clear();
                    Notifications.create().title(msg).show();
                });
            });
        }
    }

    @Override
    public void onAttach() {
        if (this.mediumInWaitLiveData != null) {
            this.mediumInWaitLiveData.removeObserver(this.mediumInWaitObserver);
            this.mediumInWaitLiveData.observe(this.mediumInWaitObserver);
        }
    }

    @Override
    public void onDetach() {
        if (this.mediumInWaitLiveData != null) {
            this.mediumInWaitLiveData.removeObserver(this.mediumInWaitObserver);
        }
    }

    public void openUrl() {
        final MediumInWait item = this.mediumInWaitListView.getSelectionModel().getSelectedItem();

        if (item != null) {
            ControllerUtils.openUrl(item.getLink());
        }
    }

    private void setItems(List<MediumInWait> mediumInWaits) {
        if (this.groupByName.isSelected()) {
            List<MediumInWait> filtered = new ArrayList<>(mediumInWaits.size());
            Set<String> uniqueNames = new HashSet<>();

            for (MediumInWait inWait : mediumInWaits) {
                if (uniqueNames.add(inWait.getTitle())) {
                    filtered.add(inWait);
                }
            }
            mediumInWaits = filtered;
        }
        this.mediumInWaitListView.getItems().setAll(mediumInWaits);
    }
}
