package com.mytlogos.enterprisedesktop.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

/**
 *
 */
public class MediumTypes {
    @FXML
    private ToggleButton textButton;
    @FXML
    private ToggleButton imageButton;
    @FXML
    private ToggleButton videoButton;
    @FXML
    private ToggleButton audioButton;
    private final ObjectProperty<Integer> mediumProperty = new ObjectPropertyBase<Integer>() {
        @Override
        public Object getBean() {
            return MediumTypes.this;
        }

        @Override
        public String getName() {
            return "mediumProperty";
        }

        @Override
        protected void invalidated() {
            ControllerUtils.setMedium(this.get(), textButton, imageButton, videoButton, audioButton);
        }
    };

    public void setEditable(boolean editable) {
        this.textButton.setDisable(!editable);
        this.imageButton.setDisable(!editable);
        this.videoButton.setDisable(!editable);
        this.audioButton.setDisable(!editable);
    }

    public Integer getMedium() {
        return mediumProperty.get();
    }

    public void setMedium(Integer medium) {
        this.mediumProperty.set(medium);
    }

    public ObjectProperty<Integer> mediumProperty() {
        return mediumProperty;
    }

    public void initialize() {
        ControllerUtils.listen(
                () -> mediumProperty.set(ControllerUtils.getMedium(textButton, imageButton, videoButton, audioButton)),
                textButton.selectedProperty(),
                imageButton.selectedProperty(),
                videoButton.selectedProperty(),
                audioButton.selectedProperty()
        );
    }
}
