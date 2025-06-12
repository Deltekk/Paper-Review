package com.paperreview.paperreview.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class PaperEntity extends BaseEntity {
    private int id;
    private String titolo;
    private String abstractPaper;    // può essere null
    private byte[] file;             // BLOB per il file vero e proprio
    private LocalDateTime dataSottomissione;
    private int refUtente;
    private int refConferenza;
    private Set<TopicEntity> topics; // opzionale, non mappato direttamente dal DAO

    // Costruttore completo (senza topics)
    public PaperEntity(int id, String titolo, String abstractPaper, byte[] file,
                       LocalDateTime dataSottomissione, int refUtente, int refConferenza) {
        this.id = id;
        this.titolo = titolo;
        this.abstractPaper = abstractPaper;
        this.file = file;
        this.dataSottomissione = dataSottomissione;
        this.refUtente = refUtente;
        this.refConferenza = refConferenza;
    }

    @Override
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getAbstractPaper() { return abstractPaper; }
    public void setAbstractPaper(String abstractPaper) { this.abstractPaper = abstractPaper; }

    public byte[] getFile() { return file; }
    public void setFile(byte[] file) { this.file = file; }

    public LocalDateTime getDataSottomissione() { return dataSottomissione; }
    public void setDataSottomissione(LocalDateTime dataSottomissione) { this.dataSottomissione = dataSottomissione; }

    public int getRefUtente() { return refUtente; }
    public void setRefUtente(int refUtente) { this.refUtente = refUtente; }

    public int getRefConferenza() { return refConferenza; }
    public void setRefConferenza(int refConferenza) { this.refConferenza = refConferenza; }

    public Set<TopicEntity> getTopics() { return topics; }
    public void setTopics(Set<TopicEntity> topics) { this.topics = topics; }

    @Override
    public String toString() {
        return "PaperEntity{" +
                "id=" + id +
                ", titolo='" + titolo + '\'' +
                ", abstractPaper='" + abstractPaper + '\'' +
                ", file=" + (file != null ? file.length + " bytes" : "null") +
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
        PaperEntity that = (PaperEntity) obj;
        return id == that.id && refUtente == that.refUtente && Objects.equals(titolo, that.titolo);
    }

    // Sovrascrivi il metodo hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, titolo, refUtente);
    }
}