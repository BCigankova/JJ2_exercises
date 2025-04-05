import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class CipherFXMLController {
    private Stage stage;
    private int keyValue = 3;

    @FXML private TextField inputEncrypt;
    @FXML private TextField inputDecrypt;
    @FXML private Button btnEncrypt;
    @FXML private Button btnDecrypt;

    @FXML private MenuItem mnuEncrypt;
    @FXML private MenuItem mnuDecrypt;

    @FXML private MenuItem mnuPlaintext;
    @FXML private MenuItem mnuCiphertext;

    @FXML private Text txtKey;


    @FXML public void encryptAction() {
        String ciphertextRes = crypt(inputEncrypt.getText(), "en");
        if(ciphertextRes == null) {
            displayInputErrorDialog();
            return;
        }
        inputDecrypt.setText(ciphertextRes);
    }

    @FXML protected void decryptAction() {
        String plaintextRes = crypt(inputDecrypt.getText(), "de");
        if(plaintextRes == null) {
            displayInputErrorDialog();
            return;
        }
        inputEncrypt.setText(plaintextRes);
    }

    @FXML protected void reverseAction() {
        if(isEncryptEnabled()) {
            disableEncryptWindow(true);
            disableEncryptMenu(true);
        } else {
            disableEncryptWindow(false);
            disableEncryptMenu(false);
        }
    }

    @FXML protected void changeKeyAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(CipherFXMLController.class.getResource("/resources/CipherKeyInputDialogFXML.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        CipherKeyDialogController controller = loader.getController();
        controller.setStage(stage);
        controller.setCPC(this);


        stage.initOwner(this.stage);
        stage.initModality(Modality.WINDOW_MODAL);


        stage.setTitle("New key");
        stage.centerOnScreen();
        Scene scene = new Scene(root, 150, 50);
        stage.setScene(scene);
        stage.show();
    }

    @FXML protected void aboutAction() {
        try {
            Desktop.getDesktop().browse(new URI("https://en.wikipedia.org/wiki/Caesar_cipher"));
        } catch (IOException | URISyntaxException ex) {
            System.out.println("ajaj");
        }
    }

    @FXML protected void exitAction() {
        stage.close();
    }

    private boolean isEncryptEnabled() {
        return !btnEncrypt.isDisable();
    }

    private void disableEncryptWindow(boolean what) {
        inputEncrypt.setDisable(what);
        btnEncrypt.setDisable(what);
        inputDecrypt.setDisable(!what);
        btnDecrypt.setDisable(!what);
    }

    private void disableEncryptMenu(boolean what) {
        mnuEncrypt.setDisable(what);
        mnuDecrypt.setDisable(!what);

        mnuPlaintext.setDisable(!what);
        mnuCiphertext.setDisable(what);
    }

    private String crypt(String text, String type) {
        StringBuilder resText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);

            if (Character.isUpperCase(letter))
                resText.append((char) ((letter + (type.equals("en") ? keyValue : -keyValue) - 'A' + (type.equals("de") ? 26 : 0)) % 26 + 'A'));
            else if (Character.isLowerCase(letter))
                resText.append((char) ((letter + (type.equals("en") ? keyValue : -keyValue) - 'a' + (type.equals("de") ? 26 : 0)) % 26 + 'a'));
            else if (letter == ' ' || letter == '.') {
                resText.append(letter);
            } else
                return null;     //dialog okno s errorem?
        }
        return resText.toString();
    }

    protected void displayInputErrorDialog() {
        new Alert(Alert.AlertType.ERROR, "Wrong input").show();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        txtKey.setText("Key: " + keyValue);
        this.stage = stage;
    }

    public int getKeyValue() {
        return this.keyValue;
    }

    public void setKeyValue(int val) {
        this.keyValue = val;
    }

    public void setTxtKey(String txt) {
        this.txtKey.setText(txt);
    }



}
