import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameState {
    private final List<Enemy> enemies;
    private final ArrayList<Shield> shieldArray;
    private final Player player1;

    private static final int ENEMY_WEAK = 10;
    private static final int ENEMY_AVG = 20;
    private static final int ENEMY_STRONG = 30;

    private int gameSpeed = 1000;  //1s doufam


    private final IntegerProperty score;


    public GameState() {
        this.enemies = new LinkedList<>();
        this.shieldArray = new ArrayList<>();
        this.player1 = new Player();
        this.score = new SimpleIntegerProperty(0);


    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public int getGameSpeed() {
        return gameSpeed;
    }

    public void setGameSpeed(int gameSpeed) {
        this.gameSpeed = gameSpeed;
    }
}
