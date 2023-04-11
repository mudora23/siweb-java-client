package com.siweb.controller;

import com.siweb.App;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

import java.net.URL;
import java.util.ResourceBundle;

public class LecturerBaseController extends BaseController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // App Logo
        Label menuLabel = new Label("SI Web");
        menuLabel.setPadding(new Insets(8));
        this.mainMenu.getChildren().add(menuLabel);


        ToggleButton tempToggle = null;

        // Page - Profile
        tempToggle = createToggle("mfx-user", "Profile");
        tempToggle.setOnAction(event -> {
            App.loadFXMLtoPane(this.contentArea, "lecturer-profiles");
            toggleClearSelectedExcept(this.mainMenu, (ToggleButton) event.getSource());
        });
        tempToggle.fire();
        tempToggle.setSelected(true);
        this.mainMenu.getChildren().add(tempToggle);

        tempToggle = createToggle("mfx-google-sites", "Enrollment");
        tempToggle.setOnAction(event -> {
            App.loadFXMLtoPane(this.contentArea, "lecturer-enrollment");
            toggleClearSelectedExcept(this.mainMenu, (ToggleButton) event.getSource());
        });
        this.mainMenu.getChildren().add(tempToggle);

        // Page - Logout
        tempToggle = createToggle("mfx-shortcut", "Log out");
        tempToggle.setOnAction(event -> logout());
        this.mainMenu.getChildren().add(tempToggle);

    }

}
