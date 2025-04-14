import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SpaceInvadersApp extends Application {

    public static final int UNIT_SIZE = 5;
    private static final int DELAY = 500;

    private final GameState gameState = new GameState();
    private Pane gamePane;
    private Text lbScore;
    private Button btnStart;
    private Timeline timeline;


    @Override
    public void start(Stage primaryStage) throws Exception {
        createGamePane();

        lbScore = new Text();
        lbScore.textProperty().bind(gameState.scoreProperty().asString());

        timeline = new Timeline();
        KeyFrame updates = new KeyFrame(
                Duration.millis(DELAY),
                e -> { if (!gameState.update()) gameOver(); }
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

        for (Enemy enemy: gameState.getEnemies())  //vezme objekt enemy a prida mu grafiku + bind v create
            gamePane.getChildren().add(new EnemyShape(enemy));     //dodelat vice druhu enemies, podle row pozice
        for(Shield shield: gameState.getShields())
            gamePane.getChildren().add(new ShieldShape(shield));
        gamePane.getChildren().add(new PlayerShape(gameState.getPlayer1()));
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

                unit_size 5 je dobry, nejak srovnat, aby konec herni plochy byl kousek od okraje
       Abstrakce: sjednotit tridy se stejnyma vlastnostma, abstract_placeable? pro vsechny

       Vyresit lasery: jak je vykreslit + dat shoot do objektu nebo do GameState?

       Zrychlovani: pohyb se musi zrychlovat, dodatecny zrychleni po nejakem case

       Shieldy: jak z nich vykrojit kus? odstranit pixely okolo

       Hit: nejak lip to implementovat i pro readability, neco.hit atd

       Prizpusobeni: udelat to tak aby se dalo nastavit kolik chci stitu, enemaku atd...
 */
