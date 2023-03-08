package com.siweb.controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import org.json.JSONObject;

import java.util.Map;

public abstract class StudentController {

    protected final HttpController http = HttpController.getInstance();

    @FXML
    public void toStudentDashboard(){
        try{
            com.siweb.App.setRoot("student-dashboard");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public void toStudentProfile(){
        try{
            com.siweb.App.setRoot("student-profile");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void logout(){
        http.logout();
    }

}
