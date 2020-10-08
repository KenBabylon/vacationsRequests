package ug.grupo2.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ug.grupo2.project.Classes.AppSettings;
import ug.grupo2.project.Classes.DataStore;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Login"), Color.TRANSPARENT);
        App.setStyles("LoginStyle");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static void setStyles(String styleSheetName) throws IOException {
        scene.getStylesheets().add(App.class.getResource("styles/" + styleSheetName + ".css").toExternalForm());
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        try {
            Gson gson = new Gson();
            File appSettings = new File("appSettings.json");
            if (!appSettings.exists())
                appSettings.createNewFile();
            DataStore.getInstance().setAppSettings(gson.fromJson(new FileReader(appSettings), AppSettings.class));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        launch();
    }

}