module com.siweb.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.base;
    requires javafx.graphics;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.json;
    requires java.net.http;
    requires com.jfoenix;
    //img pack of ikonli
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.elusive;
    requires org.kordamp.ikonli.antdesignicons;

    opens com.siweb to javafx.fxml;
    exports com.siweb;
    exports com.siweb.model;
    opens com.siweb.model to javafx.fxml;
    exports com.siweb.controller;
    opens com.siweb.controller to javafx.fxml;
}