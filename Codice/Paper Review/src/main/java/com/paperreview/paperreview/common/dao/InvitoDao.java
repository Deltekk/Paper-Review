package com.paperreview.paperreview.common.dao;

import com.paperreview.paperreview.entities.InvitoEntity;
import com.paperreview.paperreview.enumerators.InvitoStatusEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InvitoDao extends BaseDao<InvitoEntity> {

    public InvitoDao(Connection connection) {
        super(connection, "Invito", "id_invito");
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (data, testo, status, email, codice, ref_conferenza, ref_mittente, ref_destinatario) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, InvitoEntity invito) throws SQLException {
        stmt.setObject(1, invito.getData());  // LocalDateTime
        stmt.setString(2, invito.getTesto());
        stmt.setString(3, invito.getStatus().getStatus());  // Usa il metodo getStatus() dell'Enum
        stmt.setString(4, invito.getEmail());
        stmt.setString(5, invito.getCodice());
        stmt.setInt(6, invito.getRefConferenza());
        stmt.setInt(7, invito.getRefMittente());
        stmt.setObject(8, invito.getRefDestinatario());  // Integer (può essere null)
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET data = ?, testo = ?, status = ?, email = ?, codice = ?, ref_conferenza = ?, ref_mittente = ?, ref_destinatario = ? " +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, InvitoEntity invito) throws SQLException {
        stmt.setObject(1, invito.getData());  // LocalDateTime
        stmt.setString(2, invito.getTesto());
        stmt.setString(3, invito.getStatus().getStatus());  // Usa il metodo getStatus() dell'Enum
        stmt.setString(4, invito.getEmail());
        stmt.setString(5, invito.getCodice());
        stmt.setInt(6, invito.getRefConferenza());
        stmt.setInt(7, invito.getRefMittente());
        stmt.setObject(8, invito.getRefDestinatario());  // Integer (può essere null)
        stmt.setInt(9, invito.getId());  // Aggiungi l'ID per il WHERE
    }

    @Override
    protected void setGeneratedId(InvitoEntity invito, int id) {
        invito.setId(id);
    }

    @Override
    protected InvitoEntity mapRow(ResultSet rs) throws SQLException {
        return new InvitoEntity(
                rs.getInt("id_invito"),
                rs.getObject("data", java.time.LocalDateTime.class),
                rs.getString("testo"),
                InvitoStatusEnum.fromString(rs.getString("status")),  // Converti la stringa in un enum
                rs.getString("email"),
                rs.getString("codice"),
                rs.getInt("ref_conferenza"),
                rs.getInt("ref_mittente"),
                (Integer) rs.getObject("ref_destinatario")  // Gestisci il valore null per ref_destinatario
        );
    }
}