package com.paperreview.paperreview.entities;

public enum Ruolo {
    Chair,
    Revisore,
    Sottorevisore,
    Autore,
    Editor;

    public static Ruolo fromString(String value) {
        for (Ruolo r : Ruolo.values()) {
            if (r.name().equalsIgnoreCase(value.trim())) {
                return r;
            }
        }
        throw new IllegalArgumentException("Valore Ruolo non valido: " + value);
    }
}