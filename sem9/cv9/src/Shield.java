import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Shield {
    private final IntegerProperty row;
    private final IntegerProperty column;
    private final BooleanProperty active;


    public Shield(int col) {
        this.row = new SimpleIntegerProperty((int) (GameState.ROWS - GameState.PLAYER_LENGTH - 10 - GameState.SHIELD_LENGTH));
        this.column = new SimpleIntegerProperty(col);
        this.active = new SimpleBooleanProperty(true);
    }


    public int getRow() {
        return row.get();
    }

    public IntegerProperty rowProperty() {
        return row;
    }

    public int getColumn() {
        return column.get();
    }

    public IntegerProperty columnProperty() {
        return column;
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
}
