import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Laser {

    private final IntegerProperty row;
    private final IntegerProperty column;
    private int direction;


    public Laser(int row, int col, int dir) {
        this.row = new SimpleIntegerProperty(row);
        this.column = new SimpleIntegerProperty(col);
        this.direction = dir;
    }

    public void move() {
        setRow(getRow() + direction);
    }

    public final IntegerProperty rowProperty() {
        return this.row;
    }

    public final int getRow() {
        return this.rowProperty().get();
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

    public int getDirection() {return this.direction;}

    public void setDirection(int dir) {this.direction = dir;}
}
