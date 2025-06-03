package com.paperreview.paperreview.entities;

public class TopicEntity extends BaseEntity  {
    int id_topic;
    String nome;

    @Override
    public int getId() {
        return id_topic;
    }

    public void setId(int id_topic) {
        this.id_topic = id_topic;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
