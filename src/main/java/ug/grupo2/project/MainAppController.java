package ug.grupo2.project;

import ug.grupo2.project.Classes.Personal;
import ug.grupo2.project.Classes.VacationRequest;
import ug.grupo2.project.Classes.AppSettings;
import ug.grupo2.project.Classes.DataStore;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXToggleButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainAppController implements Initializable {

  double x, y;
  DataStore dataStore = DataStore.getInstance();
  Personal user = dataStore.getUser();
  AppSettings appSettings = dataStore.getAppSettings();

  @FXML
  private StackPane MainApp;
  @FXML
  private AnchorPane appBar;
  @FXML
  private Circle profilePic;
  @FXML
  private Button closeButton;
  @FXML
  private JFXButton requestButton;
  @FXML
  private Label userNameLabel;
  @FXML
  private JFXToggleButton ToggleDarkMode;

  @FXML
  private void handleCloseButtonAction(ActionEvent event) {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    // Dynamic styles
    MainApp.setStyle("-fx-background-color: " + appSettings.getRelativePallete().getBackground() + "; -c-primary: "
        + appSettings.getRelativePallete().getSecondary().getMain());
    appBar.setStyle("-fx-background-color: " + appSettings.getRelativePallete().getPrimary().getMain());

    // Logic
    userNameLabel.setText(user.getName());
    ToggleDarkMode.selectedProperty().set(appSettings.getDarkMode());
    ToggleDarkMode.selectedProperty().addListener(e -> {
      appSettings.setDarkMode(ToggleDarkMode.isSelected());
      MainApp.setStyle("-fx-background-color: " + appSettings.getRelativePallete().getBackground() + "; -c-primary: "
          + appSettings.getRelativePallete().getSecondary().getMain());
      appBar.setStyle("-fx-background-color: " + appSettings.getRelativePallete().getPrimary().getMain());
    });

    try {
      Gson gson = new Gson();
      File FileUsuariosDB = new File("vacationRequests.json");
      if (!FileUsuariosDB.exists())
        FileUsuariosDB.createNewFile();
      dataStore.setRequests(gson.fromJson(new FileReader(FileUsuariosDB), new TypeToken<List<VacationRequest>>() {
      }.getType()));
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    // profilePic.setFill(
    // new ImagePattern(new
    // Image(getClass().getResource("../img/profilePictures/picDefault.png").toExternalForm())));
    profilePic.setFill(new ImagePattern(
        new Image(App.class.getResource("img/profilePictures/" + user.getProfilePicture()).toExternalForm())));
  }

  @FXML
  private void handleRequestVacation(ActionEvent event) {
    try {
      Scene scene = new Scene(FXMLLoader.load(App.class.getResource("views/Requests.fxml")));
      scene.getStylesheets().add(App.class.getResource("styles/RequestsStyle.css").toExternalForm());
      Stage stage = new Stage(StageStyle.TRANSPARENT);
      stage.setScene(scene);
      stage.show();
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

}
