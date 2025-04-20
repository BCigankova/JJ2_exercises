package cv9.spaceInvaders;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class ShieldShape extends Pane {
    public ShieldShape(Shield shield) {

        Polygon shieldShape = new Polygon();
        shieldShape.getPoints().addAll(
                0.0, 0.0,
                (double) GameState.SHIELD_WIDTH * SpaceInvadersController.UNIT_SIZE, 0.0,
                (double) GameState.SHIELD_WIDTH * SpaceInvadersController.UNIT_SIZE, (double) GameState.SHIELD_LENGTH * SpaceInvadersController.UNIT_SIZE,
                ((double) GameState.SHIELD_WIDTH - 10.0) * SpaceInvadersController.UNIT_SIZE, (double) GameState.SHIELD_LENGTH * SpaceInvadersController.UNIT_SIZE,
                ((double) GameState.SHIELD_WIDTH - 10.0) * SpaceInvadersController.UNIT_SIZE, ((double) GameState.SHIELD_LENGTH - 10.0) * SpaceInvadersController.UNIT_SIZE,
                10.0 * SpaceInvadersController.UNIT_SIZE, ((double) GameState.SHIELD_LENGTH - 10.0) * SpaceInvadersController.UNIT_SIZE,
                10.0 * SpaceInvadersController.UNIT_SIZE, (double) GameState.SHIELD_LENGTH * SpaceInvadersController.UNIT_SIZE,
                0.0, (double) GameState.SHIELD_LENGTH * SpaceInvadersController.UNIT_SIZE
        );
        shieldShape.setFill(Color.GREEN);
        shieldShape.layoutXProperty().bind(shield.columnProperty().multiply(SpaceInvadersController.UNIT_SIZE));
        shieldShape.layoutYProperty().bind(shield.rowProperty().multiply(SpaceInvadersController.UNIT_SIZE));
        shieldShape.visibleProperty().bind(shield.activeProperty());
        shieldShape.opacityProperty().bind(shield.livesProperty().divide(GameState.SHIELD_LIVES));

        /*
        Rectangle baseForNow = new Rectangle(GameState.SHIELD_WIDTH * SpaceInvadersController.UNIT_SIZE, GameState.SHIELD_LENGTH * SpaceInvadersController.UNIT_SIZE, Color.GREEN);
        baseForNow.xProperty().bind(shield.columnProperty().multiply(SpaceInvadersController.UNIT_SIZE));   //pokud je stit na 10 pozici, je na 10 * unit_size pixelu, pokud se pohne o 1, pohne se o 1 * unit_size
        baseForNow.yProperty().bind(shield.rowProperty().multiply(SpaceInvadersController.UNIT_SIZE));
        baseForNow.visibleProperty().bind(shield.activeProperty());
        baseForNow.opacityProperty().bind(shield.livesProperty().divide(GameState.SHIELD_LIVES));

         */

        getChildren().add(shieldShape);
    }
}
