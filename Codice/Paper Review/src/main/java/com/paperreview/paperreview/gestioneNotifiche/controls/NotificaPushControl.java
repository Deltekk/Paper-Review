package com.paperreview.paperreview.gestioneNotifiche.controls;

import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.InvitoDao;
import com.paperreview.paperreview.common.dbms.dao.NotificaDao;
import com.paperreview.paperreview.entities.InvitoEntity;
import com.paperreview.paperreview.entities.NotificaEntity;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.time.LocalDateTime;
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
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void avviaNotificheSeNonAttive(Pane rootPane) {
        if (timerAvviato) return;
        timerAvviato = true;

        scheduler.scheduleAtFixedRate(() -> {
            try {
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
                        mostraOverlay(rootPane, "Notifica", messaggio);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 15, TimeUnit.SECONDS);
    }

    private static void mostraOverlay(Pane parentPane, String titolo, String messaggio) {
        try {
            FXMLLoader loader = new FXMLLoader(NotificaPushControl.class.getResource(
                    "/com/paperreview/paperreview/boundaries/notifiche/notificaPush.fxml"));
            Parent popup = loader.load();

            NotificaPushControl control = loader.getController();
            control.setTitle(titolo);
            control.setMessage(messaggio);

            popup.setOpacity(0);
            parentPane.getChildren().add(popup);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), popup);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> parentPane.getChildren().remove(popup));
                }
            }, 4000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}