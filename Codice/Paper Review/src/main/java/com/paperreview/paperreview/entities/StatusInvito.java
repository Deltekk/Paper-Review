package com.paperreview.paperreview.entities;

public enum StatusInvito {
    Inviato,
    Accettato,
    Rifiutato;

    public static StatusInvito fromString(String value) {
        for (StatusInvito s : StatusInvito.values()) {
            if (s.name().equalsIgnoreCase(value.trim())) {
                return s;
            }
        }
        throw new IllegalArgumentException("Valore Status non valido: " + value);
    }
}
