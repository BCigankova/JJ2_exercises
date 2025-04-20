package cv9.spaceInvaders;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SpaceInvadersApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(SpaceInvadersApp.class.getResource("/MainWindow.fxml"));
        Parent root = loader.load();

        SpaceInvadersController controller = loader.getController();

        Scene scene = new Scene(root, GameState.COLUMNS * SpaceInvadersController.UNIT_SIZE,
                GameState.ROWS * SpaceInvadersController.UNIT_SIZE, Color.BLACK);

        root.setStyle("-fx-background-color: transparent;");

        scene.setOnKeyPressed((KeyEvent e) -> controller.handleKeyPressed(e.getCode()));
        scene.setOnKeyReleased((KeyEvent e) -> controller.handleKeyReleased(e.getCode()));

        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


/*
TODO: Grafika: alieni 1. zakladni 2. vice druhu
               player
               nebo hitbox
               nastavit okraje hry aby to bylo odsazeny
       Gameplay: vice hracu?
       Other:   cleanup kodu pro readability

 */
