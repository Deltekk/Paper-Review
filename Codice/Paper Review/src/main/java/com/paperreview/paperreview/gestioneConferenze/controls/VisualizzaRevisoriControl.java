package com.paperreview.paperreview.gestioneConferenze.controls;

import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.RuoloConferenzaDao;
import com.paperreview.paperreview.common.dbms.dao.UtenteDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.entities.Ruolo;
import com.paperreview.paperreview.entities.UtenteEntity;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Set;

public class VisualizzaRevisoriControl implements ControlledScreen {
    @FXML
    private FlowPane revisoriContainer;

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        try{
            ConferenzaEntity conferenza = UserContext.getConferenzaAttuale();

            RuoloConferenzaDao ruoloConferenzaDao = new RuoloConferenzaDao(DBMSBoundary.getConnection());

            Set<Integer> revisoriId = ruoloConferenzaDao.getIdUtentiByRuoloAndConferenza(Ruolo.Revisore, conferenza.getId());

            if(revisoriId.isEmpty())
            {
                Label testo = new Label("Non ci sono ancora revisori per questa conferenza!");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);

                revisoriContainer.getChildren().add(testo);
            }
            else
            {
                UtenteDao utenteDao = new UtenteDao(DBMSBoundary.getConnection());

                for (Integer idRevisore : revisoriId) {
                        Node card = creaRevisoreCard(utenteDao.getById(idRevisore));
                        revisoriContainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            // TODO: Gestire questo errore
            throw new RuntimeException(e);
        }

    }

    private Node creaRevisoreCard(UtenteEntity utente) {
        HBox card = new HBox(20);
        card.getStyleClass().addAll("revisore-card", "bg-celeste");
        card.setPrefWidth(300);
        card.setMaxWidth(300);
        card.setAlignment(Pos.CENTER);

        FontIcon revisoreIcon = new FontIcon("fas-user-tie");
        revisoreIcon.setIconSize(24);
        revisoreIcon.setIconColor(Color.WHITE);
        revisoreIcon.getStyleClass().add("ombra");

        Label nomeLabel = new Label(String.format("%s %s", utente.getNome(), utente.getCognome()));
        nomeLabel.getStyleClass().addAll("font-bold", "text-bold", "h5", "ombra", "text-bianco");
        nomeLabel.setWrapText(true);

        card.getChildren().addAll(revisoreIcon, nomeLabel);

        return card;
    }

}
