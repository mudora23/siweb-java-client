package com.siweb;

import javafx.application.Application; // Application class from which JavaFX applications extend. https://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.html
import javafx.fxml.FXMLLoader;
// FXML introduction: http://fxexperience.com/2011/10/fxml-why-it-rocks-and-the-next-phase/
// Scene Builder Download: https://gluonhq.com/products/scene-builder/#download
// Skin JMetro: https://www.pixelduke.com/java-javafx-theme-jmetro/

import javafx.scene.Parent; // The base class for all nodes that have children in the scene graph. https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Parent.html
import javafx.scene.Scene; // The container for all content in a scene graph. https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html
import javafx.stage.Stage; // The top level JavaFX container. https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html

import javafx.scene.image.Image;
import javafx.scene.SceneAntialiasing; // Specifies the level of anti-aliasing desired. https://docs.oracle.com/javase/8/javafx/api/javafx/scene/SceneAntialiasing.html

import java.io.IOException;

public class App extends Application {

    public static final String APP_NAME = "SIWeb";
    public static final String APP_VERSION = "0.0.1";
    public static final String APP_DESC = "a simple student project";
    public static final String API_URI = "https://siweb.ltech.com.mo/api";
    //public static final String API_URI = "http://127.0.0.1:8000/api";

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        // set application icon
        stage.getIcons().add(new Image(getClass().getResource("images/logo.png").toString()));

        //FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/login.fxml"));
        scene = new Scene(loadFXML("login"), 1600, 900, true, SceneAntialiasing.BALANCED);
        stage.setTitle(String.format("%s (Ver. %s) - %s", APP_NAME, APP_VERSION, APP_DESC));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void setRoot(String fxml) {
        try {
            scene.setRoot(loadFXML(fxml));
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}