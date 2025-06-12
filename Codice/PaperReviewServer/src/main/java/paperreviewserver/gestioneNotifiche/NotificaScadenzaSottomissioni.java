package paperreviewserver.gestioneNotifiche;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import paperreviewserver.common.ConsoleLogger;
import paperreviewserver.common.DBMSBoundary;
import paperreviewserver.common.dao.ConferenzaDao;
import paperreviewserver.common.dao.PaperDao;
import paperreviewserver.common.dao.NotificaDao;
import paperreviewserver.common.dao.UtenteDao;
import paperreviewserver.common.email.EmailSender;
import paperreviewserver.common.email.MailBase;
import paperreviewserver.common.email.NotificaScadenzaMail;
import paperreviewserver.entities.PaperEntity;
import paperreviewserver.entities.UtenteEntity;

import java.io.Console;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class NotificaScadenzaSottomissioni implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ConsoleLogger.line();
        ConsoleLogger.job("AdeguamentoContenuti", "Avvio notifica scadenza...");

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
                LocalDate inizioAvviso = giornoScadenza.minusDays(giorniPreavviso);

                if (!oggi.isBefore(inizioAvviso) && !oggi.isAfter(giornoScadenza)) {

                    // 4.2.3.1 Trova gli autori che NON hanno ancora sottomesso
                    List<Object[]> datiPapers = paperDao.getPapersIdTitoloUtenteByConferenza(idConf);

                    ConsoleLogger.info(datiPapers.toString());

                    List<PaperEntity> paperEntities = new ArrayList<>();
                    for (Object[] dati : datiPapers) {
                        Integer idPaper = (Integer) dati[0];
                        String titolo = (String) dati[1];
                        Integer refUtente = (Integer) dati[2];
                        paperEntities.add(new PaperEntity(idPaper, titolo, refUtente));
                    }

                    // 4.2.3.2 Ottieni info per notifica/mailing
                    List<Integer> idAutori = paperEntities.stream()
                            .map(PaperEntity::getRefUtente)
                            .toList();

                    List<Object[]> utentiRaw = utenteDao.getUtentiInfoByIds(idAutori);

                    Map<Integer, UtenteEntity> utentiMap = new HashMap<>();
                    for (Object[] raw : utentiRaw) {
                        int id = (Integer) raw[0];
                        String nome = (String) raw[1];
                        String cognome = (String) raw[2];
                        String email = (String) raw[3];
                        utentiMap.put(id, new UtenteEntity(id, nome, cognome, email));
                    }

                    // Ora per ogni paper/autore invia notifica personalizzata
                    for (PaperEntity paper : paperEntities) {
                        UtenteEntity utente = utentiMap.get(paper.getRefUtente());
                        if (utente == null) continue;

                        String testoNotifica = String.format(
                                "Gentile %s %s, ricordati di sottomettere il tuo articolo %s per la conferenza %s!",
                                utente.getNome(), utente.getCognome(), paper.getTitolo(), nomeConferenza);

                        ConsoleLogger.info(testoNotifica);

                        notificaDao.inserisciNotifica(utente.getId(), idConf, testoNotifica);


                        // Prepara subject e body (HTML o testo semplice)
                        String subject = "Promemoria sottomissione articolo per la conferenza " + nomeConferenza;
                        String body = "<p>" + testoNotifica + "</p>";

                        NotificaScadenzaMail mail = new NotificaScadenzaMail(utente.getEmail(), subject, body);

                        try {
                            EmailSender.sendEmail(mail);
                            ConsoleLogger.success("Notifica e email inviata a " + utente.getNome() + " " + utente.getCognome() + " (" + utente.getEmail() + ")");
                        } catch (Exception e) {
                            ConsoleLogger.error("Errore invio email a " + utente.getEmail() + ": " + e.getMessage());
                        }

                        ConsoleLogger.success("Notifica e email inviata a "
                                + utente.getNome() + " " + utente.getCognome() + " (" + utente.getEmail() + ")");
                    }
                }
            }
        } catch (SQLException e) {
            ConsoleLogger.error("Errore durante il controllo notifiche: " + e.getMessage());
        }

        ConsoleLogger.success("Notifica inviata correttamente");
        ConsoleLogger.line();
    }
}