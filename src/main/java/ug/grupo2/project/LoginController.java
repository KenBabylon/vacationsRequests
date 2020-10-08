package ug.grupo2.project;

import ug.grupo2.project.Classes.Personal;
import ug.grupo2.project.Classes.DataStore;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class LoginController implements Initializable {
    double x, y;
    List<Personal> personalList;

    @FXML
    private Button closeButton;
    @FXML
    private Button singInButton;
    @FXML
    private JFXTextField userInput;
    @FXML
    private JFXTextField passwordInput;
    @FXML
    private Label errorMessage;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        passwordInput.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                handleSingIn();
            }
        });

        try {
            Gson gson = new Gson();
            File FileUsuariosDB = new File("usuariosDB.json");
            if (!FileUsuariosDB.exists() || FileUsuariosDB.length() == 0) {
                FileUsuariosDB.createNewFile();
                errorMessage.setText("No hay usuarios en la base de datos");
                singInButton.setDisable(true);
            } else {
                FileReader jsonFileDB = new FileReader(FileUsuariosDB);
                personalList = gson.fromJson(jsonFileDB, new TypeToken<List<Personal>>() {
                }.getType());
            }
        } catch (IOException e1) {
            System.out.println(e1.getMessage());
        }
    }

    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSingInButton(ActionEvent event) {
        handleSingIn();
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

    private void handleSingIn() {
        Personal p1 = null;
        DataStore dataStore = DataStore.getInstance();
        Boolean isUserValid = false;
        String userEntered = userInput.getText();
        String passwordEntered = passwordInput.getText();
        for (Personal personal : personalList) {
            if (personal.getUser().equals(userEntered)) {
                isUserValid = true;
                if (personal.getPassword().equals(passwordEntered)) {
                    p1 = personal;
                } else {
                    errorMessage.setText("Invalid Password");
                }
            }
        }
        if (p1 != null) {
            try {
                dataStore.setUser(p1);
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();
                App.setRoot("MainApp");
                App.setStyles("MainAppStyle");
                stage.show();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            if (!isUserValid)
                errorMessage.setText("Invalid User");
        }
    }
}
