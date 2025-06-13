package paperreviewserver.gestioneNotifiche;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import paperreviewserver.common.ConsoleLogger;
import paperreviewserver.common.DBMSBoundary;
import paperreviewserver.common.dao.ConferenzaDao;
import paperreviewserver.common.dao.PaperDao;
import paperreviewserver.common.dao.NotificaDao;
import paperreviewserver.common.dao.UtenteDao;
import paperreviewserver.common.email.EmailSender;
import paperreviewserver.common.email.NotificaScadenzaMail;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class NotificaScadenzaSottomissioni implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        ConsoleLogger.line();
        ConsoleLogger.job("AdeguamentoContenuti", "Avvio notifica scadenza Paper...");

        // Apro Connessione
        try (Connection connection = DBMSBoundary.getConnection()) {

            ConferenzaDao conferenzaDao = new ConferenzaDao(connection);
            PaperDao paperDao = new PaperDao(connection);
            UtenteDao utenteDao = new UtenteDao(connection);
            NotificaDao notificaDao = new NotificaDao(connection);

            // 4.1 Ottengo tutte le conferenze
            Map<Integer, String> conferenze = conferenzaDao.getAllIdsAndNomi();
            LocalDate oggi = LocalDate.now();

            // 4.2 Per ogni conferenza...
            for (Map.Entry<Integer, String> entry : conferenze.entrySet()) {
                int idConf = entry.getKey();
                String nomeConferenza = entry.getValue();

                // 4.2.1 Scadenza sottomissione articoli
                LocalDateTime scadenza = conferenzaDao.getScadenzaSottomissione(idConf);

                // 4.2.2 Giorni di preavviso
                Integer giorniPreavviso = conferenzaDao.getGiorniPreavviso(idConf);

                if (scadenza == null || giorniPreavviso == null) continue;

                // 4.2.3 Controllo periodo di avviso
                LocalDate giornoScadenza = scadenza.toLocalDate();
                long giorniRimanenti = java.time.temporal.ChronoUnit.DAYS.between(oggi, giornoScadenza);

                if (giorniRimanenti <= giorniPreavviso && !oggi.isAfter(giornoScadenza)) {

                    // 4.2.3.1 Trova gli autori che NON hanno ancora sottomesso
                    List<Integer> idAutori = paperDao.getAutoriSenzaPaper(idConf);

                    // 4.2.3.2 Ottieni info Autori
                    List<Object[]> utenti = utenteDao.getUtentiInfoByIds(idAutori);

                    for (Object[] utente : utenti) {
                        if (utente == null) continue;
                        int id = (Integer) utente[0];
                        String nome = (String) utente[1];
                        String cognome = (String) utente[2];
                        String email = (String) utente[3];

                        // Creo testoNotifia
                        String testoNotifica = String.format(
                                "Attenzione mancano %d alla scadenza della sottomissione degli articoli nella conferenza: %s",
                                giorniRimanenti, nomeConferenza);

                        ConsoleLogger.info(testoNotifica);
                        notificaDao.inserisciNotifica(id, idConf, testoNotifica);
                        ConsoleLogger.success("Notifica inviata per paper mancante a " + nome + " " + cognome);

                        // Prepara subject e body (HTML o testo semplice)
                        String subject = "Notifica di imminente scadenza sottomissione";
                        String body = "<p> Salve " + nome + " " + cognome +
                                ", le notifichiamo che ancora non ha sottomesso il suo articolo nella conferenza " +
                                nomeConferenza + ". La scadenza è prevista per giorno " + giornoScadenza +
                                " alle 23:59. E' pregato di rimediare al più presto.</p>";

                        NotificaScadenzaMail mail = new NotificaScadenzaMail(email, subject, body);

                        try {
                            EmailSender.sendEmail(mail);
                            ConsoleLogger.success("Email inviata per paper mancante a " + nome + " " + cognome + " (" + email + ")");
                        } catch (Exception e) {
                            ConsoleLogger.error("Errore invio email a " + email + ": " + e.getMessage());
                        }
                    }


                    List<Object[]> datiPapers = paperDao.getPapersSenzaFileByConferenza(idConf);

                    for (Object[] dati : datiPapers) {
                        String titolo = (String) dati[1];
                        Integer refUtente = (Integer) dati[2];

                        Object[] utente = utenteDao.getUtenteById(refUtente);
                        if (utente == null) continue;
                        String nome = (String) utente[0];
                        String cognome = (String) utente[1];
                        String email = (String) utente[2];

                        String testoNotifica = String.format(
                                "Attenzione mancano %d giorni alla scadenza della sottomissione degli articoli nella conferenza: %s",
                                giorniRimanenti, nomeConferenza);

                        notificaDao.inserisciNotifica(refUtente, idConf, testoNotifica);
                        ConsoleLogger.info(testoNotifica);

                        // Prepara subject e body (HTML o testo semplice)
                        String subject = "Notifica di imminente scadenza sottomissione";
                        String body = "<p> Salve " + nome + " " + cognome +
                                ", le notifichiamo che ancora non ha sottomesso il suo articolo nella conferenza " +
                                nomeConferenza + ". La scadenza è prevista per giorno " + giornoScadenza +
                                " alle 23:59. E' pregato di rimediare al più presto.</p>" +
                                "<p> Nome paper mancante:" + titolo + "</p>";

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
            ConsoleLogger.line();
        } catch (SQLException e) {
            ConsoleLogger.error("Errore durante il controllo notifiche Scadenza Sottomissione: " + e.getMessage());
        }
    }
}