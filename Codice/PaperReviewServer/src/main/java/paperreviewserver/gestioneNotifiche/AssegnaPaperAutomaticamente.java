package paperreviewserver.gestioneNotifiche;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import paperreviewserver.common.ConsoleLogger;
import paperreviewserver.common.dbms.DBMSBoundary;
import paperreviewserver.common.dbms.dao.ConferenzaDao;
import paperreviewserver.common.dbms.dao.NotificaDao;
import paperreviewserver.common.dbms.dao.PaperDao;
import paperreviewserver.common.dbms.dao.UtenteDao;
import paperreviewserver.common.email.EmailSender;
import paperreviewserver.common.email.NotificaScadenzaMail;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AssegnaPaperAutomaticamente implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ConsoleLogger.line();
        ConsoleLogger.job("AdeguamentoContenuti", "Avvio notifica scadenza Adeguamento Contenuti...");

        try(Connection connect = DBMSBoundary.getConnection()) {
            // TODO: Logica di assegnazione
        } catch (SQLException e) {
            ConsoleLogger.error("Errore durante l'assegnazione dei paper: " + e.getMessage());
        }

        ConsoleLogger.line();
    }
}