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
    requires jbcrypt;

    opens com.paperreview.paperreview to javafx.fxml;
    exports com.paperreview.paperreview;
    exports com.paperreview.paperreview.controllers;
    opens com.paperreview.paperreview.controllers to javafx.fxml;

}