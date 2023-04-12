package com.siweb.controller;

import com.siweb.model.AppModel;
import com.siweb.model.Enrollment;
import com.siweb.view.SelectOption;
import com.siweb.view.builder.BuilderMFXComboBox;
import com.siweb.view.builder.BuilderMFXTextField;
import com.siweb.view.facade.FacadePaginatedTable;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

/***
 * AdminEnrollmentController manages the users management page of admin
 */
public class AdminEnrollmentController extends BaseController {

    private FacadePaginatedTable<Enrollment> enrollmentsPaginatedTable;
    @FXML
    private TableView<Enrollment> enrollmentsTable;
    @FXML
    private Pagination enrollmentsTablePagination;
    @FXML
    private VBox enrollmentDetailVBox;
    @FXML
    private MFXScrollPane enrollmentDetailPane;

    @FXML
    private MFXButton enrollmentDeleteBtn;
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

        // request dependency before setting up... (to show data in the dropdown list. e.g. we need a list of semesters to be selected when adding / editing enrollments.)
        http.get("/academic/section/list_names/",(JSONArray res) ->{
            sectionModel.add(res, false);
            http.get("/academic/course/list_names/",(JSONArray res2) -> {
                courseModel.add(res2, false);
                http.get("/user/list_names/", (JSONArray res3) -> {
                    userModel.add(res3, false);
                    Platform.runLater(this::setup);
                });
            });
        });
    }
    public void setup() {

        // default ordering, will be used in TableView and the ordering select
        String defaultOrdering = "-section__section_code,section__course__course_code";

        // Create a new Facade class to easily manage the tableView with pagination
        enrollmentsPaginatedTable = new FacadePaginatedTable.Builder<Enrollment>(enrollmentModel, enrollmentsTable, enrollmentsTablePagination, "/academic/enrollment/", "#resultsCountLabel")
                .addColumn(new TableColumn<Enrollment, String>("Student"), "getUser", 250)
                .addColumn(new TableColumn<Enrollment, String>("Section"), "getSection", 190)
                .addColumn(new TableColumn<Enrollment, String>("Final Grade"), "getFinalGrade", 90)
                .addColumn(new TableColumn<Enrollment, String>("Lecturer"), "getSectionLecturerFullName", 190)
                .setPageSize(23)
                .setOrdering(defaultOrdering)
                .build();

        // Add a listener when select / deselect a row
        enrollmentsPaginatedTable.addSelectionListener((obs, oldSelection, newSelection) -> {

            // disable the buttons and clear the detail box first, then enable them accordingly if needed below
            enrollmentDetailVBox.getChildren().clear();
            enrollmentDetailPane.setVvalue(0.0);
            enrollmentDeleteBtn.setDisable(true);
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

                // enable delete button
                enrollmentDeleteBtn.setDisable(false);

                // show basic information
                enrollmentDetailVBox.getChildren().add(new Label("Enrollment Information"));
                enrollmentDetailVBox.getChildren().add(new BuilderMFXTextField.Builder("id","ID").setText(newSelection.getId() + "").setDisable(true).build().get());

                enrollmentDetailVBox.getChildren().add(new BuilderMFXComboBox.Builder("user","Student *", userModel.getSelectOptionList("student")).setIsFiltered(true).setValText(newSelection.getUser()).build().get());

                enrollmentDetailVBox.getChildren().add(new BuilderMFXTextField.Builder("final_grade","Final Grade").setText(newSelection.getFinalGrade()).build().get());

                enrollmentDetailVBox.getChildren().add(new BuilderMFXComboBox.Builder("section","Section *", sectionModel.getSelectOptionList()).setIsFiltered(true).setValText(newSelection.getSection()).build().get());


            }
        });


        // "search" button creation and listen to "ENTER" presses
        tableHeaderHBox.getChildren().add(new BuilderMFXTextField.Builder("search", "Search").setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                enrollmentsPaginatedTable.setSearch(((MFXTextField) AppModel.scene.lookup("#search")).getText());
                enrollmentsPaginatedTable.refresh(true);
            }
        }).setPrefWidth(230).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,10,4,10)).build().get());


        // "order by" select and listen to changes
        tableHeaderHBox.getChildren().add(new BuilderMFXComboBox.Builder("order_by", "Order By", List.of(
                new SelectOption("Default", "-section__section_code,section__course__course_code"),
                new SelectOption("Section (ascending)", "section__section_code"),
                new SelectOption("Section (descending)", "-section__section_code"),
                new SelectOption("Course Code (ascending)", "section__course__course_code"),
                new SelectOption("Course Code (descending)", "-section__course__course_code"),
                new SelectOption("Course Name (ascending)", "section__course__course_name"),
                new SelectOption("Course Name (descending)", "-section__course__course_name"),
                new SelectOption("Final Grade (ascending)", "final_grade"),
                new SelectOption("Final Grade (descending)", "-final_grade"),
                new SelectOption("Student Username (ascending)", "user__username"),
                new SelectOption("Student Username (descending)", "-user__username"),
                new SelectOption("Student First Name (ascending)", "user__first_name"),
                new SelectOption("Student First Name (descending)", "-user__first_name"),
                new SelectOption("Student Last Name (ascending)", "user__last_name"),
                new SelectOption("Student Last Name (descending)", "-user__last_name"),
                new SelectOption("Lecturer Username (ascending)", "section__lecturer__username"),
                new SelectOption("Lecturer Username (descending)", "-section__lecturer__username"),
                new SelectOption("Lecturer First Name (ascending)", "section__lecturer__first_name"),
                new SelectOption("Lecturer First Name (descending)", "-section__lecturer__first_name"),
                new SelectOption("Lecturer Last Name (ascending)", "section__lecturer__last_name"),
                new SelectOption("Lecturer Last Name (descending)", "-section__lecturer__last_name")
        )).addSelectionListener((obs, oldSelection, newSelection)->{
            enrollmentsPaginatedTable.setOrdering(newSelection.getValText());
            enrollmentsPaginatedTable.refresh(true);
        }).setValText(defaultOrdering).setPrefWidth(280).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,4,4,10)).build().get());

    }

    /***
     * After clicking on the new button, show an empty form on the right for admin to fill in.
     */
    public void enrollmentNew() {

        enrollmentDetailVBox.getChildren().clear();
        enrollmentDetailPane.setVvalue(0.0);
        enrollmentDeleteBtn.setDisable(true);

        enrollmentsPaginatedTable.clearSelection();

        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(100));
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setNode(enrollmentDetailVBox);
        fade.play();

        enrollmentSaveBtn.setDisable(false);

        enrollmentDetailVBox.getChildren().add(new Label("Enrollment Information"));
        enrollmentDetailVBox.getChildren().add(new BuilderMFXTextField.Builder("id","ID").setDisable(true).build().get());

        enrollmentDetailVBox.getChildren().add(new BuilderMFXComboBox.Builder("user","Student *", userModel.getSelectOptionList("student")).setIsFiltered(true).build().get());

        enrollmentDetailVBox.getChildren().add(new BuilderMFXTextField.Builder("final_grade","Final Grade").build().get());

        enrollmentDetailVBox.getChildren().add(new BuilderMFXComboBox.Builder("section","Section *", sectionModel.getSelectOptionList()).setIsFiltered(true).build().get());


    }

    /***
     * After clicking on the save button.
     * 1. if a enrollment is selected, update the selected enrollment with a PUT method to the server.
     * 2. if no enrollment is selected, create a enrollment user with a POST method to the server.
     */
    public void enrollmentSave() {

        Enrollment selectedEnrollment = enrollmentsPaginatedTable.getCurrentSelection();

        SelectOption section = ((MFXComboBox<SelectOption>) AppModel.scene.lookup("#section")).getValue();
        SelectOption user = ((MFXComboBox<SelectOption>) AppModel.scene.lookup("#user")).getValue();


        // Updating the existing user and refresh the table
        if(selectedEnrollment != null)
        {


            http.put("/academic/enrollment/" + selectedEnrollment.getId() + "/", Map.of(
                    "final_grade",         ((MFXTextField) AppModel.scene.lookup("#final_grade")).getText(),
                    "section",   section == null ? "" : section.getValText(),
                    "user",   user == null ? "" : user.getValText()
            ), (JSONObject jsonUser) -> {

                Platform.runLater(() -> {
                    enrollmentsPaginatedTable.refresh(false);
                });

            });

        }

        // create a new enrollment and refresh the table
        else {

            http.post("/academic/enrollment/", Map.of(
                    "final_grade",         ((MFXTextField) AppModel.scene.lookup("#final_grade")).getText(),
                    "section",   section == null ? "" : section.getValText(),
                    "user",   user == null ? "" : user.getValText()
            ), (JSONObject jsonUser) -> {

                Platform.runLater(() -> {

                    enrollmentDetailVBox.getChildren().clear();
                    enrollmentDetailPane.setVvalue(0.0);
                    enrollmentDeleteBtn.setDisable(true);
                    enrollmentSaveBtn.setDisable(true);

                    enrollmentsPaginatedTable.refresh(false);
                });
            });
        }

    }


    /***
     * After clicking the delete button, a confirmation box is shown, with the option to delete the enrollment permanently.
     */
    public void enrollmentDelete() {

        Enrollment selectedEnrollment = enrollmentsPaginatedTable.getCurrentSelection();

        if(selectedEnrollment != null)
        {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete confirmation");
            alert.setHeaderText("Are you sure to permanently delete the enrollment " + selectedEnrollment.getSection() + " for " + selectedEnrollment.getUser() + "?");
            alert.setContentText("The following enrollment will be permanently deleted: " + "\nSection: " + selectedEnrollment.getSection() + "\nStudent: " + selectedEnrollment.getUser() + "\nFinal Grade: " + selectedEnrollment.getFinalGrade());
            alert.initOwner(AppModel.stage);
            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent())
            {
                System.err.println("Dialog exited");
            }
            else if(result.get() == ButtonType.OK)
            {
                System.err.println("Dialog OK pressed");

                http.delete("/academic/enrollment/"+selectedEnrollment.getId()+"/", (JSONObject json) -> {

                    Platform.runLater(() -> {
                        enrollmentsPaginatedTable.refresh(false);
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
