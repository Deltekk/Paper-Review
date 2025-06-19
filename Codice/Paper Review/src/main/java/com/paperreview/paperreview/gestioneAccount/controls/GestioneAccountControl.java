package com.paperreview.paperreview.gestioneAccount.controls;

import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.MainControl;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.gestioneNotifiche.controls.NotificaPushControl;
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
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneAccount/modificaPasswordBoundary/modificaPasswordBoundary.fxml");
    }

    @FXML
    private void handleLogout() {
        System.out.println("Esegui logout");

        UserContext.logout();

        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneAccount/loginBoundary/loginBoundary.fxml");
        mainControl.clearHistory();

        NotificaPushControl.fermaNotifichePush();
    }

    @FXML
    private void handleVisualizzaTopic(){
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/modificaTopic/modificaTopic.fxml");
    }

}