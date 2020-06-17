package com.mytlogos.enterprisedesktop.model;

public class MediumType {
    public static final int NOVEL = 0x1;
    public static final int MANGA = 0x2;
    public static final int ANIME = 0x4;
    public static final int SERIES = 0x8;
    public static final int ALL = MediumType.ANIME | MediumType.MANGA | MediumType.NOVEL | MediumType.SERIES;
    public static final int TEXT = 0x1;
    public static final int AUDIO = 0x2;
    public static final int VIDEO = 0x4;
    public static final int IMAGE = 0x8;

    public static Medium getMedium(int medium) {
        if (medium == Medium.TEXT.value) {
            return Medium.TEXT;
        } else if (medium == Medium.IMAGE.value) {
            return Medium.IMAGE;
        } else if (medium == Medium.VIDEO.value) {
            return Medium.VIDEO;
        } else if (medium == Medium.AUDIO.value) {
            return Medium.AUDIO;
        } else {
            throw new IllegalArgumentException("no definite medium type");
        }
    }

    public static int addMediumType(int mediumType, int toAdd) {
        return mediumType | toAdd;
    }

    public static int removeMediumType(int mediumType, int toRemove) {
        return mediumType & ~toRemove;
    }

    public static int toggleMediumType(int mediumType, int toToggle) {
        return mediumType ^ toToggle;
    }

    public static boolean is(int type, int toCheck) {
        return (type & toCheck) == toCheck;
    }

    public static boolean intersect(int type, int toCheck) {
        return (type & toCheck) > 0;
    }

    public enum Medium {
        TEXT(0x1, "Text"),
        AUDIO(0x2, "Audio"),
        VIDEO(0x4, "Video"),
        IMAGE(0x8, "Image");

        private final int value;
        private final String name;

        Medium(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }
}
