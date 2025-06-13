package paperreviewserver.gestioneNotifiche.controls;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import paperreviewserver.common.ConsoleLogger;
import paperreviewserver.common.DotenvUtil;
import paperreviewserver.gestioneNotifiche.*;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class ScadenzeControl {

    public static void inizializzaScheduler() {
        try {
            DotenvUtil.init();

            String cronExpr = DotenvUtil.getJobScheduleCron();

            // 🔎 Validazione cron sintattica
            try {
                TriggerBuilder.newTrigger()
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpr))
                        .build();
            } catch (Exception e) {
                ConsoleLogger.error("❌ Cron expression non valida: " + cronExpr);
                ConsoleLogger.error("ℹ️ Dettaglio errore: " + e.getMessage());
                ConsoleLogger.warning("Arresto del server per configurazione errata.");
                System.exit(1);
            }

            // ✅ Avvio scheduler solo se la cron è valida
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            // Pianificazione dei job con la connessione centralizzata
            List<Class<? extends Job>> jobs = Arrays.asList(
                    NotificaScadenzaAdeguamentoContenuti.class,
                    NotificaScadenzaAdeguamentoFormato.class,
                    NotificaScadenzaCorrezioniEditoriali.class,
                    NotificaScadenzaImpaginazione.class,
                    NotificaScadenzaRevisioni.class,
                    NotificaScadenzaSottomissioni.class
            );

            // Pianificazione dei job senza offset, partono tutti insieme
            for (Class<? extends Job> jobClass : jobs) {
                String jobName = jobClass.getSimpleName();

                JobDetail job = JobBuilder.newJob(jobClass)
                        .withIdentity(jobName, "notifiche")
                        .build();

                // Pianifica i job tutti contemporaneamente con la stessa cron expression
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trigger-" + jobName, "notifiche")
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpr)) // Usa la cron espressione per la pianificazione
                        .build();

                scheduler.scheduleJob(job, trigger);

                ConsoleLogger.info(String.format("📅 Job %s schedulato per: %s", jobName, cronExpr));
            }

        } catch (SchedulerException e) {
            ConsoleLogger.error("❌ Errore durante l'inizializzazione dello scheduler: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            ConsoleLogger.error("❌ Errore generale durante l'inizializzazione dello scheduler: " + e.getMessage());
            System.exit(1);
        }
    }
}