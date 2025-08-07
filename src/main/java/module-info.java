//module com.example.connectverseproject {
//    requires javafx.controls;
//    requires javafx.fxml;
//    requires javafx.web;
//
//    requires org.controlsfx.controls;
//    requires com.dlsc.formsfx;
//    requires net.synedra.validatorfx;
//    requires org.kordamp.ikonli.javafx;
//    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
//    requires com.almasb.fxgl.all;
//
//    opens com.example.connectverseproject to javafx.fxml;
//    exports com.example.connectverseproject;
//}
//



module com.example.connectverseproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires java.sql; // ✅ This line fixes your error

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens com.example.connectverseproject to javafx.fxml;
    exports com.example.connectverseproject;
}
