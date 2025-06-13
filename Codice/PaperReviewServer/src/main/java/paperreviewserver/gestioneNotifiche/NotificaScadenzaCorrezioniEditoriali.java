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
        ConsoleLogger.job("AdeguamentoContenuti", "Avvio notifica scadenza correzioni editoriali...");

        try (Connection connect = DBMSBoundary.getConnection()) {
            // Creazione delle DAO
            ConferenzaDao conferenzaDao = new ConferenzaDao(connect);
            ProceedingDao proceedingDao = new ProceedingDao(connect);
            UtenteDao utenteDao = new UtenteDao(connect);
            NotificaDao notificaDao = new NotificaDao(connect);

            // 4.1 Ottieni tutte le conferenze
            Map<Integer, String> conferenze = conferenzaDao.getAllIdsAndNomi();
            LocalDate oggi = LocalDate.now();

            // 4.2 Per ogni conferenza...
            for (Map.Entry<Integer, String> entry : conferenze.entrySet()) {
                int idConferenza = entry.getKey();
                String nomeConferenza = entry.getValue();

                // 4.2.1 Scadenza per le correzioni editoriali
                LocalDateTime scadenzaCorrezioni = conferenzaDao.getScadenzaEditing(idConferenza);

                // 4.2.2 Giorni di preavviso
                Integer giorniPreavviso = conferenzaDao.getGiorniPreavviso(idConferenza);

                if (scadenzaCorrezioni == null || giorniPreavviso == null) continue;

                // 4.2.3 Controllo del periodo di avviso
                LocalDate giornoScadenza = scadenzaCorrezioni.toLocalDate();
                long giorniRimanenti = java.time.temporal.ChronoUnit.DAYS.between(oggi, giornoScadenza);

                if (giorniRimanenti <= giorniPreavviso && !oggi.isAfter(giornoScadenza)) {
                    // 4.2.3.1 Trova gli editori per questa conferenza
                    List<Integer> editori = proceedingDao.getEditorByConferenza(idConferenza);

                    // 4.2.3.2 Ottieni info sugli editori
                    List<Object[]> utenti = utenteDao.getUtentiInfoByIds(editori);

                    for (Object[] utente : utenti) {
                        if (utente == null) continue;
                        int idUtente = (Integer) utente[0];
                        String nome = (String) utente[1];
                        String cognome = (String) utente[2];
                        String email = (String) utente[3];

                        // Crea il testo della notifica
                        String testoNotifica = String.format(
                                "Gentile %s %s, mancano %d giorni alla scadenza per l'invio delle correzioni editoriali nella conferenza: %s",
                                nome, cognome, giorniRimanenti, nomeConferenza);

                        // Inserisci la notifica nel database
                        notificaDao.inserisciNotifica(idUtente, idConferenza, testoNotifica);
                        ConsoleLogger.info(testoNotifica);

                        // Prepara subject e body dell'email
                        String subject = "Notifica di imminente scadenza correzioni editoriali";
                        String body = "<p>Gentile " + nome + " " + cognome + ",</p>" +
                                "<p>La scadenza per l'invio delle correzioni editoriali nella conferenza <b>" + nomeConferenza + "</b> " +
                                "si avvicina. Mancano solo <b>" + giorniRimanenti + "</b> giorni per inviare le correzioni.</p>" +
                                "<p>La scadenza è fissata per il giorno <b>" + giornoScadenza + "</b> alle 23:59. " +
                                "Ti invitiamo a completare l'invio delle correzioni prima della scadenza.</p>";

                        // Crea l'oggetto mail
                        NotificaScadenzaMail mail = new NotificaScadenzaMail(email, subject, body);

                        // Invio dell'email
                        try {
                            EmailSender.sendEmail(mail);
                            ConsoleLogger.success("Notifica e email inviata per correzioni editoriali a " + nome + " " + cognome + " (" + email + ")");
                        } catch (Exception e) {
                            ConsoleLogger.error("Errore invio email a " + email + ": " + e.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            ConsoleLogger.error("Errore durante il controllo notifiche Correzioni Editoriali: " + e.getMessage());
        }

        ConsoleLogger.line();
    }
}