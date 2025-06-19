package paperreviewserver.entities;

public class TopicEntity {
    private final int id;
    private final String nome;

    public TopicEntity(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}