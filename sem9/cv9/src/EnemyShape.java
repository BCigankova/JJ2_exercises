import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EnemyShape extends Pane {
    public EnemyShape(Enemy enemy) {
        Rectangle baseForNow = new Rectangle(GameState.ENEMY_WIDTH * SpaceInvadersApp.UNIT_SIZE, GameState.ENEMY_LENGTH* SpaceInvadersApp.UNIT_SIZE, Color.GREEN);

        baseForNow.xProperty().bind(enemy.columnProperty().multiply(SpaceInvadersApp.UNIT_SIZE));   //pokud je stit na 10 pozici, je na 10 * unit_size pixelu, pokud se pohne o 1, pohne se o 1 * unit_size
        baseForNow.yProperty().bind(enemy.rowProperty().multiply(SpaceInvadersApp.UNIT_SIZE));

        baseForNow.visibleProperty().bind(enemy.activeProperty());
        this.visibleProperty().bind(enemy.activeProperty());

        getChildren().add(baseForNow);
    }
}
