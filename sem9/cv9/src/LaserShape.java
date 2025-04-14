import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class LaserShape extends Rectangle {
    public LaserShape(Laser laser) {
        this.xProperty().bind(laser.columnProperty().multiply(SpaceInvadersApp.UNIT_SIZE));
        this.yProperty().bind(laser.rowProperty().multiply(SpaceInvadersApp.UNIT_SIZE));
    }
}
