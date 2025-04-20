package cv9.spaceInvaders;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EnemyShape extends Pane {
    public EnemyShape(Enemy enemy) {
        Rectangle baseForNow = new Rectangle(GameState.ENEMY_WIDTH * SpaceInvadersController.UNIT_SIZE, GameState.ENEMY_LENGTH* SpaceInvadersController.UNIT_SIZE, Color.GREEN);

        baseForNow.xProperty().bind(enemy.columnProperty().multiply(SpaceInvadersController.UNIT_SIZE));   //pokud je stit na 10 pozici, je na 10 * unit_size pixelu, pokud se pohne o 1, pohne se o 1 * unit_size
        baseForNow.yProperty().bind(enemy.rowProperty().multiply(SpaceInvadersController.UNIT_SIZE));

        baseForNow.visibleProperty().bind(enemy.activeProperty());

        getChildren().add(baseForNow);
    }
}
