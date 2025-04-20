package cv9.spaceInvaders;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class SpaceInvadersController implements Initializable {

    private static final int DELAY = 10;
    public static final int UNIT_SIZE = 5;

    private final GameState gameState = new GameState(DELAY);

    @FXML private Pane gamePane;
    @FXML private StackPane gameStack;
    @FXML private Text lbScore;
    @FXML private Text lbLives;
    @FXML private Button btnStart;

    private Timeline timeline;
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    private final KeyCode MOVE_LEFT_KEY = KeyCode.A;
    private final KeyCode MOVE_RIGHT_KEY = KeyCode.D;
    private final KeyCode SHOOT_KEY = KeyCode.SPACE;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createGamePane();

        gameState.lasersProperty().addListener(this::updateLasers);

        lbScore.textProperty().bind(gameState.scoreProperty().asString());
        lbLives.textProperty().bind(gameState.getPlayer1().livesProperty().asString());

        timeline = new Timeline(new KeyFrame(Duration.millis(DELAY), e -> {
            if (pressedKeys.contains(MOVE_LEFT_KEY)) gameState.moveLeft();
            if (pressedKeys.contains(MOVE_RIGHT_KEY)) gameState.moveRight();
            if (!gameState.update()) gameOver();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);

        btnStart.visibleProperty().bind(gameState.activeProperty().not());
    }

    @FXML public void onStartAction() {
        gameState.setDefaultValues();
        gameState.setActive(true);
        gamePane.setOpacity(1);
        timeline.play();
    }

    public void handleKeyPressed(KeyCode code) {
        pressedKeys.add(code);
        if (code == SHOOT_KEY) gameState.playerShoot();
    }

    public void handleKeyReleased(KeyCode code) {
        pressedKeys.remove(code);
    }

    private void updateLasers(ListChangeListener.Change<? extends Laser> e) {
        e.next();
        if (e.wasAdded()) addLaser(e.getAddedSubList().getFirst());
    }

    private void gameOver() {
        timeline.stop();
        gamePane.setOpacity(0.2);
    }

    private void createGamePane() {
        gamePane.setMaxWidth(UNIT_SIZE * GameState.COLUMNS);
        gamePane.setMaxHeight(UNIT_SIZE * GameState.ROWS);

        for (Enemy enemy : gameState.getEnemies()) {
            EnemyShape enemyShape = new EnemyShape(enemy);
            gamePane.getChildren().add(enemyShape);
            enemy.setShape((Shape) enemyShape.getChildren().getFirst());
        }

        for (Shield shield : gameState.getShields()) {
            ShieldShape shieldShape = new ShieldShape(shield);
            gamePane.getChildren().add(shieldShape);
            shield.setShape((Shape) shieldShape.getChildren().getFirst());
        }

        PlayerShape playerShape = new PlayerShape(gameState.getPlayer1());
        gamePane.getChildren().add(playerShape);
        gameState.getPlayer1().setShape((Shape) playerShape.getChildren().getFirst());
    }

    private void addLaser(Laser laser) {
        LaserShape laserShape = new LaserShape(laser);
        gamePane.getChildren().add(laserShape);
        laser.setShape((Shape) laserShape.getChildren().getFirst());
    }
}
