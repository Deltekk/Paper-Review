package com.paperreview.paperreview.controllers;

import com.paperreview.paperreview.interfaces.ControlledScreen;
import javafx.fxml.FXML;

public class GestioneAccountControl implements ControlledScreen {

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    @FXML
    private void handleModificaPassword() {
        System.out.println("Vai alla modifica password");
        mainControl.setView("/com/paperreview/paperreview/boundaries/modificaPassword/modificaPasswordBoundary.fxml");
    }

    @FXML
    private void handleLogout() {
        System.out.println("Esegui logout");
        // TODO: Logica di logout

        mainControl.setView("/com/paperreview/paperreview/boundaries/login/loginBoundary.fxml");
        mainControl.clearHistory();
    }
}