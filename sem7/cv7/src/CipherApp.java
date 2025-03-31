import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CipherApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new CipherUIPane());
        primaryStage.setTitle("Ceasar Cipher");
        primaryStage.setScene(scene);
        primaryStage.setWidth(500);
        primaryStage.setHeight(200);
        primaryStage.show();
    }

    //fugnuje i bez toho tho?
    public static void main(String[] args) {
        launch(args);
    }
}
