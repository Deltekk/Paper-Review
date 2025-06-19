package com.paperreview.paperreview.common.dbms.dao;

import com.paperreview.paperreview.entities.TopicEntity;
import com.paperreview.paperreview.entities.PaperEntity;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class TopicPaperDao {

    private final Connection connection;

    public TopicPaperDao(Connection connection) {
        this.connection = connection;
    }

    // Associa un topic a un paper
    public void addTopicToPaper(int topicId, int paperId) throws SQLException {
        String query = "INSERT IGNORE INTO TopicPaper (ref_topic, ref_paper) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, topicId);
            stmt.setInt(2, paperId);
            stmt.executeUpdate();
        }
    }

    public void removeByPaperId(int idPaper) throws SQLException {
        String query = "DELETE FROM TopicPaper WHERE ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPaper);
            stmt.executeUpdate();
        }
    }

    // Rimuove l'associazione tra un topic e un paper
    public void removeTopicFromPaper(int topicId, int paperId) throws SQLException {
        String query = "DELETE FROM TopicPaper WHERE ref_topic = ? AND ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, topicId);
            stmt.setInt(2, paperId);
            stmt.executeUpdate();
        }
    }

    // Rimuove tutte le associazioni di un topic
    public void removeAllPapersFromTopic(int topicId) throws SQLException {
        String query = "DELETE FROM TopicPaper WHERE ref_topic = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, topicId);
            stmt.executeUpdate();
        }
    }

    // Rimuove tutte le associazioni di un paper
    public void removeAllTopicsFromPaper(int paperId) throws SQLException {
        String query = "DELETE FROM TopicPaper WHERE ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paperId);
            stmt.executeUpdate();
        }
    }

    // Controlla se un'associazione esiste
    public boolean exists(int topicId, int paperId) throws SQLException {
        String query = "SELECT 1 FROM TopicPaper WHERE ref_topic = ? AND ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, topicId);
            stmt.setInt(2, paperId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Restituisce tutti i Topic associati a un Paper
    public Set<TopicEntity> getTopicsForPaper(int paperId) throws SQLException {
        String query = "SELECT t.id_topic, t.nome FROM Topic t " +
                "JOIN TopicPaper tp ON t.id_topic = tp.ref_topic WHERE tp.ref_paper = ?";
        Set<TopicEntity> topics = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paperId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TopicEntity topic = new TopicEntity();
                    topic.setId(rs.getInt("id_topic"));
                    topic.setNome(rs.getString("nome"));
                    topics.add(topic);
                }
            }
        }
        return topics;
    }

    // Restituisce tutti i Paper associati a un Topic
    public Set<Integer> getPaperIdsForTopic(int topicId) throws SQLException {
        String query = "SELECT ref_paper FROM TopicPaper WHERE ref_topic = ?";
        Set<Integer> paperIds = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, topicId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    paperIds.add(rs.getInt("ref_paper"));
                }
            }
        }
        return paperIds;
    }

    // Restituisce tutti i Topic associati a un Paper (solo id)
    public Set<Integer> getTopicIdsForPaper(int paperId) throws SQLException {
        String query = "SELECT ref_topic FROM TopicPaper WHERE ref_paper = ?";
        Set<Integer> topicIds = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paperId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    topicIds.add(rs.getInt("ref_topic"));
                }
            }
        }
        return topicIds;
    }

    // (Facoltativo) Restituisce tutti i Paper associati a un Topic come oggetti, dato un PaperDao
    public List<PaperEntity> getPapersForTopic(int topicId, PaperDao paperDao) throws SQLException {
        List<PaperEntity> papers = new ArrayList<>();
        for (Integer idPaper : getPaperIdsForTopic(topicId)) {
            PaperEntity paper = paperDao.getById(idPaper);
            if (paper != null) {
                papers.add(paper);
            }
        }
        return papers;
    }

    // (Facoltativo) Restituisce tutte le associazioni come coppie [topic, paper]
    public Set<int[]> getAllTopicPaperAssociations() throws SQLException {
        String query = "SELECT ref_topic, ref_paper FROM TopicPaper";
        Set<int[]> result = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.add(new int[]{rs.getInt("ref_topic"), rs.getInt("ref_paper")});
            }
        }
        return result;
    }
}