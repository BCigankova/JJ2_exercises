import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.random;

public class GameState {
    private final List<Enemy> enemies;
    private final List<Shield> shields;
    private final ListProperty<Laser> lasers;
    private final Player player1;

    public static final int ENEMY_WEAK = 10;
    public static final int ENEMY_AVG = 20;
    public static final int ENEMY_STRONG = 30;


    public static final int ROWS = 200;
    public static final int COLUMNS = 250;

    public static final int SHIELD_WIDTH = 10;
    public static final int SHIELD_LENGTH = 10;

    public static final int SHIELD_COLS = 5;
    public static final int SHIELD_SPACING = 10;


    public static final int PLAYER_WIDTH = 15;
    public static final int PLAYER_LENGTH = 15;

    public static final int PLAYER_LIVES = 3;

    public static final int ENEMY_WIDTH = 10;
    public static final int ENEMY_LENGTH = 10;
    public static final int ENEMY_SPACING = 5;


    public static final int ENEMY_ROWS = 3;
    public static final int ENEMY_COLS = 11;

    public static final int ENEMY_DELAY = 50;

    public static final int LASER_LENGTH = 5;
    public static final int SHOOT_ENEMY_DELAY = 50000;
    private int timeElapsedToShoot = 0;

    private final int delay;


    private final IntegerProperty score;

    private final BooleanProperty active;


    public GameState(int delay) {
        this.enemies = new ArrayList<>();
        initializeEnemies();

        this.shields = new ArrayList<>();
        initializeShields();

        this.lasers = new SimpleListProperty<>(FXCollections.observableArrayList());

        this.player1 = new Player(PLAYER_LIVES,ROWS - PLAYER_LENGTH - 5, (COLUMNS - PLAYER_WIDTH) / 2);  //5 odsazeni at to lip vypada

        this.score = new SimpleIntegerProperty(0);
        this.active = new SimpleBooleanProperty(false);

        this.delay = delay;
    }

    private void initializeEnemies() {
        int allEnemies =  ENEMY_COLS * ENEMY_WIDTH + (ENEMY_COLS - 1) * ENEMY_SPACING;   //dat enemy spacing
        for(int i = 0; i < ENEMY_ROWS; i++)
            for(int j = 0; j < ENEMY_COLS; j++)
                enemies.add(new Enemy(0, ENEMY_DELAY,  ENEMY_LENGTH + i * (ENEMY_LENGTH + ENEMY_SPACING),  (COLUMNS - allEnemies) / 2 + j * (ENEMY_WIDTH + ENEMY_SPACING)));
    }

    private void initializeShields() {
        int allShields =  SHIELD_COLS * SHIELD_WIDTH + (SHIELD_COLS - 1) * SHIELD_SPACING;
        for(int i = 0; i < SHIELD_COLS; i++)
            shields.add(new Shield((COLUMNS - allShields) / 2 + i * (SHIELD_WIDTH + SHIELD_SPACING)));
    }

    private void setDefaultEnemyPos(Enemy enemy, int i, int j) {
        enemy.setRow(ENEMY_LENGTH + i * (ENEMY_LENGTH + ENEMY_SPACING));
        enemy.setColumn((COLUMNS - (ENEMY_COLS * ENEMY_WIDTH + (ENEMY_COLS - 1) * ENEMY_SPACING)) / 2 + j * (ENEMY_WIDTH + ENEMY_SPACING));
    }

    public void setDefaultValues() {
        for(int i = 0; i < ENEMY_ROWS; i++)
            for(int j = 0; j < ENEMY_COLS; j++)
                setDefaultEnemyPos(enemies.get(i * ENEMY_COLS + j), i, j);

        enemies.forEach(e -> e.setActive(true));
        enemies.forEach(e -> e.setMoveInterval(ENEMY_DELAY));
        shields.forEach(s -> s.setActive(true));

        player1.setColumn((COLUMNS - PLAYER_WIDTH) / 2);
        player1.setRow(ROWS - PLAYER_LENGTH - 5);
        player1.setActive(true);

        lasers.forEach(l -> l.setActive(false));
        lasers.clear();

        score.setValue(0);
        active.setValue(false);
    }

    public boolean update() {
        enemies.forEach(enemy -> enemy.updateTimeSinceLastMoved(delay));
        if(enemies.stream().filter(HittableObject::isActive).anyMatch(this::isEnemyOnWall)) {
            enemies.forEach(Enemy::descend);
        }
        enemies.forEach(Enemy::move);
        if(enemies.stream().filter(HittableObject::isActive).anyMatch(e -> (e.getRow() + ENEMY_LENGTH == ROWS) || (e.isHit(player1.getShape())))) {
            setActive(false);
            return isActive();
        }

        //vyfiltruju si alieny podle toho, jestli jsou prvni v rade: jestli je za alienem nejaky, ktery ma vetsi row, tak ho neberu
        //pak z nich nahodne vyberu ty, kter vystreli, po urcitem casovem useku

        timeElapsedToShoot += delay;
        if(timeElapsedToShoot >= SHOOT_ENEMY_DELAY) {
            timeElapsedToShoot = 0;
            List<Enemy> shootable = new LinkedList<>();
            for(Enemy enemy: enemies) {
                if(enemies.stream().filter(HittableObject::isActive).filter(e -> e.getColumn() == enemy.getColumn()).noneMatch(e -> e.getRow() > enemy.getRow()))
                    shootable.add(enemy);
            }
            lasers.add(shootable.get((int) (random() * (shootable.size() - 1))).shoot());
        }
        lasers.forEach(Laser::move);
        lasers.stream().filter(HittableObject::isActive).forEach(this::checkHit);
        return isActive();
    }

    private void checkHit(Laser laser) {
        if(player1.isHit(laser.getShape())) {
            if(player1.isDead()) {
                player1.setActive(false);
                this.setActive(false);
                return;
            }
            laser.setActive(false);
            return;
        }
        for(Enemy enemy: enemies) {
            if (enemy.isHit(laser.getShape())) {
                enemy.setActive(false);
                laser.setActive(false);
                increaseScore();
                return;
            }
        }

        //naraz do stitu -> dead stit (pridat rozbijeni stitu/zivoty?)
        for(Shield shield: shields) {
            if (shield.isHit(laser.getShape())) {
                shield.setActive(false);
                laser.setActive(false);
                return;
            }
        }

        //naraz konec/zacatek herni plochy -> zmizi
        if (laser.getRow() == 0 || laser.getRow() + LASER_LENGTH == ROWS) {
            laser.setActive(false);
        }
    }

    private void increaseScore() {
        score.set(score.get() + 1);
    }

    private boolean isEnemyOnWall(Enemy enemy) {    //check i druhe strany xd
        return (enemy.getColumn() + ENEMY_WIDTH == COLUMNS) || (enemy.getColumn() == 0);
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
        Laser laser = player1.shoot();
        addLaser(laser);
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
        return this.player1;
    }

    public final void addLaser(Laser laser) {
        this.lasersProperty().add(laser);
    }

    public final ListProperty<Laser> lasersProperty() {    //final
        return this.lasers;
    }

    public final List<Laser> getLasers() {
        return this.lasersProperty().get();
    }


    public final boolean isActive() {
        return this.activeProperty().get();
    }

    public final void setActive(final boolean active) {
        this.activeProperty().set(active);
    }
}
