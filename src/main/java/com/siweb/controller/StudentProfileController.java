package com.siweb.controller;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.json.JSONObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.ImageView;



public class StudentProfileController extends StudentController   {
    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private AnchorPane slider;
    @FXML
    private Label fatherNameData;
    @FXML
    private Label motherNameData;
    @FXML
    private Label addressData;
    @FXML
    private Label telData;
    @FXML
    private Label usernameData;
    @FXML
    private Label emailData;



    public void initialize() {
        slider.setTranslateX(-190);
        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.5));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-190);

            slide.setOnFinished((ActionEvent ae) -> {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
            });
        });

        MenuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.5));
            slide.setNode(slider);

            slide.setToX(-190);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent ae) -> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);

            });
        });


        // get student data
        http.get("/student/current/", (JSONObject res) -> {
            // The only thread that is allowed to modify the JavaFX GUI is the JavaFX thread. We will need to wait for it.
            Platform.runLater(() -> {

                fatherNameData.setText(res.getString("fatherName"));
                motherNameData.setText(res.getString("motherName"));
                addressData.setText(res.getString("address"));
                telData.setText(res.getString("tel"));
                usernameData.setText(res.getJSONObject("user").getString("username"));
                emailData.setText(res.getJSONObject("user").getString("email"));
            });
        });
    }
}
