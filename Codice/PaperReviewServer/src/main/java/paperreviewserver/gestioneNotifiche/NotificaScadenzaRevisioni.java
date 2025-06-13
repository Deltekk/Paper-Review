package paperreviewserver.gestioneNotifiche;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import paperreviewserver.common.ConsoleLogger;
import paperreviewserver.common.DBMSBoundary;
import paperreviewserver.common.dao.*;
import paperreviewserver.common.email.EmailSender;
import paperreviewserver.common.email.NotificaScadenzaMail;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class NotificaScadenzaRevisioni implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        ConsoleLogger.line();
        ConsoleLogger.job("AdeguamentoContenuti", "Avvio notifica scadenza Revisioni...");
        try(Connection connection = DBMSBoundary.getConnection()) {
            // Apro Connessioni
            ConferenzaDao conferenzaDao = new ConferenzaDao(connection);
            RevisioneDao revisioneDao = new RevisioneDao(connection);
            UtenteDao utenteDao = new UtenteDao(connection);
            NotificaDao notificaDao = new NotificaDao(connection);

            // 4.1 Ottengo tutte le conferenze
            Map<Integer, String> conferenze = conferenzaDao.getAllIdsAndNomi();
            LocalDate oggi = LocalDate.now();

            // 4.2 Per ogni conferenza
            for(Map.Entry<Integer, String> entry : conferenze.entrySet()) {
                int idConferenza = entry.getKey();
                String conferenza = entry.getValue();

                // 4.2.1 Scadenza Revisioni Articoli
                LocalDateTime scadenza = conferenzaDao.getScadenzaRevisione(idConferenza);

                // 4.2.2 Giorni di Preavviso
                Integer giorniPreavviso = conferenzaDao.getGiorniPreavviso(idConferenza);

                if(scadenza == null || giorniPreavviso == null) continue;

                // 4.2.3 Controllo Periodo di Avviso
                LocalDate giornoScadenza = scadenza.toLocalDate();
                long giorniRimanenti = java.time.temporal.ChronoUnit.DAYS.between(oggi, giornoScadenza);

                if(giorniRimanenti <= giorniPreavviso && !oggi.isAfter(giornoScadenza)) {
                    // 4.2.3.1 Trova i revisori che NON hanno ancora caricato la propria revisione
                    List<Integer> idAutori = revisioneDao.getRevisoriSenzaRevisione(idConferenza);

                    // 4.2.3.2 Ottieni info Autori
                    List<Object[]> utenti = utenteDao.getUtentiInfoByIds(idAutori);

                    for(Object[] utente : utenti) {
                        if(utente == null) continue;
                        int idUtente = (Integer) utente[0];
                        String nome = utente[1].toString();
                        String cognome = utente[2].toString();
                        String email = utente[3].toString();

                        // Creo testoNotifica
                        String testoNotifica = String.format(
                                "Attenzione mancano %d alla scadenza della sottomissione delle revisioni nella conferenza: %s",
                                giorniRimanenti, conferenza);

                        ConsoleLogger.info(testoNotifica);
                        notificaDao.inserisciNotifica(idUtente, idConferenza, testoNotifica);

                        // Prepara subject e body (HTML o testo semplice)
                        String subject = "Notifica di imminente scadenza Revisione";
                        String body = "<p> Salve " + nome + " " + cognome +
                                ", le notifichiamo che ancora non ha sottomesso alcune delle sue revisioni nella conferenza " +
                                conferenza + ". La scadenza della sottomissione delle revisioni è prevista per giorno " + giornoScadenza +
                                " alle 23:59. E' pregato di rimediare al più presto.</p>";

                        NotificaScadenzaMail mail = new NotificaScadenzaMail(email, subject, body);

                        try {
                            EmailSender.sendEmail(mail);
                            ConsoleLogger.success("Notifica e email inviata per paper mancante a " + nome + " " + cognome + " (" + email + ")");
                        } catch (Exception e) {
                            ConsoleLogger.error("Errore invio email a " + email + ": " + e.getMessage());
                        }
                    }
                }
            }
            ConsoleLogger.line();
        } catch (SQLException e) {
            ConsoleLogger.error("Errore durante il controllo notifiche: " + e.getMessage());
        }
    }
}
