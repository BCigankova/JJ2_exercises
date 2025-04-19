import javafx.scene.shape.Shape;

public class Enemy extends HittableObject{
    private final int type;
    private int direction;    //1 doprava -1 doleva

    public Enemy(int type, int row, int column) {
        super(row, column);
        this.type = type;
        this.direction = 1;
    }

    public void move() {
        setColumn(getColumn() + direction);
    }    //update

    public void descend() {
        setRow( (getRow() + GameState.ENEMY_LENGTH));
    }

    //presunout spis do gamestate? i v playeru

    public Laser shoot() {
        return new Laser( (getRow() + GameState.ENEMY_WIDTH / 2),  (getColumn() + GameState.ENEMY_LENGTH), 1);
    }

    public int getType() {
        return type;
    }

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int dir) {
        this.direction = dir;
    }

}
