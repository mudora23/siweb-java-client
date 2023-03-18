package com.siweb.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.util.Map;

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

    @FXML
    private TextField firstNameData;

    @FXML
    private TextField lastNameData;

    private JSONObject originalDate;
    public void initialize() {
        String url = AdminStudentListController.studentURL;
        http.get(url, (JSONObject res) -> {
            Platform.runLater(() -> {
                originalDate = res;
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


    @FXML
    private void setFirstNameData(String FirstNameData) {
        this.firstNameData.setText(FirstNameData);
    }

    @FXML
    private void setLastNameData(String LastNameData) {
        this.lastNameData.setText(LastNameData);
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
            setFirstNameData(res.getJSONObject("user").getString("first_name"));
            setLastNameData(res.getJSONObject("user").getString("last_name"));
        });
    }

    @FXML
    public void updateStudentData() {
        String url = AdminStudentListController.studentURL;
        // Create a JSONObject to store the updated student data
        JSONObject updatedData = new JSONObject();
        JSONObject userData = new JSONObject();

        updatedData.put("fatherName", fatherNameData.getText());
        updatedData.put("motherName", motherNameData.getText());
        updatedData.put("address", addressData.getText());
        updatedData.put("tel", telData.getText());
        updatedData.put("id", originalDate.getInt("id"));

        userData.put("last_name", lastNameData.getText());
        userData.put("groups", originalDate.getJSONObject("user").getJSONArray("groups"));
        userData.put("id", originalDate.getJSONObject("user").getInt("id"));
        userData.put("first_name", firstNameData.getText());
        userData.put("url", originalDate.getJSONObject("user").getString("url"));
        userData.put("email", emailData.getText());
        userData.put("username", usernameData.getText());

        updatedData.put("user", userData);

        System.out.println(updatedData);
        // Send the updated data to the server using a PUT request
        http.put(url,updatedData.toMap(), (JSONObject response) -> {
            // Handle the server response, e.g., display a success message or handle errors
            Platform.runLater(this::toAdminProfile);
        });
    }
}

