package com.siweb.controller;

import com.siweb.App;
import com.siweb.controller.utility.UtilityHttpController;
import com.siweb.model.UserModel;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.utils.ToggleButtonsUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class BaseController implements Initializable {

    protected final UtilityHttpController http = UtilityHttpController.getInstance();
    protected final UserModel userModel = UserModel.getInstance();

    //protected Scene scene;
    //protected Stage stage;



    //@FXML
    //protected Pane rootPane;




    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        // rootPane
        // simply use onAction in fxml
        /*this.forgetPasswordButton.setOnAction(event -> {
            this.forgetPassword();
        });
        this.loginButton.setOnAction(event -> {
            this.login();
        });*/


        /*if(rootPane != null) {

            System.err.println(rootPane);

            this.scene = rootPane.getScene();
            this.stage = (Stage) this.scene.getWindow();
            System.err.println("scene and stage set");
        }
        else {
            System.err.println("no scene and stage");
        }*/

    }

    public BaseController() {




    }



}
