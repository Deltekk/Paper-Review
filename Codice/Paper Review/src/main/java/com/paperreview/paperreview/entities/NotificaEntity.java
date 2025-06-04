package com.paperreview.paperreview.entities;

import java.time.LocalDateTime;

public class NotificaEntity extends BaseEntity {
    private int idNotifica;
    private LocalDateTime data;
    private String testo;
    private boolean isLetta;
    private int refUtente;
    private int refConferenza;

    // Costruttore
    public NotificaEntity(int idNotifica, LocalDateTime data, String testo, boolean isLetta, int refUtente, int refConferenza) {
        this.idNotifica = idNotifica;
        this.data = data;
        this.testo = testo;
        this.isLetta = isLetta;
        this.refUtente = refUtente;
        this.refConferenza = refConferenza;
    }

    @Override
    public int getId() {
        return idNotifica;
    }

    public void setId(int idNotifica) {
        this.idNotifica = idNotifica;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public boolean isLetta() {
        return isLetta;
    }

    public void setLetta(boolean isLetta) {
        this.isLetta = isLetta;
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
        return "NotificaEntity{" +
                "idNotifica=" + idNotifica +
                ", data=" + data +
                ", testo='" + testo + '\'' +
                ", isLetta=" + isLetta +
                ", refUtente=" + refUtente +
                ", refConferenza=" + refConferenza +
                '}';
    }
}
