package paperreviewserver.gestioneNotifiche;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import paperreviewserver.common.ConsoleLogger;
import paperreviewserver.common.dbms.DBMSBoundary;
import paperreviewserver.common.dbms.dao.*;

import paperreviewserver.common.email.EmailAssegnazione;
import paperreviewserver.common.email.EmailSender;
import paperreviewserver.entities.ConferenzaEntity;
import paperreviewserver.entities.PaperEntity;
import paperreviewserver.entities.TopicEntity;
import paperreviewserver.entities.UtenteEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AssegnaPaperAutomaticamente implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ConsoleLogger.line();
        ConsoleLogger.job("AdeguamentoContenuti", "Avvio notifica scadenza Adeguamento Contenuti...");

        try (Connection connect = DBMSBoundary.getConnection()) {
            ConferenzaDao conferenzaDao = new ConferenzaDao(connect);
            List<ConferenzaEntity> conferenze = conferenzaDao.getAll();

            for (ConferenzaEntity conferenza : conferenze) {
                LocalDate scadenza = conferenza.getScadenzaSottomissione().toLocalDate();
                LocalDate oggi = LocalDate.now();
                String modalita = conferenza.getModalitaDistribuzione();

                if ("Broadcast".equalsIgnoreCase(modalita) && oggi.isAfter(scadenza.plusDays(2))) {
                    ConsoleLogger.job("Distribuzione Broadcast", conferenza.getNome());

                    UtenteDao utenteDao = new UtenteDao(connect);
                    PaperDao paperDao = new PaperDao(connect);

                    List<UtenteEntity> revisori = utenteDao.getRevisoriConferenza(conferenza.getId());
                    List<PaperEntity> papers = paperDao.getByConferenza(conferenza.getId());

                    distribuisciBroadcast(conferenza, papers, revisori, connect);

                    // Email di notifica per ogni assegnazione
                    for (PaperEntity paper : papers) {
                        String titoloPaper = paper.getTitolo();
                        Set<Integer> idRevisori = new RevisioneDao(connect).getRevisoriByPaper(paper.getId());
                        for (int id : idRevisori) {
                            Object[] info = utenteDao.getUtenteById(id);
                            if (info == null) continue;
                            String nome = (String) info[0];
                            String cognome = (String) info[1];
                            String email = (String) info[2];

                            try {
                                NotificaDao notificaDao = new NotificaDao(connect);
                                String testo = String.format("Ti è stato assegnato il paper \"%s\" nella conferenza \"%s\". Puoi visualizzarlo nella sezione 'Revisioni'.",
                                        titoloPaper, conferenza.getNome());
                                notificaDao.inserisciNotifica(id, conferenza.getId(), testo);
                                ConsoleLogger.success("Notifica inviata a " + nome + " " + cognome);
                            } catch (Exception e) {
                                ConsoleLogger.error("Errore inserimento notifica per " + nome + " " + cognome + ": " + e.getMessage());
                            }

                            EmailAssegnazione mail = new EmailAssegnazione(email, nome + " " + cognome, titoloPaper, conferenza.getNome());
                            try {
                                EmailSender.sendEmail(mail);
                                ConsoleLogger.success("Email assegnazione inviata a " + nome + " " + cognome);
                            } catch (Exception e) {
                                ConsoleLogger.error("Errore invio email a " + email + ": " + e.getMessage());
                            }
                        }
                    }
                } else if ("Topic".equalsIgnoreCase(modalita) && oggi.isAfter(scadenza)) {
                    ConsoleLogger.job("Distribuzione Topic", conferenza.getNome());

                    UtenteDao utenteDao = new UtenteDao(connect);
                    PaperDao paperDao = new PaperDao(connect);

                    List<UtenteEntity> revisori = utenteDao.getRevisoriConferenza(conferenza.getId());
                    List<PaperEntity> papers = paperDao.getByConferenza(conferenza.getId());

                    distribuisciPerTopic(conferenza, papers, revisori, connect);

                    // Email di notifica per ogni assegnazione
                    for (PaperEntity paper : papers) {
                        String titoloPaper = paper.getTitolo();
                        Set<Integer> idRevisori = new RevisioneDao(connect).getRevisoriByPaper(paper.getId());
                        for (int id : idRevisori) {
                            Object[] info = utenteDao.getUtenteById(id);
                            if (info == null) continue;
                            String nome = (String) info[0];
                            String cognome = (String) info[1];
                            String email = (String) info[2];

                            try {
                                NotificaDao notificaDao = new NotificaDao(connect);
                                String testo = String.format("Ti è stato assegnato il paper \"%s\" nella conferenza \"%s\". Puoi visualizzarlo nella sezione 'Revisioni'.",
                                        titoloPaper, conferenza.getNome());
                                notificaDao.inserisciNotifica(id, conferenza.getId(), testo);
                                ConsoleLogger.success("Notifica inviata a " + nome + " " + cognome);
                            } catch (Exception e) {
                                ConsoleLogger.error("Errore inserimento notifica per " + nome + " " + cognome + ": " + e.getMessage());
                            }

                            EmailAssegnazione mail = new EmailAssegnazione(email, nome + " " + cognome, titoloPaper, conferenza.getNome());
                            try {
                                EmailSender.sendEmail(mail);
                                ConsoleLogger.success("Email assegnazione inviata a " + nome + " " + cognome);
                            } catch (Exception e) {
                                ConsoleLogger.error("Errore invio email a " + email + ": " + e.getMessage());
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            ConsoleLogger.error("Errore durante l'assegnazione dei paper: " + e.getMessage());
        }

        ConsoleLogger.line();
    }

    private void distribuisciBroadcast(ConferenzaEntity conferenza,
                                       List<PaperEntity> papers,
                                       List<UtenteEntity> revisori,
                                       Connection conn) throws SQLException {

        RevisioneDao revisioneDao = new RevisioneDao(conn);
        Map<Integer, Integer> revisoriCount = new HashMap<>();

        // Conta quanti paper ha già ogni revisore (potrebbero averli scelti da broadcast)
        for (UtenteEntity revisore : revisori) {
            int count = revisioneDao.countPaperAssegnati(revisore.getId(), conferenza.getId());
            revisoriCount.put(revisore.getId(), count);
        }

        // Assegna solo i paper che hanno meno di 4 revisori
        for (PaperEntity paper : papers) {
            Set<Integer> revisoriCorrenti = revisioneDao.getRevisoriByPaper(paper.getId());

            // Assegna i revisori mancanti (fino a 4)
            while (revisoriCorrenti.size() < 4) {
                Optional<Integer> revisoreDisponibile = revisori.stream()
                        .filter(r -> revisoriCount.get(r.getId()) < 4)              // max 4 paper per revisore
                        .map(UtenteEntity::getId)
                        .filter(id -> !revisoriCorrenti.contains(id))              // evita doppioni
                        .findFirst();                                              // uno alla volta

                if (revisoreDisponibile.isEmpty()) break; // nessun revisore disponibile

                Integer idRevisore = revisoreDisponibile.get();
                revisioneDao.assegnaPaper(paper.getId(), idRevisore);
                revisoriCorrenti.add(idRevisore);
                revisoriCount.put(idRevisore, revisoriCount.get(idRevisore) + 1);
            }
        }
    }

    private void distribuisciPerTopic(ConferenzaEntity conferenza,
                                      List<PaperEntity> papers,
                                      List<UtenteEntity> revisori,
                                      Connection conn) throws SQLException {

        RevisioneDao revisioneDao = new RevisioneDao(conn);
        TopicUtenteDao topicUtenteDao = new TopicUtenteDao(conn);
        TopicDao topicDao = new TopicDao(conn);

        Map<Integer, Set<String>> topicRevisori = new HashMap<>();
        Map<Integer, Integer> revisoriCount = new HashMap<>();

        // Inizializza i topic e conteggi dei revisori
        for (UtenteEntity revisore : revisori) {
            Set<String> topics = topicUtenteDao.getTopicsForUser(revisore.getId())
                    .stream().map(t -> t.getNome().toLowerCase()).collect(Collectors.toSet());
            topicRevisori.put(revisore.getId(), topics);
            revisoriCount.put(revisore.getId(),
                    revisioneDao.countPaperAssegnati(revisore.getId(), conferenza.getId()));
        }

        for (PaperEntity paper : papers) {
            Set<Integer> revisoriCorrenti = revisioneDao.getRevisoriByPaper(paper.getId());
            Set<String> topicsPaper = topicDao.getTopicsByPaper(paper.getId())
                    .stream().map(t -> t.getNome().toLowerCase()).collect(Collectors.toSet());

            // Step 1: assegnamento per topic
            List<Integer> revisoriCompatibili = revisori.stream()
                    .filter(r -> revisoriCount.get(r.getId()) < 4)
                    .filter(r -> !revisoriCorrenti.contains(r.getId()))
                    .filter(r -> topicRevisori.get(r.getId()).stream().anyMatch(topicsPaper::contains))
                    .map(UtenteEntity::getId)
                    .toList();

            for (Integer idRevisore : revisoriCompatibili) {
                if (revisoriCorrenti.size() >= 4) break;
                revisioneDao.assegnaPaper(paper.getId(), idRevisore);
                revisoriCorrenti.add(idRevisore);
                revisoriCount.put(idRevisore, revisoriCount.get(idRevisore) + 1);
            }

            // Step 2: completamento casuale (se ancora meno di 4)
            List<Integer> revisoriRestanti = revisori.stream()
                    .filter(r -> revisoriCount.get(r.getId()) < 4)
                    .filter(r -> !revisoriCorrenti.contains(r.getId()))
                    .map(UtenteEntity::getId)
                    .toList();

            for (Integer idRevisore : revisoriRestanti) {
                if (revisoriCorrenti.size() >= 4) break;
                revisioneDao.assegnaPaper(paper.getId(), idRevisore);
                revisoriCorrenti.add(idRevisore);
                revisoriCount.put(idRevisore, revisoriCount.get(idRevisore) + 1);
            }
        }
    }
}