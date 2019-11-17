package com.mytlogos.enterprisedesktop.model;

/**
 *
 */
public enum TlState {
    HIATUS("Hiatus"),
    COMPLETE("Complete"),
    ONGOING("Ongoing"),
    DROPPED("Dropped"),
    ;

    private final String displayState;

    TlState(String displayState) {
        this.displayState = displayState;
    }

    public String getDisplayState() {
        return displayState;
    }
}
