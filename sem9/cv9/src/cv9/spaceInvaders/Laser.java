package cv9.spaceInvaders;

public class Laser extends HittableObject {

    private final int direction;

    public Laser(int row, int col, int dir) {
        super(row, col);
        this.direction = dir;
    }

    public void move() {
        setRow(getRow() + direction);
    }
}
