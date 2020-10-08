package ug.grupo2.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import ug.grupo2.project.Classes.AppSettings;
import ug.grupo2.project.Classes.DataStore;
import ug.grupo2.project.Classes.Personal;
import ug.grupo2.project.Classes.VacationRequest;
import ug.grupo2.project.enums.RequestStatus;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class RequestVacationController implements Initializable {
  double x, y;
  DataStore dataStore = DataStore.getInstance();
  AppSettings appSettings = dataStore.getAppSettings();
  Personal user = dataStore.getUser();

  @FXML
  private StackPane rootPane;
  @FXML
  private Button closeButton;
  @FXML
  private DatePicker dateTo;
  @FXML
  private DatePicker dateFrom;
  @FXML
  private JFXTextField userReason;
  @FXML
  private Label errorMessage;
  @FXML
  private JFXButton sendButton;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    // STYLES
    rootPane.setStyle("-fx-pallete-background: " + appSettings.getRelativePallete().getBackground()
        + "; -fx-secondary: " + appSettings.getRelativePallete().getSecondary().getMain() + "; -fx-secondary-light: "
        + appSettings.getRelativePallete().getSecondary().getLight() + "; -fx-primarytext: "
        + appSettings.getRelativePallete().getPrimary().getContrastText());

    RequiredFieldValidator validator = new RequiredFieldValidator();
    // Validator for text field
    validator.setMessage("Input Required");
    userReason.getValidators().add(validator);
    userReason.focusedProperty().addListener((o, oldVal, newVal) -> {
      if (!newVal)
        userReason.validate();
    });

  }

  @FXML
  private void handleCloseButtonAction(ActionEvent event) {
    if (InputsAreEmpty())
      dataStore.setDataWasAdded(false);
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void handleSendButtonAction(ActionEvent event) {
    try {
      Gson gson = new Gson();
      File FileUsuariosDB = new File("vacationRequests.json");
      LocalDate dateFromInput = dateFrom.getValue();
      LocalDate dateToInput = dateTo.getValue();
      String reasonInput = userReason.getText();
      boolean isDateFromEmpty = dateFromInput == null, isDateToEmpty = dateToInput == null,
          isReasonEmpty = reasonInput.isEmpty();
      if (isDateFromEmpty || isDateToEmpty || isReasonEmpty) {
        final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
        if (isDateFromEmpty)
          dateFrom.pseudoClassStateChanged(errorClass, true);
        if (isDateToEmpty)
          dateTo.pseudoClassStateChanged(errorClass, true);

        errorMessage.setText("There are empty fields");
      } else {
        VacationRequest vr = new VacationRequest(dateFromInput.toString(), dateToInput.toString(), reasonInput,
            user.getName(), RequestStatus.Pending);
        if (dataStore.addRequest(vr)) {
          Writer writer = new FileWriter(FileUsuariosDB);
          gson.toJson(dataStore.getRequests(), writer);
          writer.close();
          dataStore.setDataWasAdded(true);
          Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
          stage.close();
        } else {
          errorMessage.setText("A Error Ocurred");
        }
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
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

  public boolean InputsAreEmpty() {
    return dateFrom.getValue() == null || dateTo.getValue() == null || userReason.getText().isEmpty();
  }
}
