import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Enemy {

    private final BooleanProperty active;
    private final int type;

    private final IntegerProperty row;
    private final IntegerProperty column;

    public Enemy(int type, int row, int column) {
        super();
        this.active = new SimpleBooleanProperty(true);
        this.type = type;
        this.row = new SimpleIntegerProperty(row);
        this.column = new SimpleIntegerProperty(column);
    }

    public void move() {
        setRow();
    }


    public int getType() {
        return type;
    }

    public boolean isActive() {
        return this.activeProperty().get();
    }

    public BooleanProperty activeProperty() {
        return this.active;
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
}
