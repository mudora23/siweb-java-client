package com.siweb.controller.utility;

import com.siweb.App;
import com.siweb.model.AppModel;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXSimpleNotification;
import io.github.palexdev.materialfx.enums.NotificationPos;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import io.github.palexdev.materialfx.notifications.base.INotification;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javafx.util.Duration;


public class UtilityNotificationController {

    private static final UtilityNotificationController instance = new UtilityNotificationController();
    private UtilityNotificationController(){}

    // Returns the instance of the controller
    public static UtilityNotificationController getInstance(){
        return instance;
    }

    public void init() {
        MFXNotificationSystem.instance().initOwner(AppModel.stage);
    }

    public void showSuccessMessage(String title) {
        _showMessage(title, "", false);
    }
    public void showSuccessMessage(String title, String description) {
        _showMessage(title, description, false);
    }
    public void showErrorMessage(String title) {
        _showMessage(title, "", true);
    }
    public void showErrorMessage(String title, String description) {
        _showMessage(title, description, true);
    }

    private void _showMessage(String title, String description, boolean isErrorMessage) {

        MFXNotificationSystem.instance()
                .initOwner(AppModel.stage)
                //.delaySetPosition(NotificationPos.TOP_RIGHT)
                .setPosition(NotificationPos.TOP_LEFT)
                .setSpacing(new Insets(AppModel.stage.getY() + 50,0,0,AppModel.stage.getX() + AppModel.stage.getWidth() - 440))
                .setCloseAfter(Duration.seconds(5))
                .publish(new MyNotification(title, description, isErrorMessage));
    }

    private class MyNotification extends MFXSimpleNotification {

        public MyNotification(String title, String description, boolean isErrorMessage) {

            String iconDescription;
            String cssFile;

            if(isErrorMessage) {
                iconDescription = "mfx-x-alt";
                cssFile = "css/mfxnotification-error.css";
            }
            else {
                iconDescription = "mfx-caspian-mark";
                cssFile = "css/mfxnotification-success.css";
            }

            MFXIconWrapper icon = new MFXIconWrapper(iconDescription, 30, 60); // mfx-variant7-mark
            icon.getStyleClass().add("icon");

            Label headerLabel = new Label();
            headerLabel.getStyleClass().add("headerLabel");
            //headerLabel.setPrefSize(30,30);


            headerLabel.textProperty().bind(new SimpleStringProperty(title));
            headerLabel.setWrapText(true);
        /*MFXIconWrapper readIcon = new MFXIconWrapper("mfx-eye", 16, 32);
        ((MFXFontIcon) readIcon.getIcon()).descriptionProperty().bind(Bindings.createStringBinding(
                () -> (getState() == NotificationState.READ) ? "mfx-eye" : "mfx-eye-slash",
                notificationStateProperty()
        ));
        StackPane.setAlignment(readIcon, Pos.CENTER_RIGHT);
        StackPane placeHolder = new StackPane(readIcon);*/
            //placeHolder.setMaxWidth(Double.MAX_VALUE);
            //HBox.setHgrow(placeHolder, Priority.ALWAYS);
            HBox header = new HBox(0, icon, headerLabel);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setPadding(InsetsFactory.of(5, 0, 5, 0));
            header.setMaxWidth(Double.MAX_VALUE);


            Label contentLabel = new Label();
            contentLabel.getStyleClass().add("content");
            contentLabel.textProperty().bind(new SimpleStringProperty(description));
            contentLabel.setWrapText(true);
            contentLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            contentLabel.setAlignment(Pos.TOP_LEFT);

            BorderPane container = new BorderPane();
            container.getStyleClass().add("notification");
            container.setTop(header);
            container.setCenter(contentLabel);
            container.getStylesheets().add(App.class.getResource(cssFile).toString());
            container.setMinHeight(0);
            container.setMaxHeight(300);
            container.setMinWidth(400);
            container.setMaxWidth(400);

            setContent(container);



        }

    }



}
