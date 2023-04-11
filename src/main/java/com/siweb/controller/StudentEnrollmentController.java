package com.siweb.controller;

import com.siweb.model.AppModel;
import com.siweb.model.Course;
import com.siweb.model.Enrollment;
import com.siweb.view.SelectOption;
import com.siweb.view.builder.BuilderMFXComboBoxController;
import com.siweb.view.builder.BuilderMFXTextFieldController;
import com.siweb.view.facade.FacadePaginatedTableController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONArray;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentEnrollmentController extends BaseController{
    @FXML
    private FacadePaginatedTableController<Enrollment> enrollmentPaginatedTable;

    @FXML
    private TableView<Enrollment> enrollmentsTable;

    @FXML
    private Pagination enrollmentTablePagination;

    @FXML
    private Label resultsCountLabel;

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
        setup();

    }
    public void setup() {

        // default ordering, will be used in TableView and the ordering select
        String defaultOrdering = "-section__course__course_code";

        // Create a new Facade class to easily manage the tableView with pagination
        enrollmentPaginatedTable = new FacadePaginatedTableController.Builder<>(enrollmentModel, enrollmentsTable, enrollmentTablePagination, "/academic/enrollment/", "#resultsCountLabel")
                .addColumn(new TableColumn<Enrollment, String>("Section Code"), "getSectionCode", 160)
                .addColumn(new TableColumn<Enrollment, String>("Course Code"), "getCourseCode", 160)
                .addColumn(new TableColumn<Enrollment, String>("Course Name"), "getCourseName", 300)
                .addColumn(new TableColumn<Enrollment, String>("Lecture Name"), "getLectureFullName", 120)
                .addColumn(new TableColumn<Enrollment, String>("Final Grade"), "getFinalGrade", 160)
                .setPageSize(23)
                .setOrdering(defaultOrdering)
                .build();


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
}
