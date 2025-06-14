package com.paperreview.paperreview.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class InvitoEntity extends BaseEntity {
    private int idInvito;
    private LocalDateTime data;
    private String testo;
    private StatusInvito status;  // Enum per lo status dell'invito
    private String email;
    private String codice;
    private int refConferenza;
    private int refMittente;
    private Integer refDestinatario;  // Può essere null, quindi è Integer e non int

    // Costruttore
    public InvitoEntity(int idInvito, LocalDateTime data, String testo, StatusInvito status,
                        String email, String codice, int refConferenza, int refMittente, Integer refDestinatario) {
        this.idInvito = idInvito;
        this.data = data;
        this.testo = testo;
        this.status = status;
        this.email = email;
        this.codice = codice;
        this.refConferenza = refConferenza;
        this.refMittente = refMittente;
        this.refDestinatario = refDestinatario;
    }

    @Override
    public int getId() {
        return idInvito;
    }

    public void setId(int idInvito) {
        this.idInvito = idInvito;
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

    public StatusInvito getStatus() {
        return status;
    }

    public void setStatus(StatusInvito status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public int getRefConferenza() {
        return refConferenza;
    }

    public void setRefConferenza(int refConferenza) {
        this.refConferenza = refConferenza;
    }

    public int getRefMittente() {
        return refMittente;
    }

    public void setRefMittente(int refMittente) {
        this.refMittente = refMittente;
    }

    public Integer getRefDestinatario() {
        return refDestinatario;
    }

    public void setRefDestinatario(Integer refDestinatario) {
        this.refDestinatario = refDestinatario;
    }

    @Override
    public String toString() {
        return "InvitoEntity{" +
                "idInvito=" + idInvito +
                ", data=" + data +
                ", testo='" + testo + '\'' +
                ", status=" + status +
                ", email='" + email + '\'' +
                ", codice='" + codice + '\'' +
                ", refConferenza=" + refConferenza +
                ", refMittente=" + refMittente +
                ", refDestinatario=" + refDestinatario +
                '}';
    }

    // Sovrascrivi il metodo equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InvitoEntity that = (InvitoEntity) obj;
        return idInvito == that.idInvito && refMittente == that.refMittente && refConferenza == that.refConferenza && Objects.equals(codice, that.codice);
    }

    // Sovrascrivi il metodo hashCode
    @Override
    public int hashCode() {
        return Objects.hash(idInvito, refMittente, refConferenza, codice);
    }
}