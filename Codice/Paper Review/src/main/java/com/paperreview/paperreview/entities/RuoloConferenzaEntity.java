package com.paperreview.paperreview.entities;

public class RuoloConferenzaEntity extends BaseEntity {
    private int id;
    private Ruolo ruolo;        // Enum Ruolo: Chair, Revisore, Sottorevisore, Autore, Editor
    private int refUtente;      // id dell'utente
    private int refConferenza;  // id della conferenza

    // Costruttore completo
    public RuoloConferenzaEntity(int id, Ruolo ruolo, int refUtente, int refConferenza) {
        this.id = id;
        this.ruolo = ruolo;
        this.refUtente = refUtente;
        this.refConferenza = refConferenza;
    }

    // Costruttore senza id (utile per inserimenti)
    public RuoloConferenzaEntity(Ruolo ruolo, int refUtente, int refConferenza) {
        this.ruolo = ruolo;
        this.refUtente = refUtente;
        this.refConferenza = refConferenza;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    public int getRefUtente() {
        return refUtente;
    }

    public void setRefUtente(int refUtente) {
        this.refUtente = refUtente;
    }

    public int getRefConferenza() {
        return refConferenza;
    }

    public void setRefConferenza(int refConferenza) {
        this.refConferenza = refConferenza;
    }

    @Override
    public String toString() {
        return "RuoloConferenzaEntity{" +
                "id=" + id +
                ", ruolo=" + ruolo +
                ", refUtente=" + refUtente +
                ", refConferenza=" + refConferenza +
                '}';
    }
}