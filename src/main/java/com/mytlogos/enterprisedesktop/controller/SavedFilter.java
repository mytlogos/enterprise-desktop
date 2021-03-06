package com.mytlogos.enterprisedesktop.controller;

/**
 *
 */
public enum SavedFilter implements MainController.DisplayValue {
    IGNORE("Ignore Saved Filter", (byte) -1),
    SAVED_ONLY("Saved only", (byte) 1),
    UNSAVED_ONLY("Unsaved only", (byte) 0);

    private final String display;
    private final byte value;

    SavedFilter(String display, byte value) {
        this.display = display;
        this.value = value;
    }

    public static SavedFilter getValue(byte savedFilter) {
        for (SavedFilter value : SavedFilter.values()) {
            if (value.value == savedFilter) {
                return value;
            }
        }
        throw new IllegalArgumentException("unknown value");
    }

    @Override
    public String getDisplayValue() {
        return this.display;
    }

    public byte getValue() {
        return this.value;
    }
}
