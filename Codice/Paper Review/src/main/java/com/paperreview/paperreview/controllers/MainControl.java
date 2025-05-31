package com.paperreview.paperreview.controllers;

import com.paperreview.paperreview.interfaces.ControlledScreen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import java.io.IOException;
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
        // Carica la schermata di login all’avvio
        setView("/com/paperreview/paperreview/boundaries/login/loginBoundary.fxml");
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

            if (fxmlPath.contains("login") || fxmlPath.contains("registrazione") || fxmlPath.contains("recupero"))
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

    public void handleHome(){
        setView("/com/paperreview/paperreview/boundaries/home/homeBoundary.fxml");
    }

    public void handleGestisciConferenze(){
        setView("/com/paperreview/paperreview/boundaries/gestisciConferenze/homeBoundary.fxml");
    }

    public void handleRevisioni(){
        setView("/com/paperreview/paperreview/boundaries/revisioni/revisioniBoundary.fxml");
    }

    public void handleSottomissioni(){
        setView("/com/paperreview/paperreview/boundaries/sottomissioni/sottomissioniBoundary.fxml");
    }

    public void handleEditing(){
        setView("/com/paperreview/paperreview/boundaries/editing/editingBoundary.fxml");
    }

    public void hanleInserisciInvito(){
        setView("/com/paperreview/paperreview/boundaries/inserisciInvito/inserisciInvitoBoundary.fxml");
    }

    public void handleNotificheEdInviti(){
        setView("/com/paperreview/paperreview/boundaries/notificheEdInviti/notificheEdInvitiBoundary.fxml");
    }

    public void handleGestioneAccount(){
        setView("/com/paperreview/paperreview/boundaries/gestioneAccount/gestioneAccountBoundary.fxml");
    }

}
