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
    requires org.checkerframework.checker.qual;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires net.bytebuddy;

    opens com.paperreview.paperreview to javafx.fxml;
    exports com.paperreview.paperreview;
    exports com.paperreview.paperreview.gestioneAccount.controls;
    opens com.paperreview.paperreview.gestioneAccount.controls to javafx.fxml;
    exports com.paperreview.paperreview.gestioneAccount.forms;
    opens com.paperreview.paperreview.gestioneAccount.forms to javafx.fxml;
    exports com.paperreview.paperreview.gestioneNotifiche.forms;
    opens com.paperreview.paperreview.gestioneNotifiche.forms to javafx.fxml;
    exports com.paperreview.paperreview.gestioneNotifiche.controls;
    opens com.paperreview.paperreview.gestioneNotifiche.controls to javafx.fxml;

    // Export the package containing your controller to javafx.fxml module
    exports com.paperreview.paperreview.gestioneConferenze.controls to javafx.fxml;
    opens com.paperreview.paperreview.gestioneConferenze.controls to javafx.fxml;

    exports com.paperreview.paperreview.presentazioneArticolo.controls to javafx.fxml;
    opens com.paperreview.paperreview.presentazioneArticolo.controls to javafx.fxml;
    exports com.paperreview.paperreview.gestioneRevisioni.controls;
    opens com.paperreview.paperreview.gestioneRevisioni.controls to javafx.fxml;
    exports com.paperreview.paperreview.gestioneRevisioni.forms;
    opens com.paperreview.paperreview.gestioneRevisioni.forms to javafx.fxml;

    exports com.paperreview.paperreview.gestionePaperDefinitivi.controls;
    opens com.paperreview.paperreview.gestionePaperDefinitivi.controls to javafx.fxml;
    exports com.paperreview.paperreview.gestioneNotifiche;
    opens com.paperreview.paperreview.gestioneNotifiche to javafx.fxml;
}
