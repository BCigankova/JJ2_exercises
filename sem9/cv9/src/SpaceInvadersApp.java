import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SpaceInvadersApp extends Application {

    private static final int UNIT_SIZE = 15;
    private static final int DELAY = 100;
    private GameState gameState = new GameState();
    private Pane gamePane;
    private Text lbScore;
    private Button btnStart;
    private Timeline timeline;


    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    private void createGamePane() {
        gamePane = new Pane();
        gamePane.setMaxWidth(UNIT_SIZE * GameState.COLUMNS);
        gamePane.setMaxHeight(UNIT_SIZE * GameState.ROWS);

        for (Enemy enemy : gameState.getEnemies())
            gamePane.getChildren().add(createEnemy(enemy));

        gamePane.getChildren().addAll(
                createBall(gameState.getBall()),
                createPaddle());
    }

    private EnemyShape createEnemy() {
        EnemyShape enemyShape = new EnemyShape(UNIT_SIZE);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
