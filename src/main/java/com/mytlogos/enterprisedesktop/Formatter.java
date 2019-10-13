package com.mytlogos.enterprisedesktop;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Formatter {
    public static String formatLocalDateTime(LocalDateTime date) {
        return DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm").format(date);
    }

    public static LocalDateTime parseLocalDateTime(String isoDate) {
        return LocalDateTime.ofInstant(Instant.parse(isoDate), ZoneId.systemDefault());
    }
}
