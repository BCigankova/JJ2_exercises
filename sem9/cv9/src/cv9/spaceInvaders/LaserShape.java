package cv9.spaceInvaders;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LaserShape extends Pane {
    public LaserShape(Laser laser) {
        Rectangle base = new Rectangle(SpaceInvadersController.UNIT_SIZE, GameState.LASER_LENGTH * SpaceInvadersController.UNIT_SIZE, Color.GREEN);
        base.xProperty().bind(laser.columnProperty().multiply(SpaceInvadersController.UNIT_SIZE));
        base.yProperty().bind(laser.rowProperty().multiply(SpaceInvadersController.UNIT_SIZE));

        base.visibleProperty().bind(laser.activeProperty());

        this.getChildren().add(base);

    }
}
