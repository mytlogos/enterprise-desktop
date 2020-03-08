package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.background.sqlite.life.LiveData;
import com.mytlogos.enterprisedesktop.background.sqlite.life.MediatorLiveData;
import com.mytlogos.enterprisedesktop.model.MediumType;
import com.mytlogos.enterprisedesktop.tools.TriFunction;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 *
 */
public class ControllerUtils {
    public static <R> LiveData<R> fxObservableToLiveData(ObservableValue<R> value) {
        MediatorLiveData<R> mediatorLiveData = new MediatorLiveData<>();
        value.addListener(observable -> mediatorLiveData.postValue(value.getValue()));
        return mediatorLiveData;
    }

    static <R, T1, T2> LiveData<R> combineLatest(LiveData<T1> t1Observable, ObservableValue<T2> t2Observable, BiFunction<T1, T2, R> combiner) {
        MediatorLiveData<R> mediatorLiveData = new MediatorLiveData<>();
        t2Observable.addListener(observable -> mediatorLiveData.postValue(combiner.apply(t1Observable.getValue(), t2Observable.getValue())));
        mediatorLiveData.addSource(t1Observable, t1 -> mediatorLiveData.postValue(combiner.apply(t1, t2Observable.getValue())));
        return mediatorLiveData;
    }

    static <R, T1, T2, T3> LiveData<R> combineLatest(LiveData<T1> t1LiveData, ObservableValue<T2> t2Observable, ObservableValue<T3> t3Observable, TriFunction<T1, T2, T3, R> combiner) {
        MediatorLiveData<R> mediatorLiveData = new MediatorLiveData<>();
        t2Observable.addListener(observable -> mediatorLiveData.postValue(combiner.apply(t1LiveData.getValue(), t2Observable.getValue(), t3Observable.getValue())));
        t3Observable.addListener(observable -> mediatorLiveData.postValue(combiner.apply(t1LiveData.getValue(), t2Observable.getValue(), t3Observable.getValue())));
        mediatorLiveData.addSource(t1LiveData, t1 -> mediatorLiveData.postValue(combiner.apply(t1, t2Observable.getValue(), t3Observable.getValue())));
        return mediatorLiveData;
    }

    public static <T> T load(String fxmlFile) {
        return load(fxmlFile, null);
    }

    public static <T, P extends Node> T load(String fxmlFile, Consumer<P> consumer) {
        final FXMLLoader loader = new FXMLLoader(ControllerUtils.class.getResource(fxmlFile));
        try {
            final P load = loader.load();
            if (consumer != null) {
                consumer.accept(load);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (consumer != null) {
                consumer.accept(null);
            }
        }
        return loader.getController();
    }

    public static <P, T extends Node> T loadNode(String fxmlFile, Consumer<P> consumer) {
        final FXMLLoader loader = new FXMLLoader(ControllerUtils.class.getResource(fxmlFile));
        try {
            final T load = loader.load();
            if (consumer != null) {
                consumer.accept(loader.getController());
            }
            return load;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <P extends Node> P load(String fxmlFile, Object controller) {
        final FXMLLoader loader = new FXMLLoader(ControllerUtils.class.getResource(fxmlFile));
        loader.setController(controller);
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getMedium(CheckBox text, CheckBox image, CheckBox video, CheckBox audio) {
        int medium = 0;
        medium = toggleValue(text.isSelected(), medium, MediumType.TEXT);
        medium = toggleValue(image.isSelected(), medium, MediumType.IMAGE);
        medium = toggleValue(video.isSelected(), medium, MediumType.VIDEO);
        return toggleValue(audio.isSelected(), medium, MediumType.AUDIO);
    }

    private static int toggleValue(boolean add, int medium, int toggleValue) {
        if (add) {
            return MediumType.addMediumType(medium, toggleValue);
        } else {
            return MediumType.removeMediumType(medium, toggleValue);
        }
    }

    public static int getMedium(RadioButton text, RadioButton image, RadioButton video, RadioButton audio) {
        int medium = 0;
        medium = toggleValue(text.isSelected(), medium, MediumType.TEXT);
        medium = toggleValue(image.isSelected(), medium, MediumType.IMAGE);
        medium = toggleValue(video.isSelected(), medium, MediumType.VIDEO);
        return toggleValue(audio.isSelected(), medium, MediumType.AUDIO);
    }

    public static void setMedium(int medium, CheckBox text, CheckBox image, CheckBox video, CheckBox audio) {
        text.setSelected(MediumType.is(medium, MediumType.TEXT));
        image.setSelected(MediumType.is(medium, MediumType.IMAGE));
        video.setSelected(MediumType.is(medium, MediumType.VIDEO));
        audio.setSelected(MediumType.is(medium, MediumType.AUDIO));
    }

    public static void setMedium(int medium, RadioButton text, RadioButton image, RadioButton video, RadioButton audio) {
        text.setSelected(MediumType.is(medium, MediumType.TEXT));
        image.setSelected(MediumType.is(medium, MediumType.IMAGE));
        video.setSelected(MediumType.is(medium, MediumType.VIDEO));
        audio.setSelected(MediumType.is(medium, MediumType.AUDIO));
    }

    public static TextFormatter<Integer> integerTextFormatter() {
        StringConverter<Integer> stringConverter = new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return object == null ? "" : object + "";
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        };
        UnaryOperator<TextFormatter.Change> changeUnaryOperator = change -> {
            if (!change.getControlNewText().matches("^\\d+$")) {
                change.setText("");
            }
            return change;
        };
        return new TextFormatter<>(stringConverter, 0, changeUnaryOperator);
    }

    public static TextFormatter<Double> doubleTextFormatter() {
        StringConverter<Double> stringConverter = new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return object == null ? "" : object + "";
            }

            @Override
            public Double fromString(String string) {
                try {
                    return Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        };
        UnaryOperator<TextFormatter.Change> changeUnaryOperator = change -> {
            if (!change.getControlNewText().matches("^\\d+[.,]?\\d*$")) {
                change.setText("");
            }
            return change;
        };
        return new TextFormatter<>(stringConverter, 0d, changeUnaryOperator);
    }

    public static <T> void addDeleteHandler(ListView<T> listView) {
        listView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                listView.getItems().removeAll(listView.getSelectionModel().getSelectedItems());
            }
        });
    }

