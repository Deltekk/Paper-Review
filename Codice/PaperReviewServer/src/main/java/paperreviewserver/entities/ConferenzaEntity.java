package paperreviewserver.entities;

import java.time.LocalDateTime;

public class ConferenzaEntity {
    private int id;
    private String nome;
    private Integer giorniPreavviso;
    private LocalDateTime scadenzaSottomissione;
    private LocalDateTime scadenzaRevisione;
    private LocalDateTime scadenzaSottomissione2;
    private LocalDateTime scadenzaEditing;
    private LocalDateTime scadenzaSottomissione3;
    private LocalDateTime scadenzaImpaginazione;

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public Integer getGiorniPreavviso() { return giorniPreavviso; }
    public LocalDateTime getScadenzaSottomissione() { return scadenzaSottomissione; }
    public LocalDateTime getScadenzaRevisione() { return scadenzaRevisione; }
    public LocalDateTime getScadenzaSottomissione2() { return scadenzaSottomissione2; }
    public LocalDateTime getScadenzaEditing() { return scadenzaEditing; }
    public LocalDateTime getScadenzaSottomissione3() { return scadenzaSottomissione3; }
    public LocalDateTime getScadenzaImpaginazione() { return scadenzaImpaginazione; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setGiorniPreavviso(Integer giorniPreavviso) { this.giorniPreavviso = giorniPreavviso; }
    public void setScadenzaSottomissione(LocalDateTime scadenzaSottomissione) { this.scadenzaSottomissione = scadenzaSottomissione; }
    public void setScadenzaRevisione(LocalDateTime scadenzaRevisione) { this.scadenzaRevisione = scadenzaRevisione; }
    public void setScadenzaSottomissione2(LocalDateTime scadenzaSottomissione2) { this.scadenzaSottomissione2 = scadenzaSottomissione2; }
    public void setScadenzaEditing(LocalDateTime scadenzaEditing) { this.scadenzaEditing = scadenzaEditing; }
    public void setScadenzaSottomissione3(LocalDateTime scadenzaSottomissione3) { this.scadenzaSottomissione3 = scadenzaSottomissione3; }
    public void setScadenzaImpaginazione(LocalDateTime scadenzaImpaginazione) { this.scadenzaImpaginazione = scadenzaImpaginazione; }
}