import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.Rectangle;

public class Enemy {

    private final BooleanProperty active;
    private final int type;

    private final IntegerProperty row;
    private final IntegerProperty column;

    private int direction;    //1 doprava -1 doleva

    public Enemy(int type, int row, int column) {
        super();
        this.active = new SimpleBooleanProperty(true);
        this.type = type;
        this.row = new SimpleIntegerProperty(row);
        this.column = new SimpleIntegerProperty(column);
        this.direction = 1;
    }

    public void move() {
        setColumn(getColumn() + direction);
    }    //update

    public void descend() {
        setRow((int) (getRow() + GameState.ENEMY_LENGTH));
    }

    //presunout spis do gamestate? i v playeru

    public Laser shoot() {
        Laser laser = new Laser((int) (getRow() + GameState.ENEMY_WIDTH / 2), (int) (getColumn() + GameState.ENEMY_LENGTH), 1);
        LaserShape laserShape = new LaserShape(laser);
        return laser;
    }



    public int getType() {
        return type;
    }

    public final BooleanProperty activeProperty() {
        return this.active;
    }

    public final boolean isActive() {
        return this.activeProperty().get();
    }

    public final void setActive(final boolean active) {
        this.activeProperty().set(active);
    }

    public final IntegerProperty rowProperty() {
        return this.row;
    }

    public final int getRow() {
        return this.rowProperty().get();       //pouzivam getter
    }

    public final void setRow(final int row) {
        this.rowProperty().set(row);
    }

    public final IntegerProperty columnProperty() {
        return this.column;
    }

    public final int getColumn() {
        return this.columnProperty().get();
    }

    public final void setColumn(final int column) {
        this.columnProperty().set(column);
    }

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int dir) {
        this.direction = dir;
    }
}
