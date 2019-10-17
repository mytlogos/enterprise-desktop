package com.mytlogos.enterprisedesktop;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Formatter {
    public static String isoFormat(LocalDateTime date) {
        return date == null ? null : date.toInstant(ZoneOffset.UTC).toString();
    }

    public static String format(LocalDateTime date) {
        return date == null ? null : DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm").format(date);
    }

    public static LocalDateTime parseLocalDateTime(String isoDate) {
        return isoDate == null ? null : LocalDateTime.ofInstant(Instant.parse(isoDate), ZoneId.systemDefault());
    }
}
