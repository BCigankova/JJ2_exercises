import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.skin.LabeledSkinBase;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SpaceInvadersApp extends Application {

    public static final int UNIT_SIZE = 5;
    private static final int DELAY = 50;

    private final GameState gameState = new GameState();
    private Pane gamePane;
    private Text lbScore;
    private Button btnStart;
    private Timeline timeline;


    @Override
    public void start(Stage primaryStage) throws Exception {
        createGamePane();

        gameState.lasersProperty().addListener((ListChangeListener<Laser>) e -> {e.next(); addLaser(e.getAddedSubList().getFirst());});

        lbScore = new Text();
        lbScore.textProperty().bind(gameState.scoreProperty().asString());

        timeline = new Timeline();
        KeyFrame updates = new KeyFrame(
                Duration.millis(DELAY),
                e -> {
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

        HBox scorePane = new HBox(10, new Text("Score: "), lbScore);

        root.setCenter(gameStack);
        root.setTop(scorePane);

        Scene scene = new Scene(root, GameState.COLUMNS * UNIT_SIZE, GameState.ROWS * UNIT_SIZE);
        scene.setOnKeyPressed(this::dispatchKeyEvents);
        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void dispatchKeyEvents(KeyEvent e) {
        switch (e.getCode()) {
            case A:  gameState.moveLeft(); break;
            case D: gameState.moveRight(); break;
            case SPACE: gameState.playerShoot(); break;
        }
    }

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
       Shieldy: jak z nich vykrojit kus? odstranit pixely okolo nebo zivoty
       Pohyb: aby strilel a hybal se
                pohyb se musi zrychlovat, dodatecny zrychleni po nejakem case
                strely musi byt rychlejsi o ranec
       Gameplay: dodelat zivoty hracovi
                 strileni alienu
       Other: package
              cleanup kodu pro readability

 */
