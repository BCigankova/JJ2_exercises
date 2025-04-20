package cv9.spaceInvaders;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Shield extends HittableObject {
    private final DoubleProperty lives;


    public Shield(int lives, int col) {
        super((GameState.ROWS - GameState.PLAYER_LENGTH - 10 - GameState.SHIELD_LENGTH), col);
        this.lives = new SimpleDoubleProperty(lives);      //navazat na barvu
    }

    public boolean isDead() {
        setLives(getLives() - 1);
        return getLives() == 0;
    }

    public final DoubleProperty livesProperty() {
        return this.lives;
    }

    public final double getLives() {
        return this.livesProperty().get();
    }

    public final void setLives(final double lives) {
        this.livesProperty().set(lives);
    }
}