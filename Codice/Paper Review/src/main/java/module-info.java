module com.paperreview.paperreview {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    requires javafx.graphics;
    requires javafx.swing;

    requires org.kordamp.ikonli.fontawesome5; // o il pack di icone che usi

    requires fr.brouillard.oss.cssfx;
    requires java.sql;
    requires jakarta.mail;
    requires io.github.cdimascio.dotenv.java;
    requires bcrypt;

    opens com.paperreview.paperreview to javafx.fxml;
    exports com.paperreview.paperreview;
    exports com.paperreview.paperreview.controls;
    opens com.paperreview.paperreview.controls to javafx.fxml;
    exports com.paperreview.paperreview.gestioneRevisioni;
    opens com.paperreview.paperreview.gestioneRevisioni to javafx.fxml;
    exports com.paperreview.paperreview.gestioneAccount.controls;
    opens com.paperreview.paperreview.gestioneAccount.controls to javafx.fxml;
    exports com.paperreview.paperreview.gestioneAccount.forms;
    opens com.paperreview.paperreview.gestioneAccount.forms to javafx.fxml;
    exports com.paperreview.paperreview.gestioneNotifiche.forms;
    opens com.paperreview.paperreview.gestioneNotifiche.forms to javafx.fxml;
    exports com.paperreview.paperreview.gestioneNotifiche.controls;
    opens com.paperreview.paperreview.gestioneNotifiche.controls to javafx.fxml;

}