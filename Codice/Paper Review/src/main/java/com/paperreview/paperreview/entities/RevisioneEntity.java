package com.paperreview.paperreview.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class RevisioneEntity extends BaseEntity {
    private int idRevisione;

    private String testo;
    private Integer valutazione;
    private LocalDateTime dataSottomissione;
    private String puntiForza;
    private String puntiDebolezza;
    private String commentoChair;

    private int refUtente;
    private int refPaper;
    private Integer refSottorevisore;

    // Costruttore completo con ID
    public RevisioneEntity(int idRevisione, String testo, Integer valutazione, LocalDateTime dataSottomissione,
                           String puntiForza, String puntiDebolezza, String commentoChair,
                           int refUtente, int refPaper, Integer refSottorevisore) {
        this.idRevisione = idRevisione;
        this.testo = testo;
        this.valutazione = valutazione;
        this.dataSottomissione = dataSottomissione;
        this.puntiForza = puntiForza;
        this.puntiDebolezza = puntiDebolezza;
        this.commentoChair = commentoChair;
        this.refUtente = refUtente;
        this.refPaper = refPaper;
        this.refSottorevisore = refSottorevisore;
    }

    // Costruttore senza ID (es. per insert)
    public RevisioneEntity(String testo, Integer valutazione, LocalDateTime dataSottomissione,
                           String puntiForza, String puntiDebolezza, String commentoChair,
                           int refUtente, int refPaper, Integer refSottorevisore) {
        this(0, testo, valutazione, dataSottomissione, puntiForza, puntiDebolezza, commentoChair,
                refUtente, refPaper, refSottorevisore);
    }

    // Costruttore solo con campi obbligatori
    public RevisioneEntity(int refUtente, int refPaper) {
        this.refUtente = refUtente;
        this.refPaper = refPaper;
    }

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

    public Integer getValutazione() {
        return valutazione;
    }

    public void setValutazione(Integer valutazione) {
        this.valutazione = valutazione;
    }

    public LocalDateTime getDataSottomissione() {
        return dataSottomissione;
    }

    public void setDataSottomissione(LocalDateTime dataSottomissione) {
        this.dataSottomissione = dataSottomissione;
    }

    public String getPuntiForza() {
        return puntiForza;
    }

    public void setPuntiForza(String puntiForza) {
        this.puntiForza = puntiForza;
    }

    public String getPuntiDebolezza() {
        return puntiDebolezza;
    }

    public void setPuntiDebolezza(String puntiDebolezza) {
        this.puntiDebolezza = puntiDebolezza;
    }

    public String getCommentoChair() {
        return commentoChair;
    }

    public void setCommentoChair(String commentoChair) {
        this.commentoChair = commentoChair;
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

    public Integer getRefSottorevisore() {
        return refSottorevisore;
    }

    public void setRefSottorevisore(Integer refSottorevisore) {
        this.refSottorevisore = refSottorevisore;
    }

    @Override
    public String toString() {
        return "RevisioneEntity{" +
                "idRevisione=" + idRevisione +
                ", testo='" + testo + '\'' +
                ", valutazione=" + valutazione +
                ", dataSottomissione=" + dataSottomissione +
                ", puntiForza='" + puntiForza + '\'' +
                ", puntiDebolezza='" + puntiDebolezza + '\'' +
                ", commentoChair='" + commentoChair + '\'' +
                ", refUtente=" + refUtente +
                ", refPaper=" + refPaper +
                ", refSottorevisore=" + refSottorevisore +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RevisioneEntity that)) return false;
        return idRevisione == that.idRevisione &&
                refUtente == that.refUtente &&
                refPaper == that.refPaper;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRevisione, refUtente, refPaper);
    }
}