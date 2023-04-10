package com.siweb.controller;

import com.siweb.model.AppModel;
import com.siweb.model.Section;
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
 * AdminSectionController manages the users management page of admin
 */
public class AdminSectionController extends BaseController {

    private FacadePaginatedTableController<Section> sectionsPaginatedTable;
    @FXML
    private TableView<Section> sectionsTable;
    @FXML
    private Pagination sectionsTablePagination;
    @FXML
    private VBox sectionDetailVBox;
    @FXML
    private MFXScrollPane sectionDetailPane;

    @FXML
    private MFXButton sectionDeleteBtn;
    @FXML
    private MFXButton sectionSaveBtn;

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
        http.get("/academic/course/list_names/",(JSONArray res) ->{
            courseModel.add(res, false);
            http.get("/user/list_lecturer_names/",(JSONArray res2) ->{
                userModel.add(res2, false);
                Platform.runLater(this::setup);
            });
        });
    }
    public void setup() {

        // default ordering, will be used in TableView and the ordering select
        String defaultOrdering = "course__course_code";

        // Create a new Facade class to easily manage the tableView with pagination
        sectionsPaginatedTable = new FacadePaginatedTableController.Builder<Section>(sectionModel, sectionsTable, sectionsTablePagination, "/academic/section/", "#resultsCountLabel")
                .addColumn(new TableColumn<Section, String>("Section Code"), "getCode", 120)
                .addColumn(new TableColumn<Section, String>("Course"), "getCourse", 300)
                .addColumn(new TableColumn<Section, String>("Lecturer"), "getLecturer", 300)
                .setPageSize(23)
                .setOrdering(defaultOrdering)
                .build();

        // Add a listener when select / deselect a row
        sectionsPaginatedTable.addSelectionListener((obs, oldSelection, newSelection) -> {

            // disable the buttons and clear the detail box first, then enable them accordingly if needed below
            sectionDetailVBox.getChildren().clear();
            sectionDetailPane.setVvalue(0.0);
            sectionDeleteBtn.setDisable(true);
            sectionSaveBtn.setDisable(true);

            // create a basic fade transition on the detail box
            FadeTransition fade = new FadeTransition();
            fade.setDuration(Duration.millis(100));
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.setNode(sectionDetailVBox);
            fade.play();

            // if a user is selected, show the user's details and enable save / delete button.
            if (newSelection != null) {

                // enable save button
                sectionSaveBtn.setDisable(false);

                // enable delete button
                sectionDeleteBtn.setDisable(false);

                // show basic information
                sectionDetailVBox.getChildren().add(new Label("Section Information"));
                sectionDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("id","ID").setText(newSelection.getId() + "").setDisable(true).build().get());

                sectionDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("section_code","Section Code *").setText(newSelection.getCode()).build().get());

                sectionDetailVBox.getChildren().add(new BuilderMFXComboBoxController.Builder("course","Course *", courseModel.getSelectOptionList()).setValText(newSelection.getCourse()).build().get());

                sectionDetailVBox.getChildren().add(new BuilderMFXComboBoxController.Builder("lecturer","Lecturer *", userModel.getSelectOptionList("lecturer")).setValText(newSelection.getLecturer()).build().get());

            }
        });


        // "search" button creation and listen to "ENTER" presses
        tableHeaderHBox.getChildren().add(new BuilderMFXTextFieldController.Builder("search", "Search").setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sectionsPaginatedTable.setSearch(((MFXTextField) AppModel.scene.lookup("#search")).getText());
                sectionsPaginatedTable.refresh(true);
            }
        }).setPrefWidth(230).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,10,4,10)).build().get());


        // "order by" select and listen to changes
        tableHeaderHBox.getChildren().add(new BuilderMFXComboBoxController.Builder("order_by", "Order By", List.of(
                new SelectOption("Code (ascending)", "section_code"),
                new SelectOption("Code (descending)", "-section_code"),
                new SelectOption("Course Code (ascending)", "course__course_code"),
                new SelectOption("Course Code (descending)", "-course__course_code"),
                new SelectOption("Course Name (ascending)", "course__course_name"),
                new SelectOption("Course Name (descending)", "-course__course_name"),
                new SelectOption("Lecturer Username (ascending)", "lecturer__username"),
                new SelectOption("Lecturer Username (descending)", "-lecturer__username"),
                new SelectOption("Lecturer First Name (ascending)", "lecturer__first_name"),
                new SelectOption("Lecturer First Name (descending)", "-lecturer__first_name"),
                new SelectOption("Lecturer Last Name (ascending)", "lecturer__last_name"),
                new SelectOption("Lecturer Last Name (descending)", "-lecturer__last_name")
        )).addSelectionListener((obs, oldSelection, newSelection)->{
            sectionsPaginatedTable.setOrdering(newSelection.getValText());
            sectionsPaginatedTable.refresh(true);
        }).setValText(defaultOrdering).setPrefWidth(230).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,4,4,10)).build().get());

    }

    /***
     * After clicking on the new button, show an empty form on the right for admin to fill in.
     */
    public void sectionNew() {

        sectionDetailVBox.getChildren().clear();
        sectionDetailPane.setVvalue(0.0);
        sectionDeleteBtn.setDisable(true);

        sectionsPaginatedTable.clearSelection();

        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(100));
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setNode(sectionDetailVBox);
        fade.play();

        sectionSaveBtn.setDisable(false);

        sectionDetailVBox.getChildren().add(new Label("Section Information"));
        sectionDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("id","ID").setDisable(true).build().get());

        sectionDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("section_code","Section Code *").build().get());

        sectionDetailVBox.getChildren().add(new BuilderMFXComboBoxController.Builder("course","Course *", courseModel.getSelectOptionList()).build().get());

        sectionDetailVBox.getChildren().add(new BuilderMFXComboBoxController.Builder("lecturer","Lecturer *", userModel.getSelectOptionList("lecturer")).build().get());


    }

    /***
     * After clicking on the save button.
     * 1. if a section is selected, update the selected section with a PUT method to the server.
     * 2. if no section is selected, create a section user with a POST method to the server.
     */
    public void sectionSave() {

        Section selectedSection = sectionsPaginatedTable.getCurrentSelection();

        SelectOption course = ((MFXComboBox<SelectOption>) AppModel.scene.lookup("#course")).getValue();
        SelectOption lecturer = ((MFXComboBox<SelectOption>) AppModel.scene.lookup("#lecturer")).getValue();

        // Updating the existing user and refresh the table
        if(selectedSection != null)
        {


            http.put("/academic/section/" + selectedSection.getId() + "/", Map.of(
                    "section_code",         ((MFXTextField) AppModel.scene.lookup("#section_code")).getText(),
                    "course",   course == null ? "" : course.getValText(),
                    "lecturer",   lecturer == null ? "" : lecturer.getValText()
            ), (JSONObject jsonUser) -> {

                Platform.runLater(() -> {
                    sectionsPaginatedTable.refresh(false);
                });

            });

        }

        // create a new section and refresh the table
        else {

            http.post("/academic/section/", Map.of(
                    "section_code",         ((MFXTextField) AppModel.scene.lookup("#section_code")).getText(),
                    "course",   course == null ? "" : course.getValText(),
                    "lecturer",   lecturer == null ? "" : lecturer.getValText()
            ), (JSONObject jsonUser) -> {

                Platform.runLater(() -> {

                    sectionDetailVBox.getChildren().clear();
                    sectionDetailPane.setVvalue(0.0);
                    sectionDeleteBtn.setDisable(true);
                    sectionSaveBtn.setDisable(true);

                    sectionsPaginatedTable.refresh(false);
                });
            });
        }

    }


    /***
     * After clicking the delete button, a confirmation box is shown, with the option to delete the section permanently.
     */
    public void sectionDelete() {

        Section selectedSection = sectionsPaginatedTable.getCurrentSelection();

        if(selectedSection != null)
        {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete confirmation");
            alert.setHeaderText("Are you sure to permanently delete the section " + selectedSection.getCourse() + "-" + selectedSection.getCode() + "?");
            alert.setContentText("The following section will be permanently deleted: " + "\nCourse: " + selectedSection.getCourse() + "\nCode: " + selectedSection.getCode() + "\nLecturer: " + selectedSection.getLecturer());
            alert.initOwner(AppModel.stage);
            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent())
            {
                System.err.println("Dialog exited");
            }
            else if(result.get() == ButtonType.OK)
            {
                System.err.println("Dialog OK pressed");

                http.delete("/academic/section/"+selectedSection.getId()+"/", (JSONObject json) -> {

                    Platform.runLater(() -> {
                        sectionsPaginatedTable.refresh(false);
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
