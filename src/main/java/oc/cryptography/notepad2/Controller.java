package oc.cryptography.notepad2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

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

    static final byte ENCRYPTED_TAG =   (byte)0xA5;
    static final byte UNENCRYPTED_TAG = (byte)0xFF;

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
            try {
                handleSavingFile(file);
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Error saving file. Aborting.").showAndWait();
                return;
            }
        }
    }

    private void handleSavingFile(File file) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(UNENCRYPTED_TAG);
        byteStream.writeBytes(textArea.getText().getBytes());
        FileOutputStream fileStream = new FileOutputStream(file);
        byteStream.writeTo(fileStream);
        byteStream.close();
        fileStream.close();
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
            try {
                handleOpeningFile(file);
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Error opening file. Aborting.").showAndWait();
                return;
            }
        }
    }

    private void handleOpeningFile(File file) throws IOException {
        FileInputStream fileStream = new FileInputStream(file);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        byte tag = (byte)fileStream.read();

        if (tag == ENCRYPTED_TAG) {
            //TODO Request and store password
            //TODO Read and store next 32 bytes as password hash
            //TODO If provided password hashes to same value,
                //TODO Decrypt and append remaining bytes
            //TODO else, open an Alert stating password was incorrect and the operation was aborted
            //TODO return
        } else {
            byteStream.writeBytes(fileStream.readAllBytes());
        }

        fileStream.close();
        textArea.clear();
        textArea.setText(byteStream.toString());
        byteStream.close();
    }

}
