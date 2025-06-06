package com.paperreview.paperreview.common.DAO;

import com.paperreview.paperreview.entities.PaperEntity;
import com.paperreview.paperreview.entities.TopicEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.HashSet;

public class PaperDao extends BaseDao<PaperEntity> {

    public PaperDao(Connection connection) {
        super(connection, "Paper", "id_paper");
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (titolo, contenuto, data_sottomissione, ref_utente, ref_conferenza) " +
                "VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, PaperEntity paper) throws SQLException {
        stmt.setString(1, paper.getTitolo());
        stmt.setString(2, paper.getContenuto());
        stmt.setObject(3, paper.getDataSottomissione());
        stmt.setInt(4, paper.getRefUtente());
        stmt.setInt(5, paper.getRefConferenza());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET titolo = ?, contenuto = ?, data_sottomissione = ?, ref_utente = ?, ref_conferenza = ? " +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, PaperEntity paper) throws SQLException {
        stmt.setString(1, paper.getTitolo());
        stmt.setString(2, paper.getContenuto());
        stmt.setObject(3, paper.getDataSottomissione());
        stmt.setInt(4, paper.getRefUtente());
        stmt.setInt(5, paper.getRefConferenza());
        stmt.setInt(6, paper.getId());
    }

    @Override
    protected void setGeneratedId(PaperEntity paper, int id) {
        paper.setId(id);
    }

    @Override
    protected PaperEntity mapRow(ResultSet rs) throws SQLException {
        return new PaperEntity(
                rs.getInt("id_paper"),
                rs.getString("titolo"),
                rs.getString("contenuto"),
                rs.getObject("data_sottomissione", java.time.LocalDateTime.class),
                rs.getInt("ref_utente"),
                rs.getInt("ref_conferenza")
        );
    }

    // Metodo per associare un Topic a un Paper
    public void addTopicToPaper(int paperId, int topicId) throws SQLException {
        String query = "INSERT INTO TopicPaper (ref_topic, ref_paper) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, topicId);
            stmt.setInt(2, paperId);
            stmt.executeUpdate();
        }
    }

    // Metodo per ottenere tutti i Topic associati a un Paper
    public Set<TopicEntity> getTopicsForPaper(int paperId) throws SQLException {
        String query = "SELECT t.id_topic, t.nome FROM Topic t " +
                "JOIN TopicPaper tp ON t.id_topic = tp.ref_topic WHERE tp.ref_paper = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paperId);
            ResultSet rs = stmt.executeQuery();
            Set<TopicEntity> topics = new HashSet<>();
            while (rs.next()) {
                TopicEntity topic = new TopicEntity();
                topic.setId(rs.getInt("id_topic"));
                topic.setNome(rs.getString("nome"));
                topics.add(topic);
            }
            return topics;
        }
    }
}