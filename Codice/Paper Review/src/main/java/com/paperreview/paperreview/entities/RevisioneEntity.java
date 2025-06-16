package com.paperreview.paperreview.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class RevisioneEntity extends BaseEntity {
    private int idRevisione;
    private String testo;
    private int valutazione;
    private LocalDateTime dataSottomissione;
    private int refUtente;
    private int refPaper;
    private String commentoChair; // <- nuovo campo

    // Costruttore completo
    public RevisioneEntity(int idRevisione, String testo, int valutazione, LocalDateTime dataSottomissione, int refUtente, int refPaper, String commentoChair) {
        this.idRevisione = idRevisione;
        this.testo = testo;
        this.valutazione = valutazione;
        this.dataSottomissione = dataSottomissione;
        this.refUtente = refUtente;
        this.refPaper = refPaper;
        this.commentoChair = commentoChair;
    }

    // Costruttore base senza ID (es. per insert preliminare)
    public RevisioneEntity() {}

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

    public String getCommentoChair() {
        return commentoChair;
    }

    public void setCommentoChair(String commentoChair) {
        this.commentoChair = commentoChair;
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
                ", commentoChair='" + commentoChair + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RevisioneEntity that = (RevisioneEntity) obj;
        return idRevisione == that.idRevisione &&
                refUtente == that.refUtente &&
                refPaper == that.refPaper;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRevisione, refUtente, refPaper);
    }
}