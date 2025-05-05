package finalproject.fx;

import finalproject.server.DelacVeci;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    private Stage primaryStage;
    private DelacVeci dv;
    @FXML private TextField username;
    @FXML private TextField password;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void logInAction() {
        if(!username.getText().isEmpty() && !password.getText().isEmpty())
            dv.login(username.getText(), password.getText());
    }

    @FXML
    public void signUpAction() {
        if(!username.getText().isEmpty() && !password.getText().isEmpty())
            dv.signup(username.getText(), password.getText());
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
}
