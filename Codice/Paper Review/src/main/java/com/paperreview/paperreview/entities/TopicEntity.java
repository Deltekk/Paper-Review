package com.paperreview.paperreview.entities;

import java.util.Objects;

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

    // Sovrascrivi il metodo equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TopicEntity that = (TopicEntity) obj;
        return id_topic == that.id_topic && Objects.equals(nome, that.nome);
    }

    // Sovrascrivi il metodo hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id_topic, nome);
    }

}
