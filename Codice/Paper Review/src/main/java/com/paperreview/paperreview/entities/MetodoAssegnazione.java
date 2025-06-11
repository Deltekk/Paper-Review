package com.paperreview.paperreview.entities;

public enum MetodoAssegnazione {
    Broadcast,
    Topic;

    public static MetodoAssegnazione fromString(String value) {
        for (MetodoAssegnazione m : MetodoAssegnazione.values()) {
            if (m.name().equalsIgnoreCase(value.trim())) {
                return m;
            }
        }
        throw new IllegalArgumentException("Valore MetodoAssegnazione non valido: " + value);
    }
}
