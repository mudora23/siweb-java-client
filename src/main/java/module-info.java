module com.siweb.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    requires java.net.http;

    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.fontawesome;

    opens com.siweb to javafx.fxml;
    exports com.siweb;
    exports com.siweb.model;
    opens com.siweb.model to javafx.fxml;
    exports com.siweb.controller;
    opens com.siweb.controller to javafx.fxml;
}