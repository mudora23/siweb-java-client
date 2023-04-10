package com.siweb.controller;

import com.siweb.model.AppModel;
import com.siweb.model.Course;
import com.siweb.model.Semester;
import com.siweb.model.SemesterModel;
import com.siweb.view.SelectOption;
import com.siweb.view.builder.BuilderMFXComboBoxController;
import com.siweb.view.builder.BuilderMFXDatePickerController;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

/***
 * AdminCourseController manages the users management page of admin
 */
public class AdminCourseController extends BaseController {

    private FacadePaginatedTableController<Course> coursesPaginatedTable;
    @FXML
    private TableView<Course> coursesTable;
    @FXML
    private Pagination coursesTablePagination;
    @FXML
    private VBox courseDetailVBox;
    @FXML
    private MFXScrollPane courseDetailPane;

    @FXML
    private MFXButton courseDeleteBtn;
    @FXML
    private MFXButton courseSaveBtn;

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

        // request dependency before setting up... (to show data in the dropdown list. e.g. we need a list of semesters to be selected when adding / editing courses.)
        http.get("/academic/semester/list_names/",(JSONArray res) ->{
            semesterModel.add(res, false);
            Platform.runLater(this::setup);
        });
    }
    public void setup() {

        // default ordering, will be used in TableView and the ordering select
        String defaultOrdering = "course_code";

        // Create a new Facade class to easily manage the tableView with pagination
        coursesPaginatedTable = new FacadePaginatedTableController.Builder<Course>(courseModel, coursesTable, coursesTablePagination, "/academic/course/", "#resultsCountLabel")
                .addColumn(new TableColumn<Course, String>("Code"), "getCode", 160)
                .addColumn(new TableColumn<Course, String>("Name"), "getName", 300)
                .addColumn(new TableColumn<Course, String>("Credit"), "getCredit", 120)
                .addColumn(new TableColumn<Course, String>("Semester"), "getSemester", 160)
                .setPageSize(23)
                .setOrdering(defaultOrdering)
                .build();

        // Add a listener when select / deselect a row
        coursesPaginatedTable.addSelectionListener((obs, oldSelection, newSelection) -> {

            // disable the buttons and clear the detail box first, then enable them accordingly if needed below
            courseDetailVBox.getChildren().clear();
            courseDetailPane.setVvalue(0.0);
            courseDeleteBtn.setDisable(true);
            courseSaveBtn.setDisable(true);

            // create a basic fade transition on the detail box
            FadeTransition fade = new FadeTransition();
            fade.setDuration(Duration.millis(100));
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.setNode(courseDetailVBox);
            fade.play();

            // if a user is selected, show the user's details and enable save / delete button.
            if (newSelection != null) {

                // enable save button
                courseSaveBtn.setDisable(false);

                // enable delete button
                courseDeleteBtn.setDisable(false);

                // show basic information
                courseDetailVBox.getChildren().add(new Label("Course Information"));
                courseDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("id","ID").setText(newSelection.getId() + "").setDisable(true).build().get());
                courseDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("code","Code *").setText(newSelection.getCode()).build().get());
                courseDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("name","Name *").setText(newSelection.getName()).build().get());
                courseDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("credit", "Credit *").setText(newSelection.getCredit()).build().get());
                courseDetailVBox.getChildren().add(new BuilderMFXComboBoxController.Builder("semester","Semester *", semesterModel.getSelectOptionList()).setValText(newSelection.getSemester()).build().get());

            }
        });


        // "search" button creation and listen to "ENTER" presses
        tableHeaderHBox.getChildren().add(new BuilderMFXTextFieldController.Builder("search", "Search").setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                coursesPaginatedTable.setSearch(((MFXTextField) AppModel.scene.lookup("#search")).getText());
                coursesPaginatedTable.refresh(true);
            }
        }).setPrefWidth(230).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,10,4,10)).build().get());


        // "order by" select and listen to changes
        tableHeaderHBox.getChildren().add(new BuilderMFXComboBoxController.Builder("order_by", "Order By", List.of(
                new SelectOption("Code (ascending)", "course_code"),
                new SelectOption("Code (descending)", "-course_code"),
                new SelectOption("Name (ascending)", "course_name"),
                new SelectOption("Name (descending)", "-course_name"),
                new SelectOption("Credit (ascending)", "course_credit"),
                new SelectOption("Credit (descending)", "-course_credit"),
                new SelectOption("Semester Year (ascending)", "course_semester__year"),
                new SelectOption("Semester Year (descending)", "-course_semester__year")
        )).addSelectionListener((obs, oldSelection, newSelection)->{
            coursesPaginatedTable.setOrdering(newSelection.getValText());
            coursesPaginatedTable.refresh(true);
        }).setValText(defaultOrdering).setPrefWidth(230).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,4,4,10)).build().get());

    }

    /***
     * After clicking on the new button, show an empty form on the right for admin to fill in.
     */
    public void courseNew() {

        courseDetailVBox.getChildren().clear();
        courseDetailPane.setVvalue(0.0);
        courseDeleteBtn.setDisable(true);

        coursesPaginatedTable.clearSelection();

        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(100));
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setNode(courseDetailVBox);
        fade.play();

        courseSaveBtn.setDisable(false);

        courseDetailVBox.getChildren().add(new Label("Course Information"));
        courseDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("id","ID").setDisable(true).build().get());
        courseDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("code","Code *").build().get());
        courseDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("name","Name *").build().get());
        courseDetailVBox.getChildren().add(new BuilderMFXTextFieldController.Builder("credit", "Credit *").build().get());
        courseDetailVBox.getChildren().add(new BuilderMFXComboBoxController.Builder("semester","Semester *", semesterModel.getSelectOptionList()).build().get());


    }

    /***
     * After clicking on the save button.
     * 1. if a course is selected, update the selected course with a PUT method to the server.
     * 2. if no course is selected, create a course user with a POST method to the server.
     */
    public void courseSave() {

        Course selectedCourse = coursesPaginatedTable.getCurrentSelection();

        SelectOption semester = ((MFXComboBox<SelectOption>) AppModel.scene.lookup("#semester")).getValue();

        // Updating the existing user and refresh the table
        if(selectedCourse != null)
        {


            http.put("/academic/course/" + selectedCourse.getId() + "/", Map.of(
                    "course_code",         ((MFXTextField) AppModel.scene.lookup("#code")).getText(),
                    "course_name",         ((MFXTextField) AppModel.scene.lookup("#name")).getText(),
                    "course_credit",     ((MFXTextField) AppModel.scene.lookup("#credit")).getText(),
                    "course_semester",   semester == null ? "" : semester.getValText()
            ), (JSONObject jsonUser) -> {

                Platform.runLater(() -> {
                    coursesPaginatedTable.refresh(false);
                });

            });

        }

        // create a new course and refresh the table
        else {

            http.post("/academic/course/", Map.of(
                    "course_code",         ((MFXTextField) AppModel.scene.lookup("#code")).getText(),
                    "course_name",         ((MFXTextField) AppModel.scene.lookup("#name")).getText(),
                    "course_credit",     ((MFXTextField) AppModel.scene.lookup("#credit")).getText(),
                    "course_semester",   semester == null ? "" : semester.getValText()
            ), (JSONObject jsonUser) -> {

                Platform.runLater(() -> {

                    courseDetailVBox.getChildren().clear();
                    courseDetailPane.setVvalue(0.0);
                    courseDeleteBtn.setDisable(true);
                    courseSaveBtn.setDisable(true);

                    coursesPaginatedTable.refresh(false);
                });
            });
        }

    }


    /***
     * After clicking the delete button, a confirmation box is shown, with the option to delete the course permanently.
     */
    public void courseDelete() {

        Course selectedCourse = coursesPaginatedTable.getCurrentSelection();

        if(selectedCourse != null)
        {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete confirmation");
            alert.setHeaderText("Are you sure to permanently delete the course " + selectedCourse.getCode() + "-" + selectedCourse.getSemester() + "?");
            alert.setContentText("The following course will be permanently deleted: " + "\nCode: " + selectedCourse.getCode() + "\nName: " + selectedCourse.getName() + "-" + selectedCourse.getSemester() + "\nCredit: " + selectedCourse.getCredit());
            alert.initOwner(AppModel.stage);
            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent())
            {
                System.err.println("Dialog exited");
            }
            else if(result.get() == ButtonType.OK)
            {
                System.err.println("Dialog OK pressed");

                http.delete("/academic/course/"+selectedCourse.getId()+"/", (JSONObject json) -> {

                    Platform.runLater(() -> {
                        coursesPaginatedTable.refresh(false);
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
