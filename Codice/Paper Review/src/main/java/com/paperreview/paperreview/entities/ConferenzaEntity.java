package com.paperreview.paperreview.entities;

import java.time.LocalDateTime;

public class ConferenzaEntity extends BaseEntity {
    private int id_conferenza;
    private String nome;
    private String descrizione;
    private String location;
    private String metodoAssegnazione;
    private String metodoValutazione;
    private int paperPrevisti;
    private LocalDateTime dataConferenza;
    private LocalDateTime scadenzaSottomissione;
    private LocalDateTime scadenzaRevisione;
    private LocalDateTime scadenzaSottomissione2;
    private LocalDateTime scadenzaEditing;
    private LocalDateTime scadenzaSottomissione3;
    private LocalDateTime scadenzaImpaginazione;

    public ConferenzaEntity(int id_conferenza, String nome, String descrizione,
                            LocalDateTime dataConferenza, String location, String metodoAssegnazione,
                            String metodoValutazione, int paperPrevisti,
                            LocalDateTime scadenzaSottomissione,
                            LocalDateTime scadenzaRevisione,
                            LocalDateTime scadenzaSottomissione2,
                            LocalDateTime scadenzaEditing,
                            LocalDateTime scadenzaSottomissione3,
                            LocalDateTime scadenzaImpaginazione) {
        this.id_conferenza = id_conferenza;
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataConferenza = dataConferenza;
        this.location = location;
        this.metodoAssegnazione = metodoAssegnazione;
        this.metodoValutazione = metodoValutazione;
        this.paperPrevisti = paperPrevisti;
        this.scadenzaSottomissione = scadenzaSottomissione;
        this.scadenzaRevisione = scadenzaRevisione;
        this.scadenzaSottomissione2 = scadenzaSottomissione2;
        this.scadenzaEditing = scadenzaEditing;
        this.scadenzaSottomissione3 = scadenzaSottomissione3;
        this.scadenzaImpaginazione = scadenzaImpaginazione;
    }

    @Override
    public int getId() {
        return id_conferenza;
    }

    public void setId(int id_conferenza) {
        this.id_conferenza = id_conferenza;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDateTime getDataConferenza() {
        return dataConferenza;
    }

    public void setDataConferenza(LocalDateTime dataConferenza) {
        this.dataConferenza = dataConferenza;
    }

    public LocalDateTime getScadenzaSottomissione() {
        return scadenzaSottomissione;
    }

    public void setScadenzaSottomissione(LocalDateTime scadenzaSottomissione) {
        this.scadenzaSottomissione = scadenzaSottomissione;
    }

    public LocalDateTime getScadenzaRevisione() {
        return scadenzaRevisione;
    }

    public void setScadenzaRevisione(LocalDateTime scadenzaRevisione) {
        this.scadenzaRevisione = scadenzaRevisione;
    }

    public LocalDateTime getScadenzaSottomissione2() {
        return scadenzaSottomissione2;
    }

    public void setScadenzaSottomissione2(LocalDateTime scadenzaSottomissione2) {
        this.scadenzaSottomissione2 = scadenzaSottomissione2;
    }

    public LocalDateTime getScadenzaEditing() {
        return scadenzaEditing;
    }

    public void setScadenzaEditing(LocalDateTime scadenzaEditing) {
        this.scadenzaEditing = scadenzaEditing;
    }

    public LocalDateTime getScadenzaSottomissione3() {
        return scadenzaSottomissione3;
    }

    public void setScadenzaSottomissione3(LocalDateTime scadenzaSottomissione3) {
        this.scadenzaSottomissione3 = scadenzaSottomissione3;
    }

    public LocalDateTime getScadenzaImpaginazione() {
        return scadenzaImpaginazione;
    }

    public void setScadenzaImpaginazione(LocalDateTime scadenzaImpaginazione) {
        this.scadenzaImpaginazione = scadenzaImpaginazione;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMetodoAssegnazione() {
        return metodoAssegnazione;
    }

    public void setMetodoAssegnazione(String metodoAssegnazione) {
        this.metodoAssegnazione = metodoAssegnazione;
    }

    public String getMetodoValutazione() {
        return metodoValutazione;
    }

    public void setMetodoValutazione(String metodoValutazione) {
        this.metodoValutazione = metodoValutazione;
    }

    public int getPaperPrevisti() {
        return paperPrevisti;
    }

    public void setPaperPrevisti(int paperPrevisti) {
        this.paperPrevisti = paperPrevisti;
    }

    @Override
    public String toString() {
        return "ConferenzaEntity{" +
                "id_conferenza=" + id_conferenza +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", data_conferenza=" + dataConferenza +
                ", location='" + location + '\'' +
                ", metodo_assegnazione='" + metodoAssegnazione + '\'' +
                ", metodo_valutazione='" + metodoValutazione + '\'' +
                ", paper_previsti=" + paperPrevisti +
                ", scadenza_sottomissione=" + scadenzaSottomissione +
                ", scadenza_revisione=" + scadenzaRevisione +
                ", scadenza_sottomissione_2=" + scadenzaSottomissione2 +
                ", scadenza_editing=" + scadenzaEditing +
                ", scadenza_sottomissione_3=" + scadenzaSottomissione3 +
                ", scadenza_impaginazione=" + scadenzaImpaginazione +
                '}';
    }

}
