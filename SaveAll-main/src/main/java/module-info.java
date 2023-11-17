module com.example.saveallfiles {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

//    requires org.json.JSONException;
//    requires org.json.JSONObject;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
//    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.saveallfiles to javafx.fxml;
    exports com.example.saveallfiles;
}