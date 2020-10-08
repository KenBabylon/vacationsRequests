package ug.grupo2.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import ug.grupo2.project.Classes.AppSettings;
import ug.grupo2.project.Classes.DataStore;
import ug.grupo2.project.Classes.Personal;
import ug.grupo2.project.enums.RequestStatus;
import ug.grupo2.project.enums.AccessLevel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.ChoiceBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RequestsController implements Initializable {

  double x, y;
  boolean changeHasMade = false;
  DataStore dataStore = DataStore.getInstance();
  AppSettings appSettings = dataStore.getAppSettings();
  Personal user = dataStore.getUser();
  boolean userIsAdmin = user.getAccessLevel().equals(AccessLevel.Admin);
  List<ug.grupo2.project.Classes.VacationRequest> dataRequest = userIsAdmin ? dataStore.getRequests()
      : dataStore.getUserRequest(user.getName());
  ObservableList<VacationRequest> tableOriginalRequests = FXCollections.observableArrayList();
  ObservableList<VacationRequest> tableRequests = FXCollections.observableArrayList();

  @FXML
  private JFXTreeTableView<VacationRequest> dataTable;
  @FXML
  private StackPane rootPane;
  @FXML
  private Button closeButton;
  @FXML
  private JFXButton addButton;
  @FXML
  private JFXButton saveChangeButton;
  @FXML
  private JFXButton discardChangeButton;
  @FXML
  private AnchorPane appBar;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    System.out.println(dataRequest.size());
    saveChangeButton.setDisable(true);
    discardChangeButton.setDisable(true);

    // Dynamic styles
    rootPane.setStyle("-fx-pallete-background: " + appSettings.getRelativePallete().getBackground()
        + "; -fx-secondary: " + appSettings.getRelativePallete().getSecondary().getMain() + "; -fx-primarytext: "
        + appSettings.getRelativePallete().getPrimary().getContrastText() + "; -fx-secondarytext: "
        + appSettings.getRelativePallete().getSecondary().getContrastText());
    dataTable.setStyle("-fx-tree-table-color: " + appSettings.getRelativePallete().getSecondary().getMain());
    appBar.setStyle("-fx-background-color: " + appSettings.getRelativePallete().getPrimary().getMain());
    addButton.setStyle("-fx-text-fill: " + appSettings.getRelativePallete().getPrimary().getContrastText());

    // Logic
    int ColumnsWidth = 700 / 6 - 5;

    JFXTreeTableColumn<VacationRequest, Date> dateFromName = new JFXTreeTableColumn<>("Date From");
    JFXTreeTableColumn<VacationRequest, Date> dateToName = new JFXTreeTableColumn<>("Date To");
    JFXTreeTableColumn<VacationRequest, String> reasonName = new JFXTreeTableColumn<>("Reason");
    JFXTreeTableColumn<VacationRequest, String> userName = new JFXTreeTableColumn<>("Name");
    JFXTreeTableColumn<VacationRequest, String> requestStatusName = new JFXTreeTableColumn<>("Status");
    JFXTreeTableColumn<VacationRequest, VacationRequest> deleteRequest = new JFXTreeTableColumn<>("Action");
    dateFromName.setPrefWidth(ColumnsWidth);
    dateToName.setPrefWidth(ColumnsWidth);
    reasonName.setPrefWidth(ColumnsWidth);
    userName.setPrefWidth(ColumnsWidth);
    requestStatusName.setPrefWidth(ColumnsWidth);
    deleteRequest.setPrefWidth(ColumnsWidth);

    // CELL VALUE FACTORY
    dateFromName.setCellValueFactory(param -> param.getValue().getValue().dateFrom);
    dateToName.setCellValueFactory(param -> param.getValue().getValue().dateTo);
    reasonName.setCellValueFactory(param -> param.getValue().getValue().reason);
    userName.setCellValueFactory(param -> param.getValue().getValue().userName);
    requestStatusName.setCellValueFactory(param -> param.getValue().getValue().requestStatus);
    deleteRequest.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue()));

    // CELL FACTORY
    dateFromName.setCellFactory(param -> new DateEditingCell());
    dateToName.setCellFactory(param -> new DateEditingCell());
    reasonName.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    ObservableList<String> requestStatusOptions = FXCollections.observableArrayList();
    for (RequestStatus status : RequestStatus.values()) {
      requestStatusOptions.add(status.toString());
    }
    requestStatusName.setCellFactory(ChoiceBoxTreeTableCell.forTreeTableColumn(requestStatusOptions));
    deleteRequest.setCellFactory(param -> new JFXTreeTableCell<VacationRequest, VacationRequest>() {
      private final JFXButton deleteButton = new JFXButton("delete");

      @Override
      protected void updateItem(VacationRequest request, boolean empty) {
        super.updateItem(request, empty);

        if (request == null) {
          setGraphic(null);
          return;
        }

        setGraphic(deleteButton);
        // deleteButton.setDisable(userIsAdmin);
        deleteButton.setStyle("-fx-background-color: " + appSettings.getRelativePallete().getSecondary().getDark()
            + "; -fx-text-fill: -fx-secondarytext");
        deleteButton.setOnAction(event -> {
          handleCommitEditing();
          tableRequests.remove(request);
        });
      }
    });

    // CELL EDIT
    dateFromName.setEditable(!userIsAdmin);
    dateFromName.setOnEditCancel(cell -> handleCancelEditing());
    dateFromName.setOnEditCommit(cell -> {
      handleCommitEditing();
      TreeItem<VacationRequest> currentRequest = dataTable.getTreeItem(cell.getTreeTablePosition().getRow());
      currentRequest.getValue().setDateFrom(cell.getNewValue());
    });
    dateToName.setEditable(!userIsAdmin);
    dateToName.setOnEditCancel(cell -> handleCancelEditing());
    dateToName.setOnEditCommit(cell -> {
      handleCommitEditing();
      TreeItem<VacationRequest> currentRequest = dataTable.getTreeItem(cell.getTreeTablePosition().getRow());
      currentRequest.getValue().setDateTo(cell.getNewValue());
    });
    reasonName.setEditable(!userIsAdmin);
    reasonName.setOnEditCancel(cell -> handleCancelEditing());
    reasonName.setOnEditCommit(cell -> {
      handleCommitEditing();
      TreeItem<VacationRequest> currentRequest = dataTable.getTreeItem(cell.getTreeTablePosition().getRow());
      currentRequest.getValue().setReason(cell.getNewValue());
    });
    requestStatusName.setEditable(userIsAdmin);
    requestStatusName.setOnEditCancel(cell -> handleCancelEditing());
    requestStatusName.setOnEditCommit(cell -> {
      handleCommitEditing();
      TreeItem<VacationRequest> currentRequest = dataTable.getTreeItem(cell.getTreeTablePosition().getRow());
      currentRequest.getValue().setRequestStatus(cell.getNewValue());
    });

    // SET TABLE DATA
    for (ug.grupo2.project.Classes.VacationRequest vacationRequest : dataRequest) {
      VacationRequest vr = new VacationRequest(parseStringToDate(vacationRequest.getDateFrom()),
          parseStringToDate(vacationRequest.getDateTo()), vacationRequest.getReason(), vacationRequest.getUser(),
          vacationRequest.getStatus().toString());
      tableOriginalRequests.add(vr);
      tableRequests.add(vr);
    }

    // TABLE CONFIG
    final TreeItem<VacationRequest> root = new RecursiveTreeItem<VacationRequest>(tableRequests,
        RecursiveTreeObject::getChildren);
    dataTable.getColumns().setAll(dateFromName, dateToName, reasonName, userName, requestStatusName, deleteRequest);
    dataTable.setEditable(true);
    dataTable.setRoot(root);
    dataTable.setShowRoot(false);
  }

  @FXML
  private void handleCloseButtonAction(ActionEvent event) {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  void handleDiscardChangeButton(ActionEvent event) {
    JFXDialogLayout content = new JFXDialogLayout();
    JFXDialog dialog = new JFXDialog(rootPane, content, JFXDialog.DialogTransition.CENTER);
    JFXButton savebutton = new JFXButton("Discard");
    JFXButton cancelbutton = new JFXButton("Cancel");
    content.setBody(new Label("Esta seguro de desahacer los cambios"));
    content.setActions(cancelbutton, savebutton);
    content.setStyle("-fx-background-color: -fx-pallete-background; -fx-text-fill: -fx-secondarytext");
    savebutton.setStyle("-fx-background-color: -fx-secondary; -fx-text-fill: -fx-secondarytext");
    cancelbutton.setStyle("-fx-background-color: -fx-secondary; -fx-text-fill: -fx-secondarytext");
    cancelbutton.setOnAction(e -> dialog.close());
    savebutton.setOnAction(e -> {
      saveChangeButton.setDisable(true);
      discardChangeButton.setDisable(true);
      changeHasMade = false;
      tableRequests = FXCollections.observableArrayList();
      for (VacationRequest vacationRequest : tableOriginalRequests) {
        tableRequests.add(vacationRequest);
      }
      final TreeItem<VacationRequest> root = new RecursiveTreeItem<VacationRequest>(tableRequests,
          RecursiveTreeObject::getChildren);
      dataTable.setRoot(root);
      dialog.close();
    });
    dialog.show();
  }

  @FXML
  void handleSaveChangeButton(ActionEvent event) {
    JFXDialogLayout content = new JFXDialogLayout();
    JFXDialog dialog = new JFXDialog(rootPane, content, JFXDialog.DialogTransition.CENTER);
    JFXButton savebutton = new JFXButton("Save");
    JFXButton cancelbutton = new JFXButton("Cancel");
    content.setBody(new Label("Esta seguro de hacer los cambios"));
    content.setActions(cancelbutton, savebutton);
    content.setStyle("-fx-background-color: -fx-pallete-background; -fx-text-fill: -fx-secondarytext");
    savebutton.setStyle("-fx-background-color: -fx-secondary; -fx-text-fill: -fx-secondarytext");
    cancelbutton.setStyle("-fx-background-color: -fx-secondary; -fx-text-fill: -fx-secondarytext");
    cancelbutton.setOnAction(e -> dialog.close());
    savebutton.setOnAction(e -> {
      try {
        uploadDataRequests();
        refreshTableData();
        List<ug.grupo2.project.Classes.VacationRequest> newRequests = new ArrayList<>();
        for (VacationRequest vacationRequest : tableRequests) {
          newRequests.add(new ug.grupo2.project.Classes.VacationRequest(
              parseDateToString(vacationRequest.getDateFrom()), parseDateToString(vacationRequest.getDateTo()),
              vacationRequest.getReason().getValue(), vacationRequest.getUserName().getValue(),
              RequestStatus.valueOf(vacationRequest.getRequestStatus().getValue())));
        }
        Gson gson = new Gson();
        File FileUsuariosDB = new File("vacationRequests.json");
        Writer writer = new FileWriter(FileUsuariosDB);
        dataStore.setRequests(newRequests);
        gson.toJson(dataStore.getRequests(), writer);
        writer.close();
      } catch (IOException error) {
        System.out.println(error.getMessage());
      }
      dialog.close();
    });
    dialog.show();
  }

  @FXML
  void handleAddButtonAction(ActionEvent event) {
    try {
      // StackPane ss =
      // FXMLLoader.load(App.class.getResource("views/RequestVacation.fxml"));
      // JFXDialogLayout dialogLayout = new JFXDialogLayout();
      // dialogLayout.setBody(ss);
      // JFXDialog dialog = new JFXDialog(rootPane, dialogLayout,
      // JFXDialog.DialogTransition.BOTTOM);
      // dialog.show();
      Scene scene = new Scene(FXMLLoader.load(App.class.getResource("views/RequestVacation.fxml")));
      scene.getStylesheets().add(App.class.getResource("styles/RequestVacationStyle.css").toExternalForm());
      Stage stage = new Stage(StageStyle.TRANSPARENT);
      stage.setOnHiding(e -> {
        if (dataStore.getDataWasAdded()) {
          refreshTableData();
          List<ug.grupo2.project.Classes.VacationRequest> vrl = dataStore.getRequests();
          ug.grupo2.project.Classes.VacationRequest vc = vrl.get(vrl.size() - 1);
          VacationRequest newVc = new VacationRequest(parseStringToDate(vc.getDateFrom()),
              parseStringToDate(vc.getDateTo()), vc.getReason(), vc.getUser(), vc.getStatus().toString());
          tableRequests.add(newVc);
        }
      });
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      System.out.println("ERROR OPENING ADD VACATION REQUEST");
    }
  }

  @FXML
  private void handleWindowDragged(MouseEvent event) {
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setX(event.getScreenX() - x);
    stage.setY(event.getScreenY() - y);
  }

  @FXML
  private void handleWindowPressed(MouseEvent event) {
    x = event.getSceneX();
    y = event.getSceneY();
  }

  public Date parseStringToDate(String date) {
    try {
      return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    } catch (ParseException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public String parseDateToString(Date date) {
    return new SimpleDateFormat("yyyy-MM-dd").format(date);
  }

  public void handleCommitEditing() {
    saveChangeButton.setDisable(false);
    discardChangeButton.setDisable(false);
    changeHasMade = true;
  }

  public void handleCancelEditing() {
    if (!changeHasMade) {
      saveChangeButton.setDisable(true);
      discardChangeButton.setDisable(true);
    }
  }

  public void refreshStoreData() {
    dataRequest = userIsAdmin ? dataStore.getRequests() : dataStore.getUserRequest(dataStore.getUser().getName());
  }

  public void refreshTableData() {
    refreshStoreData();
    tableOriginalRequests.clear();
    for (ug.grupo2.project.Classes.VacationRequest vacationRequest : dataRequest) {
      VacationRequest vr = new VacationRequest(parseStringToDate(vacationRequest.getDateFrom()),
          parseStringToDate(vacationRequest.getDateTo()), vacationRequest.getReason(), vacationRequest.getUser(),
          vacationRequest.getStatus().toString());
      tableOriginalRequests.add(vr);
    }
  }

  public void uploadDataRequests() {
    List<ug.grupo2.project.Classes.VacationRequest> vr = new ArrayList<>();
    for (VacationRequest vacationRequest : tableRequests) {
      vr.add(new ug.grupo2.project.Classes.VacationRequest(parseDateToString(vacationRequest.getDateFrom()),
          parseDateToString(vacationRequest.getDateTo()), vacationRequest.getReason().getValue(),
          vacationRequest.getUserName().getValue(),
          RequestStatus.valueOf(vacationRequest.getRequestStatus().getValue())));
    }
    dataStore.setRequests(vr);
  }

  /**
   * Wrapper for table
   */
  class VacationRequest extends RecursiveTreeObject<VacationRequest> {
    SimpleObjectProperty<Date> dateFrom;
    SimpleObjectProperty<Date> dateTo;
    StringProperty reason;
    StringProperty userName;
    StringProperty requestStatus;

    public VacationRequest(Date dateFrom, Date dateTo, String reason, String userName, String requestStatus) {
      this.dateFrom = new SimpleObjectProperty(dateFrom);
      this.dateTo = new SimpleObjectProperty(dateTo);
      this.reason = new SimpleStringProperty(reason);
      this.userName = new SimpleStringProperty(userName);
      this.requestStatus = new SimpleStringProperty(requestStatus);
    }

    public Date getDateFrom() {
      return dateFrom.get();
    }

    public void setDateFrom(Date dateFrom) {
      this.dateFrom = new SimpleObjectProperty(dateFrom);
    }

    public Date getDateTo() {
      return this.dateTo.get();
    }

    public void setDateTo(Date dateTo) {
      this.dateTo = new SimpleObjectProperty(dateTo);
    }

    public StringProperty getReason() {
      return this.reason;
    }

    public void setReason(String reason) {
      this.reason = new SimpleStringProperty(reason);
    }

    public StringProperty getUserName() {
      return this.userName;
    }

    public void setUserName(String userName) {
      this.userName = new SimpleStringProperty(userName);
    }

    public StringProperty getRequestStatus() {
      return this.requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
      this.requestStatus = new SimpleStringProperty(requestStatus);
    }

  }

  class DateEditingCell extends JFXTreeTableCell<VacationRequest, Date> {

    private DatePicker datePicker;

    private DateEditingCell() {
    }

    @Override
    public void startEdit() {
      if (!isEmpty()) {
        super.startEdit();
        createDatePicker();
        setText(null);
        setGraphic(datePicker);
      }
    }

    @Override
    public void cancelEdit() {
      super.cancelEdit();

      setText(getDate().toString());
      setGraphic(null);
    }

    @Override
    public void updateItem(Date item, boolean empty) {
      super.updateItem(item, empty);

      if (empty) {
        setText(null);
        setGraphic(null);
      } else {
        if (isEditing()) {
          if (datePicker != null) {
            datePicker.setValue(getDate());
          }
          setText(null);
          setGraphic(datePicker);
        } else {
          setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
          setGraphic(null);
        }
      }
    }

    private void createDatePicker() {
      datePicker = new DatePicker(getDate());
      datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
      datePicker.setOnAction((e) -> {
        System.out.println("Committed: " + datePicker.getValue().toString());
        commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
      });
    }

    private LocalDate getDate() {
      return getItem() == null ? LocalDate.now() : getItem().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
  }

}