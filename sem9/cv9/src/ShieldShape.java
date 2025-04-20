import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class ShieldShape extends Pane {

    //pozici mam uz v shieldu a jen nabinduju
    //co kdybych je nabindovala na sebe navzajem a pak mela jedno x, y pro bind se shieldem? asi lepsi reseni
    //+ nastavitelny pocet stitu (shield rows/cols)
    //... vsak to muze byt POLYGON
    public ShieldShape(Shield shield) {
        /*Rectangle base = new Rectangle(UNIT_SIZE * GameState.SHIELD_WIDTH, UNIT_SIZE * (GameState.SHIELD_WIDTH / 3), Color.GREEN);
        base.xProperty().bind(shield.columnProperty().multiply(UNIT_SIZE));
        base.yProperty().bind(shield.rowProperty().multiply(UNIT_SIZE - (GameState.SHIELD_WIDTH / 3)));

        Rectangle top = new Rectangle(UNIT_SIZE * , UNIT_SIZE * 5, Color.GREEN);

        Polygon rightTri = new Polygon()

         */


        Rectangle baseForNow = new Rectangle(GameState.SHIELD_WIDTH * SpaceInvadersApp.UNIT_SIZE, GameState.SHIELD_LENGTH * SpaceInvadersApp.UNIT_SIZE, Color.GREEN);
        baseForNow.xProperty().bind(shield.columnProperty().multiply(SpaceInvadersApp.UNIT_SIZE));   //pokud je stit na 10 pozici, je na 10 * unit_size pixelu, pokud se pohne o 1, pohne se o 1 * unit_size
        baseForNow.yProperty().bind(shield.rowProperty().multiply(SpaceInvadersApp.UNIT_SIZE));

        baseForNow.visibleProperty().bind(shield.activeProperty());

        getChildren().add(baseForNow);
    }
}
