package com.siweb.controller;

import com.siweb.model.*;
import com.siweb.view.SelectOption;
import com.siweb.view.builder.BuilderMFXComboBoxController;
import com.siweb.view.builder.BuilderMFXTextFieldController;
import com.siweb.view.facade.FacadePaginatedTableController;
import io.github.palexdev.materialfx.controls.*;
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
import org.json.JSONObject;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/***
 * AdminCourseController manages the users management page of admin
 */
public class LecturerEnrollmentController extends BaseController {

    private FacadePaginatedTableController<Enrollment> enrollmentPaginatedTable;
    @FXML
    private TableView<Enrollment> enrollmentTable;
    @FXML
    private Pagination enrollmentTablePagination;
    @FXML
    private VBox enrollmentDetailVBox;
    @FXML
    private MFXScrollPane enrollmentDetailPane;

    @FXML
    private MFXButton enrollmentSaveBtn;

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

        setup();
    }
    public void setup() {

        // default ordering, will be used in TableView and the ordering select
        String defaultOrdering = "course_code";

        // Create a new Facade class to easily manage the tableView with pagination
        enrollmentPaginatedTable = new FacadePaginatedTableController.Builder<Enrollment>(enrollmentModel, enrollmentTable, enrollmentTablePagination, "/academic/enrollment/", "#resultsCountLabel")
                .addColumn(new TableColumn<Enrollment, String>("Section Code"), "getSectionCode", 100)
                .addColumn(new TableColumn<Enrollment, String>("Course Code"), "getCourseCode", 100)
                .addColumn(new TableColumn<Enrollment, String>("Course Name"), "getCourseName", 200)
                .addColumn(new TableColumn<Enrollment, String>("Student ID"), "getStudentUserName", 90)
                .addColumn(new TableColumn<Enrollment, String>("Student Name"), "getStudentFullName", 150)
                .addColumn(new TableColumn<Enrollment, String>("Final Grade"), "getFinalGrade", 100)
                .setPageSize(23)
                .setOrdering(defaultOrdering)
                .build();

        // Add a listener when select / deselect a row
        enrollmentPaginatedTable.addSelectionListener((obs, oldSelection, newSelection) -> {

            // disable the buttons and clear the detail box first, then enable them accordingly if needed below
            enrollmentDetailVBox.getChildren().clear();
            enrollmentDetailPane.setVvalue(0.0);
            enrollmentSaveBtn.setDisable(true);

            // create a basic fade transition on the detail box
            FadeTransition fade = new FadeTransition();
            fade.setDuration(Duration.millis(100));
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.setNode(enrollmentDetailVBox);
            fade.play();

            // if a user is selected, show the user's details and enable save / delete button.
            if (newSelection != null) {

                // enable save button
                enrollmentSaveBtn.setDisable(false);


                // show basic information
                enrollmentDetailVBox.getChildren().add(new Label("Enrollment Information"));
                enrollmentDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("section code","Section Code").setText(newSelection.getSectionCode()).setDisable(true).build().get());
                enrollmentDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("course code","Course Code").setText(newSelection.getCourseCode()).setDisable(true).build().get());
                enrollmentDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("course name","Course Name").setText(newSelection.getCourseName()).setDisable(true).build().get());
                enrollmentDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("student ID","Student username").setText(newSelection.getStudentUserName()).setDisable(true).build().get());
                enrollmentDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("student name","Student Name").setText(newSelection.getStudentFullName()).setDisable(true).build().get());
                enrollmentDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("final grade","Final Grade").setText(newSelection.getFinalGrade()).build().get());

            }
        });


        // "search" button creation and listen to "ENTER" presses
        tableHeaderHBox.getChildren().add(new BuilderMFXTextFieldController.Builder("search", "Search").setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                enrollmentPaginatedTable.setSearch(((MFXTextField) AppModel.scene.lookup("#search")).getText());
                enrollmentPaginatedTable.refresh(true);
            }
        }).setPrefWidth(230).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,10,4,10)).build().get());


        // "order by" select and listen to changes
        tableHeaderHBox.getChildren().add(new BuilderMFXComboBoxController.Builder("order_by", "Order By", List.of(
                new SelectOption("Section Code (ascending)", "section__section_code"),
                new SelectOption("Section Code (descending)", "-section__section_code"),
                new SelectOption("Course Code (ascending)", "section__course__course_code"),
                new SelectOption("Course Code (descending)", "-section__course__course_code"),
                new SelectOption("Course Name (ascending)", "section__course__course_name"),
                new SelectOption("Course Name (descending)", "-section__course__course_name"),
                new SelectOption("final grade (ascending)", "final_grade"),
                new SelectOption("final grade (descending)", "-final_grade")

        )).addSelectionListener((obs, oldSelection, newSelection)->{
            enrollmentPaginatedTable.setOrdering(newSelection.getValText());
            enrollmentPaginatedTable.refresh(true);
        }).setValText(defaultOrdering).setPrefWidth(230).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,4,4,10)).build().get());

    }

    /***
     * After clicking on the new button, show an empty form on the right for admin to fill in.
     */

    public void enrollmentSave() {

        Enrollment selectedEnrollment = enrollmentPaginatedTable.getCurrentSelection();

        SelectOption enrollment = ((MFXComboBox<SelectOption>) AppModel.scene.lookup("#final_grae")).getValue();

        // Updating the existing user and refresh the table
        if(selectedEnrollment != null)
        {


            http.put("/academic/enrollment/" + selectedEnrollment.getId() + "/", Map.of(
                    "final_grade", ((MFXTextField) AppModel.scene.lookup("#final_grade")).getText()
            ), (JSONObject jsonUser) -> {

                Platform.runLater(() -> {
                    enrollmentPaginatedTable.refresh(false);
                });

            });

        }


    }

}
