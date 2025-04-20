package cv9.spaceInvaders;

public class Enemy extends HittableObject {
    private final int type;
    private int direction;    //1 doprava -1 doleva

    private int timeSinceLastMove = 0;
    private int moveInterval;

    private final int SPEEDUP = 10;

    public Enemy(int type, int moveInterval, int row, int column) {
        super(row, column);
        this.type = type;
        this.direction = 1;
        this.moveInterval = moveInterval;
    }

    public void move() {
        if (this.timeSinceLastMove >= this.moveInterval) {
            setColumn(getColumn() + direction);
            this.timeSinceLastMove = 0;
        }
    }

    public void updateTimeSinceLastMoved(int timeToAdd) {
        this.timeSinceLastMove += timeToAdd;
    }

    public void descend() {
        if (this.timeSinceLastMove >= this.moveInterval) {
            setRow(getRow() + GameState.ENEMY_LENGTH);
            setDirection(getDirection() * -1);
            if(moveInterval - SPEEDUP <= 0)
                moveInterval = 0;
            else
                moveInterval -= SPEEDUP;
        }
    }

    public Laser shoot() {
        return new Laser(getRow() + GameState.ENEMY_LENGTH + 5,getColumn() + GameState.ENEMY_WIDTH / 2, 1);
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


    public long getTimeSinceLastMove() {
        return timeSinceLastMove;
    }

    public void setTimeSinceLastMove(int timeSinceLastMove) {
        this.timeSinceLastMove = timeSinceLastMove;
    }

    public int getMoveInterval() {
        return moveInterval;
    }

    public void setMoveInterval(int moveInterval) {
        this.moveInterval = moveInterval;
    }

}
