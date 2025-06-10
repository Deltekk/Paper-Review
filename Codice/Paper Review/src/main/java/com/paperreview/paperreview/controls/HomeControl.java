package com.paperreview.paperreview.controls;
import com.paperreview.paperreview.common.dao.ConferenzaDao;
import com.paperreview.paperreview.common.DBMSBoundary;
import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class HomeControl implements ControlledScreen {

    private MainControl mainControl;

    @FXML
    private FlowPane conferenzeContainer;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        caricaConferenze();
    }

    private void caricaConferenze(){
        try{
            ConferenzaDao conferenzaDao = new ConferenzaDao(DBMSBoundary.getConnection());
            List<ConferenzaEntity> conferenze  = conferenzaDao.getAll();

            conferenzeContainer.getChildren().clear();

            for(int i = 0; i < 10; i++){
                for (ConferenzaEntity c : conferenze) {
                    VBox card = creaCardConferenza(c);
                    conferenzeContainer.getChildren().add(card);
                }
            }



        }catch(SQLException e){
            e.printStackTrace();
            // TODO: Gestire errore come scritto nel flusso
        }
    }

    private VBox creaCardConferenza(ConferenzaEntity c) {
        VBox card = new VBox(10);
        card.getStyleClass().add("conference-card");
        card.setPrefWidth(300);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #44C8F4; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);");

        Label title = new Label(c.getNome());
        title.getStyleClass().addAll("h5", "text-bianco", "font-bold");
        title.setWrapText(true);


        FontIcon calendarIcon = new FontIcon("fas-calendar-times"); // icona calendario FontAwesome
        calendarIcon.setIconSize(16);
        calendarIcon.setIconColor(Color.WHITE); // o altro colore adatto

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Label date = new Label(c.getDataConferenza().toLocalDate().format(formatter));
        date.getStyleClass().addAll("h6", "text-bianco", "ombra", "font-light");

        // Metti icona + label in un HBox con spacing
        HBox dateBox = new HBox(5, calendarIcon, date);
        dateBox.setAlignment(Pos.CENTER_LEFT);

        Label description = new Label(c.getDescrizione());
        description.getStyleClass().addAll("h6", "text-bianco", "ombra", "font-light");
        description.setWrapText(true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button partecipateBtn = new Button("Partecipa");
        partecipateBtn.getStyleClass().add("green-button");
        partecipateBtn.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(title, dateBox, description, spacer, partecipateBtn);

        return card;
    }


}
