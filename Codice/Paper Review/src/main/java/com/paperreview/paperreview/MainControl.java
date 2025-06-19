package com.paperreview.paperreview;

import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.TopicUtenteDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.entities.TopicEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class MainControl {

    @FXML
    private StackPane rootPane;

    @FXML
    private HBox header;

    @FXML
    private RowConstraints headerRow, contentRow;

    @FXML
    private ImageView logoImage;

    @FXML
    private Button logoButton;

    // Stack per tenere la cronologia delle viste
    private Stack<String> viewHistory = new Stack<>();

    @FXML
    public void initialize() {
        // Carica la schermata di loginBoundary all’avvio
        setView("/com/paperreview/paperreview/boundaries/gestioneAccount/loginBoundary/loginBoundary.fxml");
        Image logo = new Image(getClass().getResourceAsStream("/images/logoBianco.png"));
        Image hoverLogo = new Image(getClass().getResourceAsStream("/images/logoHover.png"));
        Image pressedLogo = new Image(getClass().getResourceAsStream("/images/logoPressed.png"));

        logoButton.setOnMouseEntered(event -> {
            logoImage.setImage(hoverLogo);
        });

        logoButton.setOnMousePressed(event -> {
            logoImage.setImage(pressedLogo);
        });

        logoButton.setOnMouseReleased(event -> {
            logoImage.setImage(hoverLogo);
        });

        logoButton.setOnMouseExited(event -> {logoImage.setImage(logo);});

        logoImage.setImage(logo);
    }

    public void showHeader() {
        header.setVisible(true);
        header.setManaged(true);
        headerRow.setPercentHeight(8);
        contentRow.setPercentHeight(92);

    }

    public void hideHeader() {
        header.setVisible(false);
        header.setManaged(false);
        headerRow.setPercentHeight(0);
        contentRow.setPercentHeight(100);
    }

    public void setView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ControlledScreen controlledScreen) {
                controlledScreen.setMainController(this);
            }

            rootPane.getChildren().setAll(view);

            if (fxmlPath.contains("loginBoundary") || fxmlPath.contains("registrazioneBoundary") || fxmlPath.contains("recupero"))
            {
                hideHeader();
            } else {

                // Aggiungi solo se diverso dall'ultimo
                if (viewHistory.isEmpty() || !viewHistory.peek().equals(fxmlPath)) {
                    viewHistory.push(fxmlPath);
                }

                showHeader();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBack() {

        for (int i = 0; i < viewHistory.size(); i++) {
            System.out.println(viewHistory.get(i));
        }

        if (viewHistory.size() > 1) {
            // Rimuovo vista corrente
            viewHistory.pop();

            // Carico la vista precedente

            String previousView = viewHistory.peek();
            setView(previousView);

            System.out.println("Previous view: " + previousView);
        }
    }

    public void clearHistory() {
        viewHistory.clear();
    }

    public void handleHome(){
        setView("/com/paperreview/paperreview/boundaries/gestioneNotifiche/home/homeBoundary.fxml");
    }

    public void handleGestisciConferenze(){
        setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/gestioneConferenze/gestioneConferenzeBoundary.fxml");
    }

    public void handleRevisioni(){

        try(Connection connection = DBMSBoundary.getConnection())
        {

            // Controlliamo se l'utente corrente ha almeno 3 topic assegnati

            TopicUtenteDao topicUtenteDao = new TopicUtenteDao(connection);
            Set<TopicEntity> topics = topicUtenteDao.getTopicsForUser(UserContext.getUtente().getId());

            // Se no reindirizziamo alla pagina di modifica topic

            if(topics.size() < 3)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Operazione non consentita");
                alert.setHeaderText("Per fare da revisore devi prima impostare i tuoi topic!");
                alert.setContentText(String.format("Verrai reindirizzato alla pagina di modifica dei tuoi topic."));
                alert.showAndWait();

                setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/modificaTopic/modificaTopicBoundary.fxml");
                return;
            }

            // Se si allora lo lasciamo procedere

            setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/visualizzaSchermataRevisioni/visualizzaSchermataRevisioniBoundary.fxml");

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void handleSottomissioni(){
        setView("/com/paperreview/paperreview/boundaries/presentazioneArticolo/visualizzaSchermataSottomissioni/visualizzaSchermataSottomissioniBoundary.fxml");
    }

    public void handleEditing(){
        setView("/com/paperreview/paperreview/boundaries/gestionePaperDefinitivi/editing/editingBoundary.fxml");
    }

    public void hanleInserisciInvito(){
        setView("/com/paperreview/paperreview/boundaries/gestioneNotifiche/inserisciCodiceInvito/inserisciCodiceInvitoBoundary.fxml");
    }

    public void handleNotificheEdInviti(){
        setView("/com/paperreview/paperreview/boundaries/gestioneNotifiche/visualizzaNotificheInviti/visualizzaNotificheInvitiBoundary.fxml");
    }

    public void handleGestioneAccount(){
        setView("/com/paperreview/paperreview/boundaries/gestioneAccount/gestioneAccountBoundary/gestioneAccountBoundary.fxml");
    }

}
