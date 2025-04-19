import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.scene.shape.Shape;

abstract public class HittableObject {
    private final IntegerProperty row;
    private final IntegerProperty column;
    private final BooleanProperty active;
    private Shape shape;


    protected HittableObject(int row, int column) {
        this.row = new SimpleIntegerProperty(row);
        this.column = new SimpleIntegerProperty(column);
        this.active = new SimpleBooleanProperty(true);
    }

    public boolean isHit(Shape shp) {
        Bounds b1 = this.getShape().localToScene(this.getShape().getBoundsInLocal());
        Bounds b2 = shp.localToScene(shp.getBoundsInLocal());
        return b1.intersects(b2) && isActive();
    }

    public final IntegerProperty rowProperty() {
        return this.row;
    }

    public final int getRow() {return this.rowProperty().get();}

    public final void setRow(final int row) {this.rowProperty().set(row);}

    public final IntegerProperty columnProperty() {
        return this.column;
    }

    public final int getColumn() {
        return this.columnProperty().get();
    }

    public final void setColumn(final int column) {
        this.columnProperty().set(column);
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

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
