package com.mytlogos.enterprisedesktop.model;

import com.mytlogos.enterprisedesktop.controller.MainController;

/**
 *
 */
public enum ReleaseState implements MainController.DisplayValue {
    ANY(-1, "Any", false),
    UNKNOWN(0, "Unknown", false),
    ONGOING(1, "Ongoing", false),
    HIATUS(2, "Hiatus", false),
    DISCONTINUED(3, "Discontinued", true),
    DROPPED(4, "Dropped", true),
    COMPLETE(5, "Completed", true);

    private final int value;
    private final String name;
    private final boolean end;

    ReleaseState(int value, String name, boolean end) {
        this.value = value;
        this.name = name;
        this.end = end;
    }

    public static ReleaseState getInstance(int state) {
        switch (state) {
            case 0:
                return UNKNOWN;
            case 1:
                return ONGOING;
            case 2:
                return HIATUS;
            case 3:
                return DISCONTINUED;
            case 4:
                return DROPPED;
            case 5:
                return COMPLETE;
            default:
                throw new IllegalArgumentException("unknown state: " + state);
        }
    }

    public boolean isEnd() {
        return this.end;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String getDisplayValue() {
        return this.name;
    }
}
