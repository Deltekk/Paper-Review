package com.paperreview.paperreview.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateParser {

    public static String parseDate(LocalDateTime date){
        // Definisci il formato della data con i "/"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.toLocalDate().format(formatter);

        return formattedDate;
    }

    public static boolean isBetweenSecondInclusive(LocalDate date, LocalDateTime start, LocalDateTime end) {
        return (date.isAfter(start.toLocalDate())) &&
                (date.isEqual(end.toLocalDate()) || date.isBefore(end.toLocalDate()));
    }

}
