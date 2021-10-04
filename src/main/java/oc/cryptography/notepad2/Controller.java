package oc.cryptography.notepad2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class Controller {

    @FXML
    private TextArea textArea;

    Stage stage;
    static final byte ENCRYPTED_TAG =   (byte)0xA5;
    static final byte UNENCRYPTED_TAG = (byte)0xFF;
    static final String SALT = "Orange";

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
            try {
                handleSavingEncryptedFile(file);
            } catch (GeneralSecurityException | IOException e) {
                new Alert(Alert.AlertType.ERROR, "Error saving file. Aborting.").showAndWait();
            }
        }
    }

    private void handleSavingEncryptedFile(File file) throws GeneralSecurityException, IOException {
        String password = getPasswordFromUser();
        if (password == null) {
            return;
        }
        SecretKey key = generateSecretKey(password);
        ByteBuffer ivBuffer = ByteBuffer.allocate(16);
        new SecureRandom().nextBytes(ivBuffer.array());

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBuffer.array()));

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(ENCRYPTED_TAG);
        byteStream.writeBytes(MessageDigest.getInstance("SHA-256").digest(password.getBytes()));
        byteStream.writeBytes(ivBuffer.array());
        byteStream.writeBytes(cipher.doFinal(textArea.getText().getBytes()));

        FileOutputStream fileStream = new FileOutputStream(file);
        byteStream.writeTo(fileStream);
        byteStream.close();
        fileStream.close();
    }

    private SecretKey generateSecretKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), 54321, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private String getPasswordFromUser() {
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setHeaderText("Enter Password");
        String password;
        try {
            password = passwordDialog.showAndWait()
                    .orElseThrow(() -> new IllegalArgumentException("Error handling password. Aborting."));
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            return null;
        }
        return password;
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
            } catch (GeneralSecurityException | IOException e) {
                new Alert(Alert.AlertType.ERROR, "Error opening file. Aborting.").showAndWait();
            }
        }
    }

    private void handleOpeningFile(File file) throws GeneralSecurityException, IOException {
        FileInputStream fileStream = new FileInputStream(file);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte tag = (byte)fileStream.read();

        if (tag == ENCRYPTED_TAG) {
            String password = getPasswordFromUser();
            if (password == null) {
                return;
            }
            byte[] storedHash = fileStream.readNBytes(32);
            byte[] currentHash = MessageDigest.getInstance("SHA-256").digest(password.getBytes());

            if (Arrays.compare(storedHash, currentHash) == 0) {
                SecretKey key = generateSecretKey(password);

                byte[] iv = fileStream.readNBytes(16);

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
                byteStream.writeBytes(cipher.doFinal(fileStream.readAllBytes()));
            } else {
                new Alert(Alert.AlertType.ERROR, "Password Incorrect. Aborting.").showAndWait();
                return;
            }
        } else {
            byteStream.writeBytes(fileStream.readAllBytes());
        }

        fileStream.close();
        textArea.clear();
        textArea.setText(byteStream.toString());
        byteStream.close();
    }
}