    static <T> void addAutoCompletionBinding(TextField mediumFilter, LiveData<List<T>> liveData, Function<T, String> stringFunction, Consumer<T> completed) {
        new AutoCompletionTextFieldBinding<>(
                mediumFilter,
                param -> {
                    if (param.isCancelled()) {
                        return Collections.emptyList();
                    }

                    final String[] simpleWords = param.getUserText().toLowerCase().split("\\W");
                    final List<T> values = liveData.getValue();

                    if (values == null) {
                        return Collections.emptyList();
                    }

                    TreeMap<Integer, List<T>> countMatched = new TreeMap<>();

                    for (T value : values) {
                        final String title = stringFunction.apply(value).toLowerCase();
                        int match = 0;
                        int count = 0;
                        for (String word : simpleWords) {
                            if (title.contains(word)) {
                                match += word.length();
                                count++;
                            }
                        }
                        if (count > 0) {
                            countMatched.computeIfAbsent(match * count, integer -> new LinkedList<>()).add(value);
                        }
                    }
                    List<T> suggestions = new LinkedList<>();

                    for (Integer match : countMatched.descendingKeySet()) {
                        suggestions.addAll(countMatched.get(match));
                    }
                    return suggestions;
                },
                new StringConverter<T>() {
                    @Override
                    public String toString(T object) {
                        return stringFunction.apply(object);
                    }

                    @Override
                    public T fromString(String string) {
                        final List<T> values = liveData.getValue();
                        if (values == null || values.isEmpty()) {
                            return null;
                        }
                        for (T value : values) {
                            if (stringFunction.apply(value).equals(string)) {
                                return value;
                            }
                        }
                        return null;
                    }
                }).setOnAutoCompleted(event -> {
            final T t = event.getCompletion();
            completed.accept(t);
        });
    }

    /**
     * Call only when Control is associated with an Scene and Window.
     */
    static void showTooltip(Control control, String tooltipText) {
        Point2D p = control.localToScene(0.0, 0.0);
        final Tooltip customTooltip = new Tooltip();
        customTooltip.setText(tooltipText);

        control.setTooltip(customTooltip);
        customTooltip.setAutoHide(true);
        final Scene scene = control.getScene();
        customTooltip.show(
                scene.getWindow(),
                p.getX() + scene.getX() + scene.getWindow().getX(),
                p.getY() + scene.getY() + scene.getWindow().getY()
        );
    }

    static void openUrl(String link) {
        try {
            Desktop.getDesktop().browse(URI.create(link));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
