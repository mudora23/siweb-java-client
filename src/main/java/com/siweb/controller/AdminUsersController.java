package com.siweb.controller;

import com.siweb.model.AppModel;
import com.siweb.view.SelectOption;
import com.siweb.view.builder.BuilderMFXComboBoxController;
import com.siweb.view.builder.BuilderMFXTextFieldController;
import com.siweb.view.facade.FacadePaginatedTableController;
import com.siweb.model.User;
import com.siweb.model.UserModel;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.enums.FloatMode;


import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.json.JSONObject;

import java.net.URL;
import java.util.*;

public class AdminUsersController extends BaseController {

    private FacadePaginatedTableController<User> usersPaginatedTable;
    private final UserModel userModel = UserModel.getInstance();
    @FXML
    private TableView usersTable;
    @FXML
    private Pagination usersTablePagination;
    @FXML
    private Label resultsCountLabel;
    @FXML
    private VBox userDetailVBox;
    @FXML
    private MFXScrollPane userDetailPane;

    @FXML
    private MFXButton userDeleteBtn;
    @FXML
    private MFXButton userSaveBtn;

    @FXML
    private HBox tableHeaderHBox;


    public void initialize(URL location, ResourceBundle resources) {

        super.initialize(location, resources);

        String defaultOrdering = "-date_joined";

        usersPaginatedTable = new FacadePaginatedTableController.Builder<User>(userModel, usersTable, usersTablePagination, "/user/", "#resultsCountLabel")
                .addColumn(new TableColumn<User, String>("Username"), "getUserName", 130)
                .addColumn(new TableColumn<User, String>("First Name"), "getFirstName", 130)
                .addColumn(new TableColumn<User, String>("Last Name"), "getLastName", 130)
                .addColumn(new TableColumn<User, String>("Email"), "getEmail", 240)
                .addColumn(new TableColumn<User, String>("Role"), "getProfileRole", 110)
                .setPageSize(23)
                .setOrdering(defaultOrdering)
                .build();

        usersPaginatedTable.addSelectionListener((obs, oldSelection, newSelection) -> {


            userDetailVBox.getChildren().clear();
            userDetailPane.setVvalue(0.0);
            userDeleteBtn.setDisable(true);
            userSaveBtn.setDisable(true);


            FadeTransition fade = new FadeTransition();
            fade.setDuration(Duration.millis(100));
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.setNode(userDetailVBox);
            fade.play();

            if (newSelection != null) {

                if(userModel.getCurrentUserID() != newSelection.id) {
                    // disable deleting own admin account
                    userDeleteBtn.setDisable(false);
                }
                userSaveBtn.setDisable(false);

                userDetailVBox.getChildren().add(new Label("Basic Information"));
                userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("id","ID").setText(newSelection.getId() + "").setDisable(true).build().get());
                userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("username","Username *").setText(newSelection.getUserName()).build().get());
                userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("first_name","First Name").setText(newSelection.getFirstName()).build().get());
                userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("last_name","Last Name").setText(newSelection.getLastName()).build().get());
                userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("email","Email").setText(newSelection.getEmail()).build().get());

                userDetailVBox.getChildren().add(new Separator());

                userDetailVBox.getChildren().add(new Label("Profile Details"));
                userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("","Profile ID").setText(newSelection.getProfileId() + "").setDisable(true).build().get());

                if(userModel.getCurrentUserID() != newSelection.id) {
                    userDetailVBox.getChildren().add(new BuilderMFXComboBoxController.Builder("role", "Role *", List.of(new SelectOption("admin"), new SelectOption("lecturer"), new SelectOption("student"))).setValText(newSelection.getProfileRole()).build().get());
                }
                else {
                    // disable changing "role" for own admin account
                    userDetailVBox.getChildren().add(new BuilderMFXComboBoxController.Builder("role", "Role *", List.of(new SelectOption("admin"), new SelectOption("lecturer"), new SelectOption("student"))).setValText(newSelection.getProfileRole()).setDisable(true).build().get());
                }
                userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("father_name","Father Name").setText(newSelection.getProfileFatherName()).build().get());
                userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("mother_name","Mother Name").setText(newSelection.getProfileMotherName()).build().get());
                userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("address_1","Address 1").setText(newSelection.getProfileAddress1()).build().get());
                userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("address_2","Address 2").setText(newSelection.getProfileAddress2()).build().get());
                userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("tel","Tel.").setText(newSelection.getProfileTel()).build().get());

            }
        });


        // search
        tableHeaderHBox.getChildren().add(new BuilderMFXTextFieldController.Builder("search", "Search").setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                usersPaginatedTable.setSearch(((MFXTextField) AppModel.scene.lookup("#search")).getText());
                usersPaginatedTable.refresh();
            }
        }).setPrefWidth(230).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,10,4,10)).build().get());



        // order by
        tableHeaderHBox.getChildren().add(new BuilderMFXComboBoxController.Builder("order_by", "Order By", List.of(
                new SelectOption("Date joined (ascending)", "date_joined"),
                new SelectOption("Date joined (descending)", "-date_joined"),
                new SelectOption("Username (ascending)", "username"),
                new SelectOption("Username (descending)", "-username"),
                new SelectOption("First Name (ascending)", "first_name"),
                new SelectOption("First Name (descending)", "-first_name"),
                new SelectOption("Last Name (ascending)", "last_name"),
                new SelectOption("Last Name (descending)", "-last_name"),
                new SelectOption("Email (ascending)", "email"),
                new SelectOption("Email (descending)", "-email")
        )).addSelectionListener((obs, oldSelection, newSelection)->{
            usersPaginatedTable.setOrdering(newSelection.getValText());
            usersPaginatedTable.refresh();
        }).setValText(defaultOrdering).setPrefWidth(230).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,4,4,10)).build().get());




    }

    public void userNew() {

        userDetailVBox.getChildren().clear();
        userDetailPane.setVvalue(0.0);
        userDeleteBtn.setDisable(true);

        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(100));
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setNode(userDetailVBox);
        fade.play();

        userSaveBtn.setDisable(false);

        userDetailVBox.getChildren().add(new Label("Basic Information"));
        userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("id","ID").setDisable(true).build().get());
        userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("username","Username *").build().get());
        userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("first_name","First Name").build().get());
        userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("last_name","Last Name").build().get());
        userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("email","Email").build().get());

        userDetailVBox.getChildren().add(new Separator());

        userDetailVBox.getChildren().add(new Label("Profile Details"));
        userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("","Profile ID").setDisable(true).build().get());
        userDetailVBox.getChildren().add(new BuilderMFXComboBoxController.Builder("role","Role *", List.of(new SelectOption("admin"), new SelectOption("lecturer"), new SelectOption("student"))).build().get());
        userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("father_name","Father Name").build().get());
        userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("mother_name","Mother Name").build().get());
        userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("address_1","Address 1").build().get());
        userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("address_2","Address 2").build().get());
        userDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("tel","Tel.").build().get());

    }

    public void userSave() {

        User selectedUser = usersPaginatedTable.getCurrentSelection();

        if(selectedUser != null)
        {
            http.put("/user/" + selectedUser.getId() + "/", Map.of(
                    "username",     ((MFXTextField) AppModel.scene.lookup("#username")).getText(),
                    "first_name",   ((MFXTextField) AppModel.scene.lookup("#first_name")).getText(),
                    "last_name",    ((MFXTextField) AppModel.scene.lookup("#last_name")).getText(),
                    "email",        ((MFXTextField) AppModel.scene.lookup("#email")).getText(),
                    "role",         ((MFXComboBox<SelectOption>) AppModel.scene.lookup("#role")).getValue().getValText(),
                    "father_name",  ((MFXTextField) AppModel.scene.lookup("#father_name")).getText(),
                    "mother_name",  ((MFXTextField) AppModel.scene.lookup("#mother_name")).getText(),
                    "address_1",    ((MFXTextField) AppModel.scene.lookup("#address_1")).getText(),
                    "address_2",    ((MFXTextField) AppModel.scene.lookup("#address_2")).getText(),
                    "tel",         ((MFXTextField) AppModel.scene.lookup("#tel")).getText()
            ), (JSONObject jsonUser) -> {

                Platform.runLater(() -> {
                    usersPaginatedTable.refresh();
                });

            });

        }

        else {





            http.post("/user/", Map.of(
                    "username",     ((MFXTextField) AppModel.scene.lookup("#username")).getText(),
                    "first_name",   ((MFXTextField) AppModel.scene.lookup("#first_name")).getText(),
                    "last_name",    ((MFXTextField) AppModel.scene.lookup("#last_name")).getText(),
                    "email",        ((MFXTextField) AppModel.scene.lookup("#email")).getText(),
                    "role",         ((MFXComboBox<SelectOption>) AppModel.scene.lookup("#role")).getValue().getValText(),
                    "father_name",  ((MFXTextField) AppModel.scene.lookup("#father_name")).getText(),
                    "mother_name",  ((MFXTextField) AppModel.scene.lookup("#mother_name")).getText(),
                    "address_1",    ((MFXTextField) AppModel.scene.lookup("#address_1")).getText(),
                    "address_2",    ((MFXTextField) AppModel.scene.lookup("#address_2")).getText(),
                    "tel",         ((MFXTextField) AppModel.scene.lookup("#tel")).getText()
            ), (JSONObject jsonUser) -> {

                Platform.runLater(() -> {

                    userDetailVBox.getChildren().clear();
                    userDetailPane.setVvalue(0.0);
                    userDeleteBtn.setDisable(true);
                    userSaveBtn.setDisable(true);

                    usersPaginatedTable.refresh();
                });

            });









        }

    }



    public void userDelete() {

        User selectedUser = usersPaginatedTable.getCurrentSelection();

        if(selectedUser != null)
        {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete confirmation");
            alert.setHeaderText("Are you sure to permanently delete the " + selectedUser.getProfileRole() +" \"" + selectedUser.getFirstName() + " " + selectedUser.getLastName() + "\"?");
            alert.setContentText("The following user will be permanently deleted: " + "\nRole: " + selectedUser.getProfileRole() + "\nUsername: " + selectedUser.getUserName() + "\nEmail: " + selectedUser.getEmail() + "\nFirst name: " + selectedUser.getFirstName() + "\nLast name: " + selectedUser.getLastName());
            alert.initOwner(AppModel.stage);
            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent())
            {
                System.err.println("Dialog exited");
            }
            else if(result.get() == ButtonType.OK)
            {
                System.err.println("Dialog OK pressed");

                http.delete("/user/"+selectedUser.getId()+"/", (JSONObject jsonUserCurrent) -> {

                    Platform.runLater(() -> {
                        usersPaginatedTable.refresh();
                    });
                });

            }
            else if(result.get() == ButtonType.CANCEL)
            {
                System.err.println("Dialog cancel pressed");
            }
        }





    }



    public void sortedBy() {
        //sortedBySelect
    }



}
