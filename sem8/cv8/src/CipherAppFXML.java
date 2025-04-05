import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CipherAppFXML extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(CipherAppFXML.class.getResource("/resources/CipherFXML.fxml"));
        Parent root = loader.load();

        CipherFXMLController controller = loader.getController();
        controller.setStage(primaryStage);

        primaryStage.setTitle("Ceasar Cipher");
        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.show();
    }

    //fugnuje i bez toho tho?
    public static void main(String[] args) {
        launch(args);
    }
}
