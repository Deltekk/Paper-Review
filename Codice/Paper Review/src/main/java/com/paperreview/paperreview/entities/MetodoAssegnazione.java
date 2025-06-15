package com.paperreview.paperreview.entities;

public enum MetodoAssegnazione {
    Broadcast("Broadcast"),
    Topic("Topic"),;

    private final String valoreDb;

    MetodoAssegnazione(String valoreDb) {
        this.valoreDb = valoreDb;
    }

    public String getValoreDb() {
        return valoreDb;
    }

    public static MetodoAssegnazione fromString(String value) {
        for (MetodoAssegnazione m : MetodoAssegnazione.values()) {
            if (m.valoreDb.equalsIgnoreCase(value.trim())) {
                return m;
            }
        }
        throw new IllegalArgumentException("Valore MetodoAssegnazione non valido: " + value);
    }
}
