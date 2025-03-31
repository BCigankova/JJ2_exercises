import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class CipherUIPane extends GridPane {
    private final CipherModel model = new CipherModel(this);

    public CipherUIPane() {

        //reverse

        Button btnReverse = new Button("Reverse");
        btnReverse.setOnAction(e -> model.reverse());
        this.add(btnReverse, 3, 1, 1, 1);

        //encrypt

        Text txtEncrypt = new Text("Plaintext:");
        this.add(txtEncrypt, 0, 2, 1, 1);

        TextField inputEncrypt = new TextField();
        this.add(inputEncrypt, 0, 3, 4, 1);

        Button btnEncrypt = new Button("Encrypt");
        btnEncrypt.setOnAction(e -> model.encrypt());
        this.add(btnEncrypt, 4, 3, 1, 1);


        //decrypt

        Text txtDecrypt = new Text("Ciphertext:");
        this.add(txtDecrypt, 0, 5, 1, 1);

        TextField inputDecrypt = new TextField();
        inputDecrypt.setDisable(true);
        this.add(inputDecrypt, 0, 6, 4, 1);

        Button btnDecrypt = new Button("Decrypt");
        btnDecrypt.setOnAction(e -> model.decrypt());
        btnDecrypt.setDisable(true);
        this.add(btnDecrypt, 4, 6, 1, 1);

        //key
        Text txtKey = new Text("Key: " + model.getKeyValue());
        this.add(txtKey, 0, 7, 1, 1);

        Button btnChangeKey = new Button("Change key");
        btnChangeKey.setOnAction(e -> model.changeKey());
        this.add(btnChangeKey, 1, 7, 1, 1);

        //menu action

        MenuItem mnuEncrypt = new MenuItem("_Encrypt");
        mnuEncrypt.setAccelerator(new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_DOWN));
        mnuEncrypt.setOnAction(e -> model.encrypt());

        MenuItem mnuDecrypt = new MenuItem("_Decrypt");
        mnuDecrypt.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN));
        mnuDecrypt.setOnAction(e -> model.decrypt());

        Menu menuAction = new Menu("_Action");
        menuAction.setMnemonicParsing(true);
        menuAction.getItems().addAll(mnuEncrypt, new SeparatorMenuItem(), mnuDecrypt);

        //menu input

        MenuItem mnuPlaintext = new MenuItem("_Plaintext");
        mnuPlaintext.setAccelerator(new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_DOWN));
        mnuPlaintext.setOnAction(e -> model.encrypt());

        MenuItem mnuCiphertext = new MenuItem("_Ciphertext");
        mnuCiphertext.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN));
        mnuCiphertext.setOnAction(e -> model.decrypt());

        Menu menuInput = new Menu("_Input");
        menuInput.setMnemonicParsing(true);
        menuInput.getItems().addAll(mnuPlaintext, new SeparatorMenuItem(), mnuCiphertext);

        //menu help

        MenuItem mnuAbout = new MenuItem("About");
        mnuAbout.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        mnuAbout.setOnAction(e -> model.about());

        MenuItem mnuExit = new MenuItem("_Exit");
        mnuExit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        mnuExit.setOnAction(e -> model.exit());

        Menu menuHelp = new Menu("_Help");
        menuHelp.setMnemonicParsing(true);
        menuHelp.getItems().addAll(mnuAbout, new SeparatorMenuItem(), mnuExit);

        //menubar

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menuAction, menuInput, menuHelp);

        FlowPane menupane = new FlowPane(menuBar);
        this.add(menupane, 0, 0, 4, 1);

    }
}
