package com.paperreview.paperreview.common;

import com.paperreview.paperreview.gestioneNotifiche.controls.NotificaPushControl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.controlsfx.control.Notifications;

public class NotificaPush {

    private final String titleText;
    private final String contentText;

    public NotificaPush(String titleText, String contentText) {
        this.titleText = titleText;
        this.contentText = contentText;
    }

    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/paperreview/paperreview/boundaries/notification/notificationBoundary.fxml"));

            Node content = loader.load();

            NotificaPushControl controller = loader.getController();
            controller.setTitle(titleText);
            controller.setMessage(contentText);

            Notifications.create()
                    .graphic(content)
                    .hideAfter(javafx.util.Duration.seconds(5))
                    .position(javafx.geometry.Pos.BOTTOM_RIGHT)
                    .show();

        } catch (Exception e) {
            throw new RuntimeException("Errore nella visualizzazione della notifica", e);
        }
    }
}
