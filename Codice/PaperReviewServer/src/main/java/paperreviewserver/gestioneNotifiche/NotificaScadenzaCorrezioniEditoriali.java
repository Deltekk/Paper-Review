package paperreviewserver.gestioneNotifiche;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import paperreviewserver.common.ConsoleLogger;
import paperreviewserver.common.DBMSBoundary;
import paperreviewserver.common.dao.ConferenzaDao;
import paperreviewserver.common.dao.NotificaDao;
import paperreviewserver.common.dao.ProceedingDao;
import paperreviewserver.common.dao.UtenteDao;
import paperreviewserver.common.email.EmailSender;
import paperreviewserver.common.email.NotificaScadenzaMail;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class NotificaScadenzaCorrezioniEditoriali implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ConsoleLogger.line();
        ConsoleLogger.job("AdeguamentoContenuti", "Avvio notifica scadenza...");

        // Logica del controllo
        ConsoleLogger.success("Notifica inviata correttamente");

        ConsoleLogger.line();
    }
}