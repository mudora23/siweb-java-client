package com.siweb.controller;

import com.siweb.model.AppModel;
import com.siweb.model.SectionTime;
import com.siweb.view.SelectOption;
import com.siweb.view.builder.BuilderMFXComboBoxController;
import com.siweb.view.builder.BuilderMFXTextFieldController;
import com.siweb.view.facade.FacadePaginatedTableController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

/***
 * AdminSectionTimeController manages the users management page of admin
 */
public class AdminSectionTimeController extends BaseController {

    private FacadePaginatedTableController<SectionTime> sectionTimesPaginatedTable;
    @FXML
    private TableView<SectionTime> sectionTimesTable;
    @FXML
    private Pagination sectionTimesTablePagination;
    @FXML
    private VBox sectionTimeDetailVBox;
    @FXML
    private MFXScrollPane sectionTimeDetailPane;

    @FXML
    private MFXButton sectionTimeDeleteBtn;
    @FXML
    private MFXButton sectionTimeSaveBtn;

    @FXML
    private HBox tableHeaderHBox;


    /***
     * initialize the page. Setup TableView, pagination, search field, and the ordering select
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources) {

        // request dependency before setting up... (to show data in the dropdown list. e.g. we need a list of semesters to be selected when adding / editing sections.)
        http.get("/academic/section/list_names/",(JSONArray res) ->{
            sectionModel.add(res, false);
            http.get("/academic/timeSlot/list_names/",(JSONArray res2) ->{
                timeSlotModel.add(res2, false);
                Platform.runLater(this::setup);
            });
        });

    }
    public void setup() {

        // default ordering, will be used in TableView and the ordering select
        String defaultOrdering = "section__course__course_code";

        // Create a new Facade class to easily manage the tableView with pagination
        sectionTimesPaginatedTable = new FacadePaginatedTableController.Builder<SectionTime>(sectionTimeModel, sectionTimesTable, sectionTimesTablePagination, "/academic/sectionTime/", "#resultsCountLabel")
                .addColumn(new TableColumn<SectionTime, String>("Section"), "getSection", 300)
                .addColumn(new TableColumn<SectionTime, String>("Time Slot"), "getTimeSlot", 300)
                .setPageSize(23)
                .setOrdering(defaultOrdering)
                .build();

        // Add a listener when select / deselect a row
        sectionTimesPaginatedTable.addSelectionListener((obs, oldSelection, newSelection) -> {

            // disable the buttons and clear the detail box first, then enable them accordingly if needed below
            sectionTimeDetailVBox.getChildren().clear();
            sectionTimeDetailPane.setVvalue(0.0);
            sectionTimeDeleteBtn.setDisable(true);
            sectionTimeSaveBtn.setDisable(true);

            // create a basic fade transition on the detail box
            FadeTransition fade = new FadeTransition();
            fade.setDuration(Duration.millis(100));
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.setNode(sectionTimeDetailVBox);
            fade.play();

            // if a user is selected, show the user's details and enable save / delete button.
            if (newSelection != null) {

                // enable save button
                sectionTimeSaveBtn.setDisable(false);

                // enable delete button
                sectionTimeDeleteBtn.setDisable(false);

                // show basic information
                sectionTimeDetailVBox.getChildren().add(new Label("Section Time Information"));
                sectionTimeDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("id","ID").setText(newSelection.getId() + "").setDisable(true).build().get());
                sectionTimeDetailVBox.getChildren().add(new BuilderMFXComboBoxController.Builder("section","Section *", sectionModel.getSelectOptionList()).setIsFiltered(true).setValText(newSelection.getSection()).build().get());
                sectionTimeDetailVBox.getChildren().add(new BuilderMFXComboBoxController.Builder("timeSlot","Time Slot *", timeSlotModel.getSelectOptionList()).setIsFiltered(true).setValText(newSelection.getTimeSlot()).build().get());
            }
        });


        // "search" button creation and listen to "ENTER" presses
        tableHeaderHBox.getChildren().add(new BuilderMFXTextFieldController.Builder("search", "Search").setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sectionTimesPaginatedTable.setSearch(((MFXTextField) AppModel.scene.lookup("#search")).getText());
                sectionTimesPaginatedTable.refresh(true);
            }
        }).setPrefWidth(230).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,10,4,10)).build().get());


        // "order by" select and listen to changes
        tableHeaderHBox.getChildren().add(new BuilderMFXComboBoxController.Builder("order_by", "Order By", List.of(
                new SelectOption("Course Code (ascending)", "section__course__course_code"),
                new SelectOption("Course Code (descending)", "-section__course__course_code"),
                new SelectOption("Time Slot (Start Time) (ascending)", "time_slot__start_time"),
                new SelectOption("Time Slot (Start Time) (descending)", "-time_slot__start_time"),
                new SelectOption("Time Slot (End Time) (ascending)", "time_slot__end_time"),
                new SelectOption("Time Slot (End Time) (descending)", "-time_slot__end_time")
        )).addSelectionListener((obs, oldSelection, newSelection)->{
            sectionTimesPaginatedTable.setOrdering(newSelection.getValText());
            sectionTimesPaginatedTable.refresh(true);
        }).setValText(defaultOrdering).setPrefWidth(280).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,4,4,10)).build().get());

    }

    /***
     * After clicking on the new button, show an empty form on the right for admin to fill in.
     */
    public void sectionTimeNew() {

        sectionTimeDetailVBox.getChildren().clear();
        sectionTimeDetailPane.setVvalue(0.0);
        sectionTimeDeleteBtn.setDisable(true);

        sectionTimesPaginatedTable.clearSelection();

        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(100));
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setNode(sectionTimeDetailVBox);
        fade.play();

