package oc.cryptography.notepad2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    @FXML
    private TextArea textArea;

    Stage stage;

    @FXML
    private MenuItem openOption;

    @FXML
    private MenuItem saveOption;

    void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void clearTextArea(ActionEvent event) {
        textArea.clear();
    }

    @FXML
    void openSaveDialog(ActionEvent event) {
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Save Text File");
        dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.txt"));
        File file = dialog.showSaveDialog(stage);
        if (file != null) {
            handleSavingFile(file);
        }
    }

    private void handleSavingFile(File file) {
        //TODO
        textArea.getText();
    }

    @FXML
    void openTextFile(ActionEvent event) {
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Open Text File");
        dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File","*.txt"));
        File file = dialog.showOpenDialog(stage);
        if (file != null) {
            handleOpeningFile(file);
        }
    }

    private void handleOpeningFile(File file) {
        //TODO
    }

}
