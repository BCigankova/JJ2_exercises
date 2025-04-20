package cv9.spaceInvaders;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Player extends HittableObject {
    private final IntegerProperty lives;

    protected Player(int lives, int row, int column) {
        super(row, column);
        this.lives = new SimpleIntegerProperty(lives);
    }

    public Laser shoot() {
        return new Laser(getRow() - GameState.LASER_LENGTH - 1,getColumn() + (GameState.PLAYER_WIDTH / 2), -1);
    }

    public boolean isDead() {
        setLives(getLives() - 1);
        return getLives() == 0;
    }

    public final IntegerProperty livesProperty() {
        return this.lives;
    }

    public final int getLives() {
        return this.livesProperty().get();
    }

    public final void setLives(final int lives) {
        this.livesProperty().set(lives);
    }

}