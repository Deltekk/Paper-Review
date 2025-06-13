package paperreviewserver;

import paperreviewserver.common.ConsoleLogger;
import paperreviewserver.common.DBMSBoundary;
import paperreviewserver.common.DotenvUtil;
import paperreviewserver.gestioneNotifiche.controls.ScadenzeControl;


public class PaperReviewServer {

    public static void main(String[] args) {

        // Disabilitiamo i warnings del logger
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "warn");
        System.setProperty("org.slf4j.simpleLogger.log.org.quartz", "warn");

        ConsoleLogger.init(); // Inizializiamo il logger

        ConsoleLogger.info("Avvio del server di gestione notifiche...");

        DotenvUtil.init(); // Inizializiamo le variabili d'ambiente

        DBMSBoundary.init();
        ScadenzeControl.inizializzaScheduler();

        ConsoleLogger.success("Server avviato con successo.");
    }

}
