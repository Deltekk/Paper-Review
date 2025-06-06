package com.paperreview.paperreview.entities;

public enum InvitoStatusEnum {
    PENDING("Inviato"),    // Invito in attesa di risposta
    ACCEPTED("Accettato"),  // Invito accettato
    REJECTED("Rifiutato");  // Invito rifiutato

    private final String status;

    // Costruttore per associare una stringa a ciascun stato
    InvitoStatusEnum(String status) {
        this.status = status;
    }

    // Metodo per ottenere la stringa associata a ciascun stato
    public String getStatus() {
        return status;
    }

    // Metodo per ottenere un valore ENUM dal nome (utile per la conversione da stringa)
    public static InvitoStatusEnum fromString(String status) {
        for (InvitoStatusEnum s : InvitoStatusEnum.values()) {
            if (s.status.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
