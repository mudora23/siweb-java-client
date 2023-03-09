package com.siweb.controller;

import javafx.fxml.FXML;
import org.json.JSONObject;

public abstract class AdminController {
    protected final HttpController http = HttpController.getInstance();

    @FXML
    public void toAdminDashboard(){
        try{
            com.siweb.App.setRoot("admin-dashboard");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public void toAdminProfile(){
        try{
            com.siweb.App.setRoot("admin-student-list");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void logout(){
        http.logout((JSONObject res) -> {
            try{
                System.err.println("returning to login page...");
                com.siweb.App.setRoot("login");
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

}
