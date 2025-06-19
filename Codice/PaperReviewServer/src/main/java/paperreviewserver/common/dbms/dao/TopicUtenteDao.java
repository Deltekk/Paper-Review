package paperreviewserver.common.dbms.dao;

import paperreviewserver.entities.TopicEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TopicUtenteDao {
    private final Connection connection;

    public TopicUtenteDao(Connection connection) {
        this.connection = connection;
    }

    public List<TopicEntity> getTopicsForUser(int idUtente) throws SQLException {
        String query = """
                SELECT t.id_topic, t.nome
                FROM Topic t
                JOIN TopicUtente tu ON t.id_topic = tu.ref_topic
                WHERE tu.ref_utente = ?
                """;

        List<TopicEntity> topics = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    topics.add(new TopicEntity(
                            rs.getInt("id_topic"),
                            rs.getString("nome")
                    ));
                }
            }
        }
        return topics;
    }
}