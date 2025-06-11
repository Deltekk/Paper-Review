package com.paperreview.paperreview.common.dao;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class CoAutoriPaperDao {

    private final Connection connection;

    public CoAutoriPaperDao(Connection connection) {
        this.connection = connection;
    }

    // Aggiungi un coautore a un paper
    public void addCoautoreToPaper(String email, int paperId) throws SQLException {
        String query = "INSERT INTO CoAutoriPaper (email, ref_paper) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setInt(2, paperId);
            stmt.executeUpdate();
        }
    }

    // Rimuovi un coautore da un paper
    public void removeCoautoreFromPaper(String email, int paperId) throws SQLException {
        String query = "DELETE FROM CoAutoriPaper WHERE email = ? AND ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setInt(2, paperId);
            stmt.executeUpdate();
        }
    }

    // Rimuovi tutti i coautori da un paper
    public void removeAllCoautoriFromPaper(int paperId) throws SQLException {
        String query = "DELETE FROM CoAutoriPaper WHERE ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paperId);
            stmt.executeUpdate();
        }
    }

    // Ottieni tutte le email dei coautori di un paper
    public Set<String> getCoautoriForPaper(int paperId) throws SQLException {
        String query = "SELECT email FROM CoAutoriPaper WHERE ref_paper = ?";
        Set<String> coautori = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paperId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    coautori.add(rs.getString("email"));
                }
            }
        }
        return coautori;
    }

    // Ottieni tutti i paper associati a una email di coautore
    public Set<Integer> getPapersForCoautore(String email) throws SQLException {
        String query = "SELECT ref_paper FROM CoAutoriPaper WHERE email = ?";
        Set<Integer> papers = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    papers.add(rs.getInt("ref_paper"));
                }
            }
        }
        return papers;
    }

    // Verifica se una email è già coautore di un certo paper
    public boolean isCoautoreOfPaper(String email, int paperId) throws SQLException {
        String query = "SELECT 1 FROM CoAutoriPaper WHERE email = ? AND ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setInt(2, paperId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Conta i coautori di un paper
    public int countCoautoriForPaper(int paperId) throws SQLException {
        String query = "SELECT COUNT(*) FROM CoAutoriPaper WHERE ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paperId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    // (Facoltativo) Ottieni tutte le associazioni [email, ref_paper]
    public List<String[]> getAllCoautorePaperAssociations() throws SQLException {
        String query = "SELECT email, ref_paper FROM CoAutoriPaper";
        List<String[]> result = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.add(new String[] {rs.getString("email"), String.valueOf(rs.getInt("ref_paper"))});
            }
        }
        return result;
    }
}