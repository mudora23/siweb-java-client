package com.siweb.view.facade;

import com.siweb.controller.utility.UtilityHttpController;
import com.siweb.model.AppModel;
import com.siweb.model.ObservableModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.json.JSONObject;

/***
 * FacadePaginatedTable provides an easy way to use TableView and Pagination with our RESTful API with server-side pagination, sorting, and filtering.
 * @param <S> The data model, ex. User
 */
public class FacadePaginatedTable<S> {

    protected final UtilityHttpController http = UtilityHttpController.getInstance();

    private final TableView<S> tableView;
    private final Pagination pagination;
    private final String apiEndPoint;
    private final int pageSize;
    private String ordering = "";
    private String search = "";
    private final String resultsCountLabelId;
    private ObservableModel<S> observableModel;

    private int previousPage = -1;

    /***
     * A Builder to create our Paginated Table
     * @param <S> The data model, ex. User
     */
    public static class Builder<S> {

        protected final UtilityHttpController http = UtilityHttpController.getInstance();
        private final TableView<S> tableView;
        private final Pagination pagination;
        private final String apiEndPoint;
        private int pageSize = 20;
        private String ordering = "";
        private String resultsCountLabelId;
        private ObservableModel<S> observableModel;

        /***
         * Construct the required params in the builder
         * @param observableModel An observable list of data to link to the tableView
         * @param tableView basic tableView created in FXML
         * @param pagination basic pagination created in FXML
         * @param apiEndPoint API End point for listing data with server-side paging, filtering, and ordering support.
         */
        public Builder(ObservableModel<S> observableModel, TableView<S> tableView, Pagination pagination, String apiEndPoint, String resultsCountLabelId) {
            this.tableView = tableView;
            this.pagination = pagination;
            this.apiEndPoint = apiEndPoint;
            this.observableModel = observableModel;
            this.resultsCountLabelId = resultsCountLabelId;

            // Link the tableview item list to the observable list.
            this.tableView.setItems(observableModel.getObsList());

        }

        /***
         * Add a column to the table
         * @param tableColumn A TableColumn for the TableView
         * @param methodName  The Method name to be bound
         * @param prefWidth The preferred width of the column
         * @return Builder
         * @param <T> Column data type
         */
        public <T> Builder<S> addColumn(TableColumn<S, T> tableColumn, String methodName, double prefWidth)
        {
            tableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> (T) ((S) cellData.getValue()).getClass().getMethod(methodName).invoke(cellData.getValue())));
            tableColumn.setSortable(false); // no sorting can be done on this client side.
            if(prefWidth > 0.0) tableColumn.setPrefWidth(prefWidth);
            tableView.getColumns().add(tableColumn);
            return this;
        }

        public Builder<S> setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder<S> setOrdering(String ordering) {
            this.ordering = ordering;
            return this;
        }

        public Builder<S> setResultsCountSelector(String resultsCountLabelId) {
            this.resultsCountLabelId = resultsCountLabelId;
            return this;
        }

        public FacadePaginatedTable<S> build() {
            return new FacadePaginatedTable<S>(this);
        }

    }

    private FacadePaginatedTable(Builder<S> builder) {
        this.tableView = builder.tableView;
        this.pagination = builder.pagination;
        this.apiEndPoint = builder.apiEndPoint;
        this.pageSize = builder.pageSize;
        this.ordering = builder.ordering;
        this.observableModel = builder.observableModel;
        this.resultsCountLabelId = builder.resultsCountLabelId;


        // Set row height
        tableView.setRowFactory(tv -> {
            javafx.scene.control.TableRow<S> row = new javafx.scene.control.TableRow<>();
            row.setPrefHeight((double) 640 / this.pageSize);
            return row;
        });

        // Update table when changing pages
        pagination.setPageFactory(pageIndex -> {

            // javafx calls setPageFactory() 2 times in the beginning in order to prepare for pages, and also multiple times when returning null.
            // this is a hotfix to prevent our table refreshed multiple times unexpectedly.
            if(pageIndex != this.previousPage)
            {
                this.previousPage = pageIndex;
                this.refresh(true);
            }
            return new Label();

        });
    }


    public void addSelectionListener(ChangeListener<? super S> listener) {
        this.tableView.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    public S getCurrentSelection() {
        return this.tableView.getSelectionModel().selectedItemProperty().get();
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void clearSelection() {
        this.tableView.getSelectionModel().clearSelection();
    }

    /***
     * Refresh the table, request the updated information from the server and store the results in model again.
     * @param isSelectFirstAfter after refreshing, either reselect the first user of the table or the user with the previous selected index
     */
    public void refresh(boolean isSelectFirstAfter) {

        http.get(apiEndPoint + "?limit="+pageSize+"&offset="+pagination.getCurrentPageIndex()*pageSize + "&ordering="+this.ordering+"&search="+java.net.URLEncoder.encode(this.search), (JSONObject res) -> {

            Platform.runLater(() -> {
                pagination.setPageCount((int) Math.ceil((double) res.getInt("count") / pageSize));

                int selectedIndex = tableView.getSelectionModel().getSelectedIndex();

                observableModel.clearObsList();
                observableModel.add(res.getJSONArray("results"), true);

                if(!resultsCountLabelId.isEmpty()) {
                    int currentResults = res.getJSONArray("results").length();
                    int offset = pagination.getCurrentPageIndex()*pageSize;

                    ((Label) AppModel.scene.lookup(resultsCountLabelId)).setText(String.format("Showing %d - %d of %d results", offset + Math.min(currentResults,1), offset + currentResults, res.getInt("count")));

                    // after refreshing, we reselect the first user of the table or the user with the previous selected index automatically
                    if (currentResults > 0)
                    {
                        if(selectedIndex > 0 && !isSelectFirstAfter){
                            tableView.getSelectionModel().select(selectedIndex);
                        }
                        else {
                            tableView.getSelectionModel().selectFirst();
                        }
                    }
                }

            });

        });


    }

}
