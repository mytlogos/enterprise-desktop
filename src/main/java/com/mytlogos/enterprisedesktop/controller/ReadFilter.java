package com.mytlogos.enterprisedesktop.controller;

/**
 *
 */
public enum ReadFilter implements MainController.DisplayValue {
    IGNORE("Ignore Read Filter", (byte) -1),
    READ_ONLY("Read only", (byte) 1),
    UNREAD_ONLY("Unread only", (byte) 0);

    private final String display;
    private final byte value;

    ReadFilter(String display, byte value) {
        this.display = display;
        this.value = value;
    }

    public static ReadFilter getValue(byte readFilter) {
        for (ReadFilter value : ReadFilter.values()) {
            if (value.value == readFilter) {
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
        return value;
    }
}
