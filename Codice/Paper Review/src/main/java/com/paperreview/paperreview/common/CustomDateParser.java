package com.paperreview.paperreview.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateParser {

    public static String parseDate(LocalDateTime date){
        // Definisci il formato della data con i "/"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.toLocalDate().format(formatter);

        return formattedDate;
    }

}
