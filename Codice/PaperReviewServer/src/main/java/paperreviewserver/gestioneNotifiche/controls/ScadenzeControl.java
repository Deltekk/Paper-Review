package paperreviewserver.gestioneNotifiche.controls;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import paperreviewserver.common.ConsoleLogger;
import paperreviewserver.common.DotenvUtil;
import paperreviewserver.gestioneNotifiche.*;

import java.util.Arrays;
import java.util.Date;
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

            List<Class<? extends Job>> jobs = Arrays.asList(
                    NotificaScadenzaAdeguamentoContenuti.class,
                    NotificaScadenzaAdeguamentoFormato.class,
                    NotificaScadenzaCorrezioniEditoriali.class,
                    NotificaScadenzaImpaginazione.class,
                    NotificaScadenzaRevisioni.class,
                    NotificaScadenzaSottomissioni.class
            );

            // Creazione dei job con l'uso della connessione centralizzata
            int offsetInSeconds = 0; // Iniziamo con 0 secondi per il primo job
            for (Class<? extends Job> jobClass : jobs) {
                String jobName = jobClass.getSimpleName();

                JobDetail job = JobBuilder.newJob(jobClass)
                        .withIdentity(jobName, "notifiche")
                        .build();

                // Impostazione del trigger con un offset di 30 secondi tra i job
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trigger-" + jobName, "notifiche")
                        .startAt(new Date(System.currentTimeMillis() + offsetInSeconds * 1000L))  // Aggiungi offset di 30 secondi per ogni job
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpr)) // Usa la cron espressione per la pianificazione
                        .build();

                scheduler.scheduleJob(job, trigger);

                ConsoleLogger.info(String.format("📅 Job %s schedulato per: %s (con offset di %d secondi)", jobName, cronExpr, offsetInSeconds));

                // Aumenta l'offset di 30 secondi per il prossimo job
                offsetInSeconds += 30;
            }

        } catch (SchedulerException e) {
            ConsoleLogger.error("❌ Errore durante l'inizializzazione dello scheduler: " + e.getMessage());
            System.exit(1);
        }
    }

}
