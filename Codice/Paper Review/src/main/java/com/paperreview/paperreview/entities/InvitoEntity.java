package com.paperreview.paperreview.entities;

import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.ConferenzaDao;
import com.paperreview.paperreview.common.dbms.dao.UtenteDao;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        try {
            Connection conn = DBMSBoundary.getConnection();
            ConferenzaDao conferenzaDao = new ConferenzaDao(conn);
            UtenteDao utenteDao = new UtenteDao(conn);

            ConferenzaEntity conferenza = conferenzaDao.getById(refConferenza);
            UtenteEntity mittente = utenteDao.getById(refMittente);

            String nomeConferenza = conferenza.getNome();
            String nomeMittente = mittente.getNome() + " " + mittente.getCognome();

            return String.format("""
                Sei stato invitato da %s a partecipare alla conferenza "%s" in qualità di %s.
                """, nomeMittente, nomeConferenza, testo);

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

    public static String generaCodice() {
        int length = 10;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int idx = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }

    public static InvitoEntity creaInvito(String email, Ruolo ruolo, int refConferenza, int refMittente, Integer refDestinatario, LocalDateTime scadenza) {
        return new InvitoEntity(
                0,                          // idInvito non ancora assegnato
                scadenza,                   // data scadenza (es. scadenzaSottomissione)
                ruolo.name(),                    // testo fisso
                StatusInvito.Inviato,       // status iniziale
                email,
                generaCodice(),             // codice generato
                refConferenza,
                refMittente,
                refDestinatario
        );
    }
}