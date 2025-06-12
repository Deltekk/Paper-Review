package com.paperreview.paperreview.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class ProceedingEntity extends BaseEntity {
    private int idProceeding;
    private String titolo;
    private LocalDateTime dataSottomissione;
    private int refUtente;
    private int refConferenza;

    // Costruttore
    public ProceedingEntity(int idProceeding, String titolo, LocalDateTime dataSottomissione, int refUtente, int refConferenza) {
        this.idProceeding = idProceeding;
        this.titolo = titolo;
        this.dataSottomissione = dataSottomissione;
        this.refUtente = refUtente;
        this.refConferenza = refConferenza;
    }

    @Override
    public int getId() {
        return idProceeding;
    }

    public void setId(int idProceeding) {
        this.idProceeding = idProceeding;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
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
        return "ProceedingEntity{" +
                "idProceeding=" + idProceeding +
                ", titolo='" + titolo + '\'' +
                ", dataSottomissione=" + dataSottomissione +
                ", refUtente=" + refUtente +
                ", refConferenza=" + refConferenza +
                '}';
    }

    // Sovrascrivi il metodo equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProceedingEntity that = (ProceedingEntity) obj;
        return idProceeding == that.idProceeding && refUtente == that.refUtente && refConferenza == that.refConferenza && Objects.equals(titolo, that.titolo);
    }

    // Sovrascrivi il metodo hashCode
    @Override
    public int hashCode() {
        return Objects.hash(idProceeding, titolo, refUtente, refConferenza);
    }
}