package paperreviewserver.gestioneNotifiche;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import paperreviewserver.common.ConsoleLogger;
import paperreviewserver.common.dbms.DBMSBoundary;
import paperreviewserver.common.dbms.dao.ConferenzaDao;
import paperreviewserver.common.dbms.dao.NotificaDao;
import paperreviewserver.common.dbms.dao.ProceedingDao;
import paperreviewserver.common.dbms.dao.UtenteDao;
import paperreviewserver.common.email.EmailSender;
import paperreviewserver.common.email.NotificaScadenzaMail;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class NotificaScadenzaImpaginazione implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ConsoleLogger.line();
        ConsoleLogger.job("AdeguamentoContenuti", "Avvio notifica scadenza...");

        try (Connection connect = DBMSBoundary.getConnection()) {
            // Apro Connessioni
            ConferenzaDao conferenzaDao = new ConferenzaDao(connect);
            ProceedingDao proceedingDao = new ProceedingDao(connect);
            UtenteDao utenteDao = new UtenteDao(connect);
            NotificaDao notificaDao = new NotificaDao(connect);

            // 4.1 Ottengo tutte le conferenze
            Map<Integer, String> conferenze = conferenzaDao.getAllIdsAndNomi();
            LocalDate oggi = LocalDate.now();

            // 4.2 Per ogni conferenza
            for (Map.Entry<Integer, String> entry : conferenze.entrySet()) {
                int idConferenza = entry.getKey();
                String nomeConferenza = entry.getValue();

                // 4.2.1 Scadenza Seconda Sottomissione Articoli
                LocalDateTime scadenza = conferenzaDao.getScadenzaImpaginazione(idConferenza);

                // 4.2.2 Giorni di Preavviso
                Integer giorniPreavviso = conferenzaDao.getGiorniPreavviso(idConferenza);

                if (scadenza == null || giorniPreavviso == null) continue;

                // 4.2.3 Controllo Periodo di Avviso
                LocalDate giornoScadenza = scadenza.toLocalDate();
                long giorniRimanenti = java.time.temporal.ChronoUnit.DAYS.between(oggi, giornoScadenza);

                if (giorniRimanenti <= giorniPreavviso && !oggi.isAfter(giornoScadenza)) {
                    // 4.2.3.1 Trova i proceedings che NON sono stati completati (campo nullo)
                    List<Object[]> datiProceedings = proceedingDao.getProceedingsNonSottomessiByConferenza(idConferenza);

                    for (Object[] row : datiProceedings) {
                        if (row == null) continue;
                        int idUtente = (Integer) row[0];
                        String nomePaper = (String) row[1];

                        // Ottieni le informazioni dell'utente (autore)
                        Object[] utente = utenteDao.getUtenteById(idUtente);
                        if (utente == null) continue;
                        String nome = (String) utente[0];
                        String cognome = (String) utente[1];
                        String email = (String) utente[2];

                        // Crea il testo della notifica
                        String testoNotifica = String.format(
                                "Attenzione mancano %d giorni alla scadenza della Impaginazione nella conferenza: %s",
                                giorniRimanenti, nomeConferenza);

                        // Inserisci la notifica nel database
                        notificaDao.inserisciNotifica(idUtente, idConferenza, testoNotifica);
                        ConsoleLogger.info(testoNotifica);

                        // Prepara subject e body dell'email
                        String subject = "Notifica di imminente scadenza sottomissione";
                        String body = "<p> Salve " + nome + " " + cognome +
                                ", le notifichiamo che ancora non ha sottomesso i Proceeding nella conferenza " +
                                nomeConferenza + ". La scadenza è prevista per giorno " + giornoScadenza +
                                " alle 23:59. E' pregato di rimediare al più presto.</p>";

                        // Crea l'oggetto mail
                        NotificaScadenzaMail mail = new NotificaScadenzaMail(email, subject, body);

                        // Invio dell'email
                        try {
                            EmailSender.sendEmail(mail);
                            ConsoleLogger.success("Notifica e email inviata per paper mancante a " + nome + " " + cognome + " (" + email + ")");
                        } catch (Exception e) {
                            ConsoleLogger.error("Errore invio email a " + email + ": " + e.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            ConsoleLogger.error("Errore durante il controllo notifiche Scadenza Impaginazione: " + e.getMessage());
        }

        ConsoleLogger.line();
    }
}
