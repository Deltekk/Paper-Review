package com.paperreview.paperreview.gestioneNotifiche.controls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.controlsfx.control.Notifications;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

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



}
