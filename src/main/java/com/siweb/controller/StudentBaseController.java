package com.siweb.controller;

import com.siweb.App;
import com.siweb.model.AppModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import java.net.URL;
import java.util.ResourceBundle;


public class StudentBaseController extends BaseController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // App Logo
        Label menuLabel = new Label(AppModel.APP_NAME);
        menuLabel.setPadding(new Insets(8));
        this.mainMenu.getChildren().add(menuLabel);


        ToggleButton tempToggle = null;

        // Page - Profile
        tempToggle = createToggle("mfx-user", "Profile");
        tempToggle.setOnAction(event -> {
            App.loadFXMLtoPane(this.contentArea, "student-profiles");
            toggleClearSelectedExcept(this.mainMenu, (ToggleButton) event.getSource());
        });
        tempToggle.fire();
        tempToggle.setSelected(true);
        this.mainMenu.getChildren().add(tempToggle);

//        tempToggle = createToggle("mfx-calendars", "Semester");
//        tempToggle.setOnAction(event -> {
//            App.loadFXMLtoPane(this.contentArea, "student-semester");
//            toggleClearSelectedExcept(this.mainMenu, (ToggleButton) event.getSource());
//        });
//        this.mainMenu.getChildren().add(tempToggle);
//
        tempToggle = createToggle("mfx-google-sites", "Enrollment");
        tempToggle.setOnAction(event -> {
            App.loadFXMLtoPane(this.contentArea, "student-enrollment");
            toggleClearSelectedExcept(this.mainMenu, (ToggleButton) event.getSource());
        });
        this.mainMenu.getChildren().add(tempToggle);



        // Page - Logout
        tempToggle = createToggle("mfx-shortcut", "Log out");
        tempToggle.setOnAction(event -> logout());
        this.mainMenu.getChildren().add(tempToggle);

    }

}
