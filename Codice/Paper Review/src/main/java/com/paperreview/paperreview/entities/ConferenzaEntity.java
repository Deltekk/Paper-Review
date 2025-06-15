package com.paperreview.paperreview.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class ConferenzaEntity extends BaseEntity {
    private int id_conferenza;
    private String nome;
    private String descrizione;
    private LocalDateTime dataConferenza;
    private String location;
    private MetodoAssegnazione metodoAssegnazione;
    private MetodoValutazione metodoValutazione;
    private int rateAccettazione;
    private int paperPrevisti;
    private int giorniPreavviso;
    private LocalDateTime scadenzaSottomissione;
    private LocalDateTime scadenzaRevisione;
    private LocalDateTime scadenzaSottomissione2;
    private LocalDateTime scadenzaEditing;
    private LocalDateTime scadenzaSottomissione3;
    private LocalDateTime scadenzaImpaginazione;
    private Set<UtenteEntity> chairs;

    public ConferenzaEntity(int id_conferenza, String nome, String descrizione,
                            LocalDateTime dataConferenza, String location, MetodoAssegnazione metodoAssegnazione,
                            MetodoValutazione metodoValutazione,
                            int rateAccettazione,
                            int paperPrevisti,
                            int giorniPreavviso,
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
        this.rateAccettazione = rateAccettazione;
        this.paperPrevisti = paperPrevisti;
        this.giorniPreavviso = giorniPreavviso;
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

    public MetodoAssegnazione getMetodoAssegnazione() {
        return metodoAssegnazione;
    }

    public void setMetodoAssegnazione(MetodoAssegnazione metodoAssegnazione) {
        this.metodoAssegnazione = metodoAssegnazione;
    }

    public MetodoValutazione getMetodoValutazione() {
        return metodoValutazione;
    }

    public void setMetodoValutazione(MetodoValutazione metodoValutazione) {
        this.metodoValutazione = metodoValutazione;
    }

    public int getPaperPrevisti() {
        return paperPrevisti;
    }

    public void setPaperPrevisti(int paperPrevisti) {
        this.paperPrevisti = paperPrevisti;
    }

    public int getRateAccettazione() {
        return rateAccettazione;
    }

    public void setRateAccettazione(int rateAccettazione) {
        this.rateAccettazione = rateAccettazione;
    }

    public int getGiorniPreavviso() {
        return giorniPreavviso;
    }

    public void setGiorniPreavviso(int giorniPreavviso) {
        this.giorniPreavviso = giorniPreavviso;
    }

    public Set<UtenteEntity> getChairs() {
        return chairs;
    }

    public void setChairs(Set<UtenteEntity> chairs) {
        this.chairs = chairs;
    }

    @Override
    public String toString() {
        return "ConferenzaEntity{" +
                "id_conferenza=" + id_conferenza +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", dataConferenza=" + dataConferenza +
                ", location='" + location + '\'' +
                ", metodoAssegnazione=" + metodoAssegnazione +
                ", metodoValutazione=" + metodoValutazione +
                ", rateAccettazione=" + rateAccettazione +
                ", paperPrevisti=" + paperPrevisti +
                ", giorniPreavviso=" + giorniPreavviso +
                ", scadenzaSottomissione=" + scadenzaSottomissione +
                ", scadenzaRevisione=" + scadenzaRevisione +
                ", scadenzaSottomissione2=" + scadenzaSottomissione2 +
                ", scadenzaEditing=" + scadenzaEditing +
                ", scadenzaSottomissione3=" + scadenzaSottomissione3 +
                ", scadenzaImpaginazione=" + scadenzaImpaginazione +
                ", chairs=" + chairs +
                '}';
    }


    // Sovrascrivi il metodo equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ConferenzaEntity that = (ConferenzaEntity) obj;
        return id_conferenza == that.id_conferenza &&
                Objects.equals(nome, that.nome) &&
                Objects.equals(dataConferenza, that.dataConferenza);

    }

    // Sovrascrivi il metodo hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id_conferenza, nome);
    }
}