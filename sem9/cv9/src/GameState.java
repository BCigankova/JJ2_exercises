import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState {
    private final List<Enemy> enemies;
    private final List<Shield> shields;
    private List<Laser> lasers;
    private final Player player1;

    public static final int ENEMY_WEAK = 10;
    public static final int ENEMY_AVG = 20;
    public static final int ENEMY_STRONG = 30;


    public static final int ROWS = 200;
    public static final int COLUMNS = 250;

    public static final double SHIELD_WIDTH = 10;
    public static final double SHIELD_LENGTH = 10;

    public static final int SHIELD_ROWS = 4;

    public static final double PLAYER_WIDTH = 15;
    public static final double PLAYER_LENGTH = 15;

    public static final double ENEMY_WIDTH = 10;
    public static final double ENEMY_LENGTH = 10;

    public static final int ENEMY_ROWS = 5;
    public static final int ENEMY_COLS = 11;

    public static final int LASER_LENGTH = 5;

    private final IntegerProperty score;

    private final BooleanProperty active;


    public GameState() {
        this.enemies = new ArrayList<>();
        initializeEnemies();
        this.shields = new ArrayList<>();
        initializeShields();
        this.lasers = new ArrayList<>();
        this.player1 = new Player((int) ((COLUMNS - PLAYER_WIDTH) / 2), (int) (ROWS - PLAYER_LENGTH - 5));  //5 odsazeni at to lip vypada
        this.score = new SimpleIntegerProperty(0);
        this.active = new SimpleBooleanProperty(false);
    }

    private void initializeEnemies() {
        for(int i = 0; i < ENEMY_ROWS; i++)
            for(int j = 0; j < ENEMY_COLS; j++)
                enemies.add(new Enemy(0, (int) ((i + 1) * (ENEMY_LENGTH + 5)), (int) ((j + 1) * (ENEMY_WIDTH + 5))));
    }

    private void initializeShields() {
        for(int i = 0; i < SHIELD_ROWS; i++)
            shields.add(new Shield((int) ((i + 1) * (SHIELD_WIDTH + 10))));
    }

    public void setDefaultValues() {
        for(int i = 0; i < ENEMY_ROWS; i++)
            for(int j = 0; j < ENEMY_COLS; j++) {
                enemies.get(i * ENEMY_ROWS + j).setRow((int) ((i + 1) * (ENEMY_LENGTH + 5)));        //tady bude chyba v zobrazovani
                enemies.get(i * ENEMY_ROWS + j).setColumn((int) ((j + 1) * (ENEMY_WIDTH + 5)));
            }

        enemies.forEach(e -> e.setActive(true));
        shields.forEach(s -> s.setActive(true));

        player1.setColumn((int) ((COLUMNS - PLAYER_WIDTH) / 2));
        player1.setRow((int) (ROWS - PLAYER_LENGTH - 5));
        lasers = new ArrayList<>();
        score.setValue(0);
        active.setValue(false);
    }

    public boolean update() {
        if(enemyOnWall()) {
            enemies.forEach(Enemy::descend);
            enemies.forEach(e -> e.setDirection(e.getDirection() * -1));
            enemies.forEach(Enemy::move);
        }
        else
            enemies.forEach(Enemy::move);
        lasers.forEach(Laser::move);
        lasers.forEach(this::checkLaser);
        return isActive();
    }

    private void checkLaser(Laser laser) {
        int top = laser.getRow();
        int bottom = top + LASER_LENGTH;
        int col = laser.getColumn();

        //naraz do hrace -> konec (pridat zivoty)

        if((bottom > player1.getRow()) && (bottom < player1.getRow() + PLAYER_LENGTH) && (col > player1.getColumn()) && (col < player1.getColumn() + PLAYER_WIDTH)){
            setActive(false);
            return;
        }

        //naraz do aliena -> dead alien
        for(Enemy enemy: enemies) {
            if ((top > enemy.getRow()) && (top < enemy.getRow() + ENEMY_LENGTH) && (col > enemy.getColumn()) && (col < enemy.getColumn() + ENEMY_WIDTH)) {
                enemy.setActive(false);
                return;
            }
        }

        //naraz do stitu -> dead stit (pridat rozbijeni stitu)
        for(Shield shield: shields) {
            if ((bottom > shield.getRow()) && (top < shield.getRow() + SHIELD_LENGTH) && (col > shield.getColumn()) && (col < shield.getColumn() + SHIELD_WIDTH)) {
                shield.setActive(false);
                return;
            }
        }

        //naraz konec/zacatek herni plochy -> zmizi
        if ((bottom == ROWS)) {
            lasers.remove(laser);
        }
    }

    private boolean enemyOnWall() {
        for(Enemy enemy: enemies) {
            if(enemy.getColumn() + ENEMY_WIDTH == COLUMNS)
                return true;
        }
        return false;
    }

    //abstrahovat mby?
    public void moveLeft() {
        if (!isActive()) return;
        int playerCol = player1.getColumn();
        if (playerCol > 0) player1.setColumn(playerCol - 1);
    }

    public void moveRight() {
        if (!isActive()) return;
        int playerCol = player1.getColumn();
        if (playerCol + PLAYER_WIDTH < COLUMNS) player1.setColumn(playerCol + 1);
    }

    public void playerShoot() {
        lasers.add(player1.shoot());
    }

    public List<Enemy> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }

    public int getScore() {
        return score.get();
    }

    public IntegerProperty scoreProperty() {
        return score;
    }


    public BooleanProperty activeProperty() {
        return active;
    }

    public List<Shield> getShields() {
        return Collections.unmodifiableList(shields);
    }

    public Player getPlayer1() {
        return player1;
    }

    public void addLaser(Laser laser) {
        this.lasers.add(laser);
    }

    public final boolean isActive() {
        return this.activeProperty().get();
    }

    public final void setActive(final boolean active) {
        this.activeProperty().set(active);
    }
}
