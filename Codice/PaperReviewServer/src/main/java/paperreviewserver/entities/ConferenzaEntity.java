package paperreviewserver.entities;

import java.time.LocalDateTime;

public class ConferenzaEntity {
    private int id;
    private String nome;
    private LocalDateTime scadenzaSottomissione;
    private LocalDateTime scadenzaRevisione;
    private LocalDateTime scadenzaSottomissione2;
    private LocalDateTime scadenzaEditing;
    private LocalDateTime scadenzaSottomissione3;
    private LocalDateTime scadenzaImpaginazione;
    // altri campi se ti servono

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDateTime getScadenzaSottomissione() { return scadenzaSottomissione; }
    public void setScadenzaSottomissione(LocalDateTime v) { this.scadenzaSottomissione = v; }

    public LocalDateTime getScadenzaRevisione() { return scadenzaRevisione; }
    public void setScadenzaRevisione(LocalDateTime v) { this.scadenzaRevisione = v; }

    public LocalDateTime getScadenzaSottomissione2() { return scadenzaSottomissione2; }
    public void setScadenzaSottomissione2(LocalDateTime v) { this.scadenzaSottomissione2 = v; }

    public LocalDateTime getScadenzaEditing() { return scadenzaEditing; }
    public void setScadenzaEditing(LocalDateTime v) { this.scadenzaEditing = v; }

    public LocalDateTime getScadenzaSottomissione3() { return scadenzaSottomissione3; }
    public void setScadenzaSottomissione3(LocalDateTime v) { this.scadenzaSottomissione3 = v; }

    public LocalDateTime getScadenzaImpaginazione() { return scadenzaImpaginazione; }
    public void setScadenzaImpaginazione(LocalDateTime v) { this.scadenzaImpaginazione = v; }
}