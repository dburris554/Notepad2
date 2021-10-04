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

    @FXML
    private MenuItem encryptedSaveOption;

    static final int ENCRYPTED_TAG = 0xA5;
    static final int UNENCRYPTED_TAG = 0xFF;

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
        dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File","*.txt"));
        File file = dialog.showSaveDialog(stage);
        if (file != null) {
            handleSavingFile(file);
        }
    }

    private void handleSavingFile(File file) {
        //TODO Create output byte array with tag (buffer?)

        //TODO Append text area text as a byte array
        textArea.getText();

        //TODO Write bytes into file and close
    }

    @FXML
    void openEncryptedSaveDialog(ActionEvent event) {
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Save Encrypted Text File");
        dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File","*.txt"));
        File file = dialog.showSaveDialog(stage);
        if (file != null) {
            handleSavingEncryptedFile(file);
        }
    }

    private void handleSavingEncryptedFile(File file) {
        //TODO Use a TextInputDialog to get a Password

        //TODO Create IV
        //TODO Initialize Cipher object
        //TODO Create output byte array with tag (buffer?)
        //TODO Append SHA-256 hash of password
        //TODO Append encrypted text area text as bytes

        //TODO Write bytes into file and close

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
        //TODO Create FileInputStream from file

        //TODO Create empty byte array for textArea content (buffer?)

        //TODO Read first byte to see if encrypted

        //TODO If encrypted,
            //TODO Request and store password
            //TODO Read and store next 32 bytes as password hash
            //TODO If provided password hashes to same value,
                //TODO Decrypt and append remaining bytes
            //TODO else, open an Alert stating password was incorrect and the operation was aborted
                //TODO return

        //TODO else, append bytes into byte array

        //TODO Write byte array into textArea
    }

}
