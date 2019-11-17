package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.model.MediumType;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function3;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 *
 */
public class ControllerUtils {
    public static <R> Flowable<R> fxObservableToFlowable(ObservableValue<R> ObservableValue) {
        return JavaFxObservable.valuesOf(ObservableValue).toFlowable(BackpressureStrategy.LATEST);
    }

    static <R, T1, T2> Flowable<R> combineLatest(Flowable<T1> t1Observable, ObservableValue<T2> t2Observable, BiFunction<T1, T2, R> combiner) {
        return Flowable.combineLatest(
                t1Observable,
                JavaFxObservable.valuesOf(t2Observable).toFlowable(BackpressureStrategy.LATEST),
                combiner
        );
    }

    static <R, T1, T2, T3> Flowable<R> combineLatest(Flowable<T1> t1Flowable, ObservableValue<T2> t2Observable, ObservableValue<T3> t3Observable, Function3<T1, T2, T3, R> combiner) {
        final Flowable<T2> t2Flowable = JavaFxObservable.valuesOf(t2Observable).toFlowable(BackpressureStrategy.LATEST);
        final Flowable<T3> t3Flowable = JavaFxObservable.valuesOf(t3Observable).toFlowable(BackpressureStrategy.LATEST);
        return Flowable.combineLatest(
                t1Flowable,
                t2Flowable,
                t3Flowable,
                combiner
        );
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
}
