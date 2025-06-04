package com.paperreview.paperreview.entities;

import java.util.Set;

public class UtenteEntity extends BaseEntity {
    private int id_utente;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private Set<TopicEntity> topics;

    public UtenteEntity(int id_utente, String nome, String cognome, String email, String password) {
        this.id_utente = id_utente;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }

    @Override
    public int getId() { return id_utente; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public Set<TopicEntity> getTopics() {
        return topics;
    }

    public void setTopics(Set<TopicEntity> topics) {
        this.topics = topics;
    }

    public String getNomeUtente() { return nome + " " + cognome; }

    public void setId(int id_utente) { this.id_utente = id_utente; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return String.format("Utente: %d, %s, %s, %s, %s", getId(), getNome(), getCognome(), getEmail(), getPassword());
    }

}
