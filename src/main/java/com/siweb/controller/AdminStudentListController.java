package com.siweb.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.json.JSONObject;
import org.json.JSONArray;
import javafx.scene.control.Button;

public class AdminStudentListController extends AdminController{

    @FXML
    public ListView<String> studentListView;

    @FXML
    public ListView<Button> modifyListView;

    public void initialize() {

        http.get("/student/", (JSONArray res) -> {

            Platform.runLater(() -> {
                studentListView.getItems().clear();
                for (Object student : res) {
                    JSONObject name = ((JSONObject) student).getJSONObject("user");
                    String username = name.getString("username");
                    studentListView.getItems().add(username);
                }

                modifyListView.getItems().clear();
                for (Object student : res) {
                    Button modifyButton = new Button("Modify");
                    modifyListView.getItems().add(modifyButton);
                }
            });
        });
    }

}
