package com.siweb.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.json.JSONObject;
import org.json.JSONArray;
import javafx.scene.control.Button;

public class AdminStudentListController extends AdminController {
    // for the admin profile controller initialize student data
    public static String studentURL;
    @FXML
    public ListView<String> studentListView;

    @FXML
    public ListView<Button> modifyListView;

    public void initialize() {
        // get all the student data
        http.get("/student/", (JSONArray res) -> {

            Platform.runLater(() -> {
                // refresh the list view while load the account page
                studentListView.getItems().clear();
                modifyListView.getItems().clear();

                for (Object student : res) {
                    // add the student name to the list
                    JSONObject user = ((JSONObject) student).getJSONObject("user");
                    String username = user.getString("username");
                    String firstName = user.getString("first_name");
                    String lastName = user.getString("last_name");
                    String studentId = String.valueOf(((JSONObject) student).getInt("id"));
                    studentListView.getItems().add(username + " " + firstName + " " + lastName);

                    // add the modify button to the list
                    Button modifyButton = new Button("Modify");


                    String url = "/student/" + studentId + "/";
                    // bind the url to the button
                    modifyButton.setOnAction(modifyButtonHandler(url));
                    modifyListView.getItems().add(modifyButton);
                }
            });
        });
    }

    public EventHandler<ActionEvent> modifyButtonHandler(String url) {
        return (ActionEvent ae) -> {
            Platform.runLater(() -> {
                // dynamically change the url
                AdminStudentListController.studentURL = url;
                com.siweb.App.setRoot("admin-profile");
            });
        };
    }
}
