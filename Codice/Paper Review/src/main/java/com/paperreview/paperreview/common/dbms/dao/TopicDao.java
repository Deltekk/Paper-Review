package com.paperreview.paperreview.common.dbms.dao;

import com.paperreview.paperreview.entities.TopicEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TopicDao extends BaseDao<TopicEntity> {

    public TopicDao(Connection connection) {
        super(connection, "Topic", "id_topic");
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (nome) VALUES (?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, TopicEntity entity) throws SQLException {
        stmt.setString(1, entity.getNome());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET nome = ? WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, TopicEntity entity) throws SQLException {
        stmt.setString(1, entity.getNome());
        stmt.setInt(2, entity.getId());
    }

    @Override
    protected void setGeneratedId(TopicEntity entity, int id) {
        entity.setId(id);
    }

    @Override
    protected TopicEntity mapRow(ResultSet rs) throws SQLException {
        TopicEntity topic = new TopicEntity();
        topic.setId(rs.getInt("id_topic"));
        topic.setNome(rs.getString("nome"));
        return topic;
    }
}