        sectionTimeSaveBtn.setDisable(false);

        sectionTimeDetailVBox.getChildren().add(new Label("Section Time Information"));
        sectionTimeDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("id","ID").setDisable(true).build().get());
        sectionTimeDetailVBox.getChildren().add(new BuilderMFXComboBoxController.Builder("section","Section *", sectionModel.getSelectOptionList()).setIsFiltered(true).build().get());
        sectionTimeDetailVBox.getChildren().add(new BuilderMFXComboBoxController.Builder("timeSlot","Time Slot *", timeSlotModel.getSelectOptionList()).setIsFiltered(true).build().get());


    }

    /***
     * After clicking on the save button.
     * 1. if a sectionTime is selected, update the selected sectionTime with a PUT method to the server.
     * 2. if no sectionTime is selected, create a sectionTime user with a POST method to the server.
     */
    public void sectionTimeSave() {

        SectionTime selectedSectionTime = sectionTimesPaginatedTable.getCurrentSelection();

        SelectOption section = ((MFXComboBox<SelectOption>) AppModel.scene.lookup("#section")).getValue();
        SelectOption timeSlot = ((MFXComboBox<SelectOption>) AppModel.scene.lookup("#timeSlot")).getValue();

        // Updating the existing user and refresh the table
        if(selectedSectionTime != null)
        {


            http.put("/academic/sectionTime/" + selectedSectionTime.getId() + "/", Map.of(
                    "section",   section == null ? "" : section.getValText(),
                    "timeSlot",   timeSlot == null ? "" : timeSlot.getValText()
            ), (JSONObject jsonUser) -> {

                Platform.runLater(() -> {
                    sectionTimesPaginatedTable.refresh(false);
                });

            });

        }

        // create a new sectionTime and refresh the table
        else {

            http.post("/academic/sectionTime/", Map.of(
                    "sectionTime_code",         ((MFXTextField) AppModel.scene.lookup("#sectionTime_code")).getText(),
                    "section",   section == null ? "" : section.getValText(),
                    "timeSlot",   timeSlot == null ? "" : timeSlot.getValText()
            ), (JSONObject jsonUser) -> {

                Platform.runLater(() -> {

                    sectionTimeDetailVBox.getChildren().clear();
                    sectionTimeDetailPane.setVvalue(0.0);
                    sectionTimeDeleteBtn.setDisable(true);
                    sectionTimeSaveBtn.setDisable(true);

                    sectionTimesPaginatedTable.refresh(false);
                });
            });
        }

    }


    /***
     * After clicking the delete button, a confirmation box is shown, with the option to delete the sectionTime permanently.
     */
    public void sectionTimeDelete() {

        SectionTime selectedSectionTime = sectionTimesPaginatedTable.getCurrentSelection();

        if(selectedSectionTime != null)
        {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete confirmation");
            alert.setHeaderText("Are you sure to permanently delete the Section Time " + selectedSectionTime.getSection() + " - " + selectedSectionTime.getTimeSlot() + "?");
            alert.setContentText("The following sectionTime will be permanently deleted: " + "\nSection: " + selectedSectionTime.getSection() + "\nTime Slot: " + selectedSectionTime.getTimeSlot());
            alert.initOwner(AppModel.stage);
            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent())
            {
                System.err.println("Dialog exited");
            }
            else if(result.get() == ButtonType.OK)
            {
                System.err.println("Dialog OK pressed");

                http.delete("/academic/sectionTime/"+selectedSectionTime.getId()+"/", (JSONObject json) -> {

                    Platform.runLater(() -> {
                        sectionTimesPaginatedTable.refresh(false);
                    });
                });

            }
            else if(result.get() == ButtonType.CANCEL)
            {
                System.err.println("Dialog cancel pressed");
            }
        }

    }

}
