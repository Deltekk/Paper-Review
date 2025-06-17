package com.paperreview.paperreview.gestioneNotifiche.controls;

import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.InvitoDao;
import com.paperreview.paperreview.common.dbms.dao.NotificaDao;
import com.paperreview.paperreview.entities.InvitoEntity;
import com.paperreview.paperreview.entities.NotificaEntity;
import com.paperreview.paperreview.entities.NotificaPush;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.*;
import java.util.concurrent.*;

public class NotificaPushControl {

    @FXML
    private Label titleLabel;

    @FXML
    private Label messageLabel;

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    // ======== TIMER & NOTIFICHE ========

    private static boolean timerAvviato = false;
    private static final Set<Integer> notificati = new HashSet<>();
    private static ScheduledExecutorService scheduler;

    public static void avviaNotifichePush() {
        if (timerAvviato) return;
        timerAvviato = true;
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (UserContext.getUtente() == null) return;
                int idUtente = UserContext.getUtente().getId();
                NotificaDao notificaDao = new NotificaDao(DBMSBoundary.getConnection());
                InvitoDao invitoDao = new InvitoDao(DBMSBoundary.getConnection());

                List<NotificaEntity> notifiche = notificaDao.getAll(idUtente);
                List<InvitoEntity> inviti = invitoDao.getAll(idUtente);
                List<String> daMostrare = new ArrayList<>();

                for (NotificaEntity n : notifiche) {
                    if (notificati.add(n.getId())) {
                        daMostrare.add(n.getTesto());
                    }
                }

                for (InvitoEntity i : inviti) {
                    if (notificati.add(i.getId())) {
                        daMostrare.add(i.getTesto());
                    }
                }

                Platform.runLater(() -> {
                    for (String messaggio : daMostrare) {
                        new NotificaPush("Hai una nuova notifica!", messaggio).visualizza();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 15, TimeUnit.SECONDS);
    }

    public static void fermaNotifichePush() {
        if (!timerAvviato) return;

        scheduler.shutdownNow(); // Ferma immediatamente il task periodico
        scheduler = null;
        timerAvviato = false;
    }


}