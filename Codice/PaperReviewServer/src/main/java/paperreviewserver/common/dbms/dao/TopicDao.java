package paperreviewserver.common.dbms.dao;

import paperreviewserver.entities.TopicEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TopicDao {
    private final Connection connection;

    public TopicDao(Connection connection) {
        this.connection = connection;
    }

    public List<TopicEntity> getTopicsByPaper(int idPaper) throws SQLException {
        String query = """
                SELECT t.id_topic, t.nome
                FROM Topic t
                JOIN TopicPaper tp ON t.id_topic = tp.ref_topic
                WHERE tp.ref_paper = ?
                """;

        List<TopicEntity> topics = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPaper);
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