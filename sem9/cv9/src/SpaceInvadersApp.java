import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

public class SpaceInvadersApp extends Application {

    public static final int UNIT_SIZE = 5;
    private static final int DELAY = 10;

    private final GameState gameState = new GameState(DELAY);

    private Pane gamePane;
    private Text lbScore;
    private Text lbLives;
    private Button btnStart;

    private Timeline timeline;
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    private final KeyCode MOVE_LEFT_KEY = KeyCode.A;
    private final KeyCode MOVE_RIGHT_KEY = KeyCode.D;
    private final KeyCode SHOOT_KEY = KeyCode.SPACE;


    @Override
    public void start(Stage primaryStage) throws Exception {
        createGamePane();

        gameState.lasersProperty().addListener(this::updateLasers);

        lbScore = new Text();
        lbScore.textProperty().bind(gameState.scoreProperty().asString());

        lbLives = new Text();
        lbLives.textProperty().bind(gameState.getPlayer1().livesProperty().asString());

        timeline = new Timeline();
        KeyFrame updates = new KeyFrame(
                Duration.millis(DELAY),
                e -> {
                    if (pressedKeys.contains(MOVE_LEFT_KEY)) {
                        gameState.moveLeft();
                    }
                    if (pressedKeys.contains(MOVE_RIGHT_KEY)) {
                        gameState.moveRight();
                    }
                    if (!gameState.update()) gameOver();
                }
        );

        timeline.getKeyFrames().add(updates);
        timeline.setCycleCount(Animation.INDEFINITE);

        btnStart = new Button("Start");
        btnStart.visibleProperty().bind(gameState.activeProperty().not());
        btnStart.setOnAction(e -> {
            gameState.setDefaultValues();
            gameState.setActive(true);
            gamePane.setOpacity(1);
            timeline.play();
        });

        btnStart.setScaleX(2);
        btnStart.setScaleY(2);

        BorderPane root = new BorderPane();

        StackPane gameStack = new StackPane();
        gameStack.getChildren().addAll(gamePane, btnStart);

        HBox infoPane = new HBox(10, new Text("Score: "), lbScore, new Text("\t\t\tLives: "), lbLives);

        root.setCenter(gameStack);
        root.setTop(infoPane);

        Scene scene = new Scene(root, GameState.COLUMNS * UNIT_SIZE, GameState.ROWS * UNIT_SIZE);

        scene.setOnKeyPressed(e -> {pressedKeys.add(e.getCode());
        if (e.getCode() == SHOOT_KEY)
            gameState.playerShoot();
        });


        scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));

        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateLasers(ListChangeListener.Change<? extends Laser> e) {
        e.next();
        if (e.wasAdded()) {
            addLaser(e.getAddedSubList().getFirst());
        }
    }
    /*
    private void dispatchKeyEvents(KeyEvent e) {
        switch (e.getCode()) {
            case A:  gameState.moveLeft(); break;
            case D: gameState.moveRight(); break;
            case SPACE: gameState.playerShoot(); break;
        }
    }

     */

    private void gameOver() {
        timeline.stop();
        gamePane.setOpacity(0.2);
    }

    private void createGamePane() {
        gamePane = new Pane();
        gamePane.setMaxWidth(UNIT_SIZE * GameState.COLUMNS);
        gamePane.setMaxHeight(UNIT_SIZE * GameState.ROWS);

        for (Enemy enemy: gameState.getEnemies()) {  //vezme objekt enemy a prida mu grafiku + bind v create
            EnemyShape enemyShape = new EnemyShape(enemy);
            gamePane.getChildren().add(enemyShape);
            enemy.setShape((Shape) enemyShape.getChildren().getFirst());
        }                                                         //vice druhu enemies
        for(Shield shield: gameState.getShields()) {
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
        gamePane.getChildren().add(new LaserShape(laser));
        laser.setShape((Shape) laserShape.getChildren().getFirst());
    }


    public static void main(String[] args) {
        launch(args);
    }
}

/*
TODO: Grafika: alieni 1. zakladni 2. vice druhu
               player
               shieldy
               vsechno polygon? sam vi kde ma x, y
               nebo hitbox
               (((oddelat pryc vykresleni laseru z app (musim je asi odstranovat misto invisible))))
       Shieldy: jak z nich vykrojit kus? odstranit pixely okolo nebo zivoty
       Gameplay: vice hracu?
       Other: package
              cleanup kodu pro readability
              predelat do javafxml gamepanes?

 */
