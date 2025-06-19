package paperreviewserver.entities;

import java.time.LocalDateTime;

public class PaperEntity {
    private int id;
    private String titolo;
    private int refUtente;
    private LocalDateTime dataSottomissione;

    public PaperEntity(int id, String titolo, int refUtente) {
        this.id = id;
        this.titolo = titolo;
        this.refUtente = refUtente;
    }
    public PaperEntity() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public int getRefUtente() { return refUtente; }
    public void setRefUtente(int refUtente) { this.refUtente = refUtente; }

    public LocalDateTime getDataSottomissione() { return dataSottomissione; }
    public void setDataSottomissione(LocalDateTime dataSottomissione) { this.dataSottomissione = dataSottomissione; }
}