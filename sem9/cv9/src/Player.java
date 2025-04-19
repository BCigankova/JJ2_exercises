import javafx.scene.shape.Shape;

public class Player extends HittableObject {

    protected Player(int row, int column) {
        super(row, column);
    }

    public Laser shoot() {
        return new Laser(getRow() - GameState.LASER_LENGTH - 1,getColumn() + (GameState.PLAYER_WIDTH / 2), -1);
    }

}