package com.siweb.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.json.JSONObject;

public class StudentProfileController extends StudentController {

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
        // get student data
        http.get("/student/current/", (JSONObject res) -> {
            // The only thread that is allowed to modify the JavaFX GUI is the JavaFX thread. We will need to wait for it.
            Platform.runLater(()->{

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
