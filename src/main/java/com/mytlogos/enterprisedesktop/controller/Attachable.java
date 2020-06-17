package com.mytlogos.enterprisedesktop.controller;

/**
 *
 */
public interface Attachable {
    void onAttach();

    void onDetach();

    default void setParentController(MainController controller) {

    }
}
