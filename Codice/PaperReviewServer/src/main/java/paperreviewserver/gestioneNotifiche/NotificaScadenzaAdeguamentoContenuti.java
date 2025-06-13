package paperreviewserver.gestioneNotifiche;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import paperreviewserver.common.ConsoleLogger;
import paperreviewserver.common.DBMSBoundary;
import paperreviewserver.common.dao.ConferenzaDao;
import paperreviewserver.common.dao.NotificaDao;
import paperreviewserver.common.dao.PaperDao;
import paperreviewserver.common.dao.UtenteDao;
import paperreviewserver.common.email.EmailSender;
import paperreviewserver.common.email.NotificaScadenzaMail;

import java.awt.print.Paper;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NotificaScadenzaAdeguamentoContenuti implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ConsoleLogger.line();
        ConsoleLogger.job("AdeguamentoContenuti", "Avvio notifica scadenza Adeguamento Contenuti...");
        try(Connection connect = DBMSBoundary.getConnection()) {
            // Apro Connessioni
            ConferenzaDao conferenzaDao = new ConferenzaDao(connect);
            PaperDao paperDao = new PaperDao(connect);
            UtenteDao utenteDao = new UtenteDao(connect);
            NotificaDao notificaDao = new NotificaDao(connect);

            // 4.1 Ottengo tutte le conferenze
            Map<Integer, String> conferenze = conferenzaDao.getAllIdsAndNomi();
            LocalDate oggi = LocalDate.now();
            // 4.2 Per ogni conferenza
            for(Map.Entry<Integer, String> entry : conferenze.entrySet()) {
                int idConferenza = entry.getKey();
                String nomeConferenza = entry.getValue();

                // 4.2.1 Scadenza Seconda Sottomissione Articoli
                LocalDateTime scadenza = conferenzaDao.getScadenzaSottomissione2(idConferenza);

                // 4.2.2 Giorni di Preavviso
                Integer giorniPreavviso = conferenzaDao.getGiorniPreavviso(idConferenza);

                if (scadenza == null || giorniPreavviso == null) continue;

                // 4.2.3 Controllo Periodo di Avviso
                LocalDate giornoScadenza = scadenza.toLocalDate();
                long giorniRimanenti = java.time.temporal.ChronoUnit.DAYS.between(oggi, giornoScadenza);

                if (giorniRimanenti <= giorniPreavviso && !oggi.isAfter(giornoScadenza)) {
                    // 4.2.3.1 Trova gli autori che NON hanno ancora caricato la nuova sottomissione
                    LocalDateTime fineScadenza = LocalDateTime.of(giornoScadenza, LocalTime.of(23, 59, 59));
                    List<Object[]> dati = paperDao.getPapersByDataSottomissione(idConferenza,fineScadenza);

                    for(Object[] row : dati) {
                        if(row == null) continue;
                        int idUtente = (Integer) row[0];
                        String nomePaper = (String) row[1];

                        Object[] utente = utenteDao.getUtenteById(idUtente);
                        if (utente == null) continue;
                        String nome = (String) utente[1];
                        String cognome = (String) utente[2];
                        String email = (String) utente[3];

                        String testoNotifica = String.format(
                                "Attenzione mancano %d giorni alla scadenza della sottomissione degli articoli nella conferenza: %s",
                                giorniRimanenti, nomeConferenza);

                        notificaDao.inserisciNotifica(idUtente, idConferenza, testoNotifica);
                        ConsoleLogger.info(testoNotifica);

                        // Prepara subject e body (HTML o testo semplice)
                        String subject = "Notifica di imminente scadenza sottomissione";
                        String body = "<p> Salve " + nome + " " + cognome +
                                ", le notifichiamo che ancora non ha sottomesso il suo articolo nella conferenza " +
                                nomeConferenza + ". La scadenza è prevista per giorno " + giornoScadenza +
                                " alle 23:59. E' pregato di rimediare al più presto.</p>" +
                                "<p> Nome paper mancante:" + nomePaper + "</p>";

                        NotificaScadenzaMail mail = new NotificaScadenzaMail(email, subject, body);

                        try {
                            EmailSender.sendEmail(mail);
                            ConsoleLogger.success("Notifica e email inviata per file mancante a " + nome + " " + cognome + " (" + email + ")");
                        } catch (Exception e) {
                            ConsoleLogger.error("Errore invio email a " + email + ": " + e.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            ConsoleLogger.error("Errore durante il controllo notifiche: " + e.getMessage());
        }

        ConsoleLogger.line();
    }
}