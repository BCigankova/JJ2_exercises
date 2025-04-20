import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlayerShape extends Pane {
    public PlayerShape(Player player) {

        Rectangle baseForNow = new Rectangle(GameState.PLAYER_WIDTH * SpaceInvadersApp.UNIT_SIZE, GameState.PLAYER_LENGTH * SpaceInvadersApp.UNIT_SIZE, Color.GREEN);
        baseForNow.xProperty().bind(player.columnProperty().multiply(SpaceInvadersApp.UNIT_SIZE));   //pokud je stit na 10 pozici, je na 10 * unit_size pixelu, pokud se pohne o 1, pohne se o 1 * unit_size
        baseForNow.yProperty().bind(player.rowProperty().multiply(SpaceInvadersApp.UNIT_SIZE));

        baseForNow.visibleProperty().bind(player.activeProperty());

        getChildren().add(baseForNow);

    }
}
