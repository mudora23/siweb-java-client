package com.siweb.controller;

import com.siweb.model.StudentModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class LoginController {

    private final HttpController http = HttpController.getInstance();
    @FXML
    private Label welcomeText;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Button forgetPasswordButton;

    private final StudentModel studentModel = new StudentModel();

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public void initialize(){

        // simply use onAction in fxml
        /*this.forgetPasswordButton.setOnAction(event -> {
            this.forgetPassword();
        });
        this.loginButton.setOnAction(event -> {
            this.login();
        });*/
    }

    @FXML
    public void forgetPassword(){

        // just for testing
        this.usernameTextField.setText("student_demo");
        this.passwordTextField.setText("student_demo");

    }

    @FXML
    public void login(){
        try {

            http.post("/auth/login/", Map.of("username", this.usernameTextField.getText(), "password", this.passwordTextField.getText()), (JSONObject resLogin) -> {

                // check the role of the user
                http.get("/group/current/", (JSONObject resGroup) -> {

                    // the user is a student
                    if(resGroup.getString("name").equals("student")) {

                        System.err.println("Welcome Back, Student!");
                        System.err.println();

                        com.siweb.App.setRoot("student-dashboard");
                    }
                    else {

                        // WIP, redirect admins / lecturers to different views

                    }
                });

            }, "login");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // testing only
    @FXML
    public void backToDashboard(){
        try {
            com.siweb.App.setRoot("student-dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}