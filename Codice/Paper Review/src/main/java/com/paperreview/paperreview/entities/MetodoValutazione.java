package com.paperreview.paperreview.entities;

public enum MetodoValutazione {
    DUE("2"),
    TRE("3"),
    QUATTRO("4");

    private final String valoreDb;

    MetodoValutazione(String valoreDb) {
        this.valoreDb = valoreDb;
    }

    public String getValoreDb() {
        return valoreDb;
    }

    public static MetodoValutazione fromString(String value) {
        for (MetodoValutazione m : MetodoValutazione.values()) {
            if (m.valoreDb.equals(value.trim())) {
                return m;
            }
        }
        throw new IllegalArgumentException("Valore MetodoValutazione non valido: " + value);
    }
}