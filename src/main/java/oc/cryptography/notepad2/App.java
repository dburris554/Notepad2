package oc.cryptography.notepad2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Controller controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.setTitle("Notepad2");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}