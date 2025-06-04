package com.paperreview.paperreview.entities;

import java.time.LocalDateTime;

public class RevisioneEntity extends BaseEntity {
    private int idRevisione;
    private String testo;
    private int valutazione;
    private LocalDateTime dataSottomissione;
    private int refUtente;
    private int refPaper;

    // Costruttore
    public RevisioneEntity(int idRevisione, String testo, int valutazione, LocalDateTime dataSottomissione, int refUtente, int refPaper) {
        this.idRevisione = idRevisione;
        this.testo = testo;
        this.valutazione = valutazione;
        this.dataSottomissione = dataSottomissione;
        this.refUtente = refUtente;
        this.refPaper = refPaper;
    }

    @Override
    public int getId() {
        return idRevisione;
    }

    public void setId(int idRevisione) {
        this.idRevisione = idRevisione;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public int getValutazione() {
        return valutazione;
    }

    public void setValutazione(int valutazione) {
        this.valutazione = valutazione;
    }

    public LocalDateTime getDataSottomissione() {
        return dataSottomissione;
    }

    public void setDataSottomissione(LocalDateTime dataSottomissione) {
        this.dataSottomissione = dataSottomissione;
    }

    public int getRefUtente() {
        return refUtente;
    }

    public void setRefUtente(int refUtente) {
        this.refUtente = refUtente;
    }

    public int getRefPaper() {
        return refPaper;
    }

    public void setRefPaper(int refPaper) {
        this.refPaper = refPaper;
    }

    @Override
    public String toString() {
        return "RevisioneEntity{" +
                "idRevisione=" + idRevisione +
                ", testo='" + testo + '\'' +
                ", valutazione=" + valutazione +
                ", dataSottomissione=" + dataSottomissione +
                ", refUtente=" + refUtente +
                ", refPaper=" + refPaper +
                '}';
    }
}
