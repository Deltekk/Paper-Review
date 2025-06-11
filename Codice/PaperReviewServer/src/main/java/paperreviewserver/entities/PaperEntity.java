package paperreviewserver.entities;

public class PaperEntity {
    private final int id;
    private final String titolo;
    private final int refUtente;

    public PaperEntity(int id, String titolo, int refUtente) {
        this.id = id;
        this.titolo = titolo;
        this.refUtente = refUtente;
    }

    public int getId() { return id; }
    public String getTitolo() { return titolo; }
    public int getRefUtente() { return refUtente; }
}