package com.mytlogos.enterprisedesktop.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

/**
 *
 */
public class ThreeStatesController {
    @FXML
    private ToggleButton yes;
    @FXML
    private ToggleButton no;
    @FXML
    private ToggleButton ignore;
    @FXML
    private ToggleGroup statesGroup;
    @FXML
    private Text text;
    private final ObjectProperty<State> states = new ObjectPropertyBase<State>() {

        @Override
        public Object getBean() {
            return ThreeStatesController.this;
        }

        @Override
        public String getName() {
            return "states";
        }

        @Override
        protected void invalidated() {
            final State state = this.get();

            if (state == State.YES) {
                statesGroup.selectToggle(yes);
            } else if (state == State.NO) {
                statesGroup.selectToggle(no);
            } else {
                statesGroup.selectToggle(ignore);
            }
        }
    };

    public State getStates() {
        return states.get();
    }

    public ObjectProperty<State> statesProperty() {
        return states;
    }

    public void setStates(State states) {
        this.states.set(states);
    }

    public void initialize() {
        this.statesGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == yes) {
                this.states.set(State.YES);
            } else if (newValue == no) {
                this.states.set(State.NO);
            } else {
                this.states.set(State.IGNORE);
            }
        });
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public enum State {
        YES,
        NO,
        IGNORE
    }
}
