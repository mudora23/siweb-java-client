package com.siweb.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;

public class AdminProfileController extends AdminController {
    @FXML
    private TextField fatherNameData;
    @FXML
    private TextField motherNameData;
    @FXML
    private TextField addressData;
    @FXML
    private TextField telData;
    @FXML
    private TextField usernameData;
    @FXML
    private TextField emailData;

    public void initialize() {
        String url = AdminStudentListController.studentURL;
        http.get(url, (JSONObject res) -> {
            Platform.runLater(() -> {
                setStudentData(res);
            });
        });
    }
    @FXML
    private void setFatherNameData(String fatherNameData) {
        this.fatherNameData.setText(fatherNameData);
    }

    @FXML
    private void setMotherNameData(String motherNameData) {
        this.motherNameData.setText(motherNameData);
    }

    @FXML
    private void setAddressData(String addressData) {
        this.addressData.setText(addressData);
    }

    @FXML
    private void setTelData(String telData) {
        this.telData.setText(telData);
    }

    @FXML
    private void setUsernameData(String usernameData) {
        this.usernameData.setText(usernameData);
    }

    @FXML
    private void setEmailData(String emailData) {
        this.emailData.setText(emailData);
    }


    public void setStudentData(JSONObject res) {
        Platform.runLater(() -> {
            System.out.println(res);
            setFatherNameData(res.getString("fatherName"));
            setMotherNameData(res.getString("motherName"));
            setAddressData(res.getString("address"));
            setTelData(res.getString("tel"));
            setUsernameData(res.getJSONObject("user").getString("username"));
            setEmailData(res.getJSONObject("user").getString("email"));
        });
    }


}
