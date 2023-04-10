package com.siweb.controller;

import com.siweb.model.AppModel;
import com.siweb.model.Semester;
import com.siweb.model.User;
import com.siweb.model.UserModel;
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
import org.json.JSONObject;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

/***
 * AdminSemesterController manages the users management page of admin
 */
public class StudentSemesterController extends BaseController {

    private FacadePaginatedTableController<Semester> semestersPaginatedTable;
    @FXML
    private TableView<Semester> semestersTable;
    @FXML
    private Pagination semestersTablePagination;
    @FXML
    private VBox semesterDetailVBox;
    @FXML
    private MFXScrollPane semesterDetailPane;

    @FXML
    private MFXButton semesterDeleteBtn;
    @FXML
    private MFXButton semesterSaveBtn;

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

        // default ordering, will be used in TableView and the ordering select
        String defaultOrdering = "-date_end";

        // Create a new Facade class to easily manage the tableView with pagination
        semestersPaginatedTable = new FacadePaginatedTableController.Builder<Semester>(semesterModel, semestersTable, semestersTablePagination, "/academic/semester/", "#resultsCountLabel")
                .addColumn(new TableColumn<Semester, String>("Year"), "getYear", 160)
                .addColumn(new TableColumn<Semester, String>("Semester"), "getSemester", 160)
                .addColumn(new TableColumn<Semester, String>("Date Start"), "getDateStart", 160)
                .addColumn(new TableColumn<Semester, String>("Date End"), "getDateEnd", 160)
                .setPageSize(23)
                .setOrdering(defaultOrdering)
                .build();

        // Add a listener when select / deselect a row

        // "search" button creation and listen to "ENTER" presses
        tableHeaderHBox.getChildren().add(new BuilderMFXTextFieldController.Builder("search", "Search").setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                semestersPaginatedTable.setSearch(((MFXTextField) AppModel.scene.lookup("#search")).getText());
                semestersPaginatedTable.refresh(true);
            }
        }).setPrefWidth(230).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,10,4,10)).build().get());



        // "order by" select and listen to changes
        tableHeaderHBox.getChildren().add(new BuilderMFXComboBoxController.Builder("order_by", "Order By", List.of(
                new SelectOption("Date end (ascending)", "date_end"),
                new SelectOption("Date end (descending)", "-date_end"),
                new SelectOption("Date start (ascending)", "date_start"),
                new SelectOption("Date start (descending)", "-date_start"),
                new SelectOption("Semester (ascending)", "semester"),
                new SelectOption("Semester (descending)", "-semester"),
                new SelectOption("Year (ascending)", "year"),
                new SelectOption("Year (descending)", "-year")
        )).addSelectionListener((obs, oldSelection, newSelection)->{
            semestersPaginatedTable.setOrdering(newSelection.getValText());
            semestersPaginatedTable.refresh(true);
        }).setValText(defaultOrdering).setPrefWidth(230).setFloatMode(FloatMode.INLINE).setPadding(new Insets(4,4,4,10)).build().get());

    }

    /***
     * After clicking on the new button, show an empty form on the right for admin to fill in.
     */


    /***
     * After clicking on the save button.
     * 1. if a semester is selected, update the selected semester with a PUT method to the server.
     * 2. if no semester is selected, create a semester user with a POST method to the server.
     */

}
