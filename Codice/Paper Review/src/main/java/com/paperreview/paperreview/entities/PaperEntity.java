package com.paperreview.paperreview.entities;

import java.time.LocalDateTime;

public class PaperEntity extends BaseEntity {
    private int idPaper;
    private String titolo;
    private String contenuto;
    private LocalDateTime dataSottomissione;
    private int refUtente;
    private int refConferenza;

    // Costruttore
    public PaperEntity(int idPaper, String titolo, String contenuto, LocalDateTime dataSottomissione, int refUtente, int refConferenza) {
        this.idPaper = idPaper;
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.dataSottomissione = dataSottomissione;
        this.refUtente = refUtente;
        this.refConferenza = refConferenza;
    }

    @Override
    public int getId() {
        return idPaper;
    }

    public void setId(int idPaper) {
        this.idPaper = idPaper;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
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

    public int getRefConferenza() {
        return refConferenza;
    }

    public void setRefConferenza(int refConferenza) {
        this.refConferenza = refConferenza;
    }

    @Override
    public String toString() {
        return "PaperEntity{" +
                "idPaper=" + idPaper +
                ", titolo='" + titolo + '\'' +
                ", contenuto='" + contenuto + '\'' +
                ", dataSottomissione=" + dataSottomissione +
                ", refUtente=" + refUtente +
                ", refConferenza=" + refConferenza +
                '}';
    }
}
