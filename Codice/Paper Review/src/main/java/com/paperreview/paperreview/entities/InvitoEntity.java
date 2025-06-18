package com.paperreview.paperreview.entities;

import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.ConferenzaDao;
import com.paperreview.paperreview.common.dbms.dao.UtenteDao;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Objects;

public class InvitoEntity extends BaseEntity {
    private int idInvito;
    private LocalDateTime data;
    private String testo;
    private StatusInvito status;
    private String email;
    private String codice;
    private int refConferenza;
    private int refMittente;
    private Integer refDestinatario;
    private Integer refPaper;

    // Costruttore completo con ID
    public InvitoEntity(int idInvito, LocalDateTime data, String testo, StatusInvito status,
                        String email, String codice, int refConferenza, int refMittente,
                        Integer refDestinatario, Integer refPaper) {
        this.idInvito = idInvito;
        this.data = data;
        this.testo = testo;
        this.status = status;
        this.email = email;
        this.codice = codice;
        this.refConferenza = refConferenza;
        this.refMittente = refMittente;
        this.refDestinatario = refDestinatario;
        this.refPaper = refPaper;
    }

    // Costruttore senza ID
    public InvitoEntity(LocalDateTime data, String testo, StatusInvito status,
                        String email, String codice, int refConferenza, int refMittente,
                        Integer refDestinatario, Integer refPaper) {
        this(0, data, testo, status, email, codice, refConferenza, refMittente, refDestinatario, refPaper);
    }

    // Costruttore base senza refPaper
    public InvitoEntity(int idInvito, LocalDateTime data, String testo, StatusInvito status,
                        String email, String codice, int refConferenza, int refMittente,
                        Integer refDestinatario) {
        this(idInvito, data, testo, status, email, codice, refConferenza, refMittente, refDestinatario, null);
    }

    // Costruttore statico per inviti standard
    public static InvitoEntity creaInvito(String email, Ruolo ruolo, int refConferenza, int refMittente,
                                          Integer refDestinatario, LocalDateTime scadenza) {
        return new InvitoEntity(
                scadenza,
                ruolo.name(),
                StatusInvito.Inviato,
                email,
                generaCodice(),
                refConferenza,
                refMittente,
                refDestinatario,
                null
        );
    }

    // Costruttore statico per inviti con paper
    public static InvitoEntity creaInvitoConPaper(String email, Ruolo ruolo, int refConferenza, int refMittente,
                                                  Integer refDestinatario, Integer refPaper, LocalDateTime scadenza) {
        return new InvitoEntity(
                scadenza,
                ruolo.name(),
                StatusInvito.Inviato,
                email,
                generaCodice(),
                refConferenza,
                refMittente,
                refDestinatario,
                refPaper
        );
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

    public String getRuolo() {
        return testo;
    }

    public String getTesto() {
        try {
            Connection conn = DBMSBoundary.getConnection();
            ConferenzaDao conferenzaDao = new ConferenzaDao(conn);
            UtenteDao utenteDao = new UtenteDao(conn);

            ConferenzaEntity conferenza = conferenzaDao.getById(refConferenza);
            UtenteEntity mittente = utenteDao.getById(refMittente);

            return String.format("""
                Sei stato invitato da %s %s a partecipare alla conferenza "%s" in qualità di %s.
                """, mittente.getNome(), mittente.getCognome(), conferenza.getNome(), testo);

        } catch (Exception e) {
            return "Hai ricevuto un nuovo invito a partecipare a una conferenza.";
        }
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

    public Integer getRefPaper() {
        return refPaper;
    }

    public void setRefPaper(Integer refPaper) {
        this.refPaper = refPaper;
    }

    public static String generaCodice() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) sb.append(chars.charAt((int)(Math.random() * chars.length())));
        sb.append('-');
        for (int i = 0; i < 3; i++) sb.append(chars.charAt((int)(Math.random() * chars.length())));
        return sb.toString();
    }

    @Override
    public String toString() {
        return "InvitoEntity{" +
                "id=" + idInvito +
                ", email='" + email + '\'' +
                ", ruolo='" + testo + '\'' +
                ", codice='" + codice + '\'' +
                ", status=" + status +
                ", conferenza=" + refConferenza +
                ", mittente=" + refMittente +
                ", destinatario=" + refDestinatario +
                ", paper=" + refPaper +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvitoEntity that)) return false;
        return idInvito == that.idInvito && refMittente == that.refMittente &&
                refConferenza == that.refConferenza && Objects.equals(codice, that.codice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInvito, refConferenza, refMittente, codice);
    }
}