import javafx.stage.Stage;


public class CipherModel {
    private final CipherUIPane UIPane;
    private int keyValue = 3;

    protected CipherModel(CipherUIPane pane) {
        this.UIPane = pane;
    }

    protected void encrypt() {

    }

    protected void decrypt() {

    }

    protected void reverse() {

    }

    protected void changeKey() {

    }

    protected void about() {

    }

    protected void exit() {
        /*
        {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
        }
         */

    }

    protected int getKeyValue() {
        return keyValue;
    }

    protected void setKeyValue(int keyValue) {
        this.keyValue = keyValue;
    }
}
