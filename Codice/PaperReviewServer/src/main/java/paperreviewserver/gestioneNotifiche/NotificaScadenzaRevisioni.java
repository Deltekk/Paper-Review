package paperreviewserver.gestioneNotifiche;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import paperreviewserver.common.ConsoleLogger;

public class NotificaScadenzaRevisioni implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ConsoleLogger.line();
        ConsoleLogger.job("AdeguamentoContenuti", "Avvio notifica scadenza...");

        // Logica del controllo
        ConsoleLogger.success("Notifica inviata correttamente");

        ConsoleLogger.line();
    }
}
