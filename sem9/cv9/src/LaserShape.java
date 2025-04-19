import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LaserShape extends Pane {
    public LaserShape(Laser laser) {
        Rectangle base = new Rectangle(SpaceInvadersApp.UNIT_SIZE, GameState.LASER_LENGTH * SpaceInvadersApp.UNIT_SIZE, Color.GREEN);
        base.xProperty().bind(laser.columnProperty().multiply(SpaceInvadersApp.UNIT_SIZE));
        base.yProperty().bind(laser.rowProperty().multiply(SpaceInvadersApp.UNIT_SIZE));

        this.visibleProperty().bind(laser.activeProperty());
        base.visibleProperty().bind(laser.activeProperty());

        this.getChildren().add(base);

    }
}
