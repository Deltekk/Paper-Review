package paperreviewserver.entities;

public class UtenteEntity {
    private final int id;
    private final String nome;
    private final String cognome;
    private final String email;

    public UtenteEntity(int id, String nome, String cognome, String email) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getEmail() { return email; }
}