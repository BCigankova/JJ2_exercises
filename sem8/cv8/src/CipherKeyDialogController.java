import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CipherKeyDialogController {

    private Stage stage;
    private CipherFXMLController cpc;
    @FXML private TextField keyDialogInput;


    @FXML private void okAction() {
        if(!checkKeyInput(keyDialogInput.getText())) {
            cpc.displayInputErrorDialog();
            return;
        }
        int key = Integer.parseInt(keyDialogInput.getText());
        cpc.setKeyValue(key);
        cpc.setTxtKey("Key: " + key);
        stage.close();
    }

    @FXML private void cancelAction() {
        stage.close();
    }

    private boolean checkKeyInput(String input) {
        if ((input != null) && (!input.isEmpty())) {
            try {
                int newKey = Integer.parseInt(input);
                if (newKey < 0 || newKey > 26)
                    throw new Exception();
                else
                    return true;
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void setCPC(CipherFXMLController cpc) {
        this.cpc = cpc;
    }
}
