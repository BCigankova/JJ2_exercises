import javafx.scene.shape.Shape;

public class Shield extends HittableObject {

    public Shield(int col) {
        super((GameState.ROWS - GameState.PLAYER_LENGTH - 10 - GameState.SHIELD_LENGTH), col);
    }
}