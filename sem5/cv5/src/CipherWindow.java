import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class CipherWindow extends JFrame {
    private JPanel mainPanel;
    private JButton reverse;   //kam ho placnu?

    private JPanel plaintextPanel;
    private JLabel abovePlaintext;
    private JTextField plaintext;    //chci aby tam slo i neslo psat... jak?
    private JButton encryptButton;

    private JPanel ciphertextPanel;
    private JLabel aboveCiphertext;
    private JTextField ciphertext;
    private JButton decryptButton;

    private JMenuBar mainMenu;

    private JMenu menuAction;
    private JMenuItem encrypt;
    private JMenuItem decrypt;
    private JMenuItem exit;

    private JMenu menuInput;
    private JMenuItem plaintextItem;
    private JMenuItem ciphertextItem;

    private JMenu menuHelp;
    private JMenuItem help;

    private int keyValue;
    private JPanel keyPanel;
    private JTextArea keyText;
    private JButton changeKey;


    public CipherWindow() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 300));
        this.pack();
        this.setVisible(true);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); //zarovna nalevo doufam

        this.reverse = new JButton("Reverse");
        reverse.addActionListener(this::onReverseAction);
        mainPanel.add(reverse);

        this.plaintextPanel = new JPanel(new BorderLayout(20, 20));
        this.plaintext = new JTextField(50);      //line wrapping
        this.abovePlaintext = new JLabel("Plaintext: ");

        this.encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(this::onEncryptAction);
        encryptButton.setBackground(Color.white);

        plaintextPanel.add(abovePlaintext, BorderLayout.NORTH);   //dodelat jak to ma vypadat dobre
        plaintextPanel.add(plaintext, BorderLayout.WEST);
        plaintextPanel.add(encryptButton, BorderLayout.CENTER);
        mainPanel.add(plaintextPanel);

        this.ciphertextPanel = new JPanel(new BorderLayout(20, 20));
        this.ciphertext = new JTextField(50);
        ciphertext.setEditable(false);
        this.aboveCiphertext = new JLabel("Ciphertext: ");

        this.decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(this::onDecryptAction);
        decryptButton.setEnabled(false);
        decryptButton.setBackground(Color.gray);

        ciphertextPanel.add(aboveCiphertext, BorderLayout.NORTH);
        ciphertextPanel.add(ciphertext, BorderLayout.WEST);
        ciphertextPanel.add(decryptButton, BorderLayout.CENTER);
        mainPanel.add(ciphertextPanel);

        this.mainMenu = new JMenuBar();

        this.menuAction = new JMenu("Action");

        this.encrypt = new JMenuItem("Encrypt");
        encrypt.addActionListener(this::onEncryptAction);
        encrypt.setMnemonic(KeyEvent.VK_E);

        this.decrypt = new JMenuItem("Decrypt");
        decrypt.addActionListener(this::onDecryptAction);
        decrypt.setEnabled(false);
        decrypt.setMnemonic(KeyEvent.VK_D);

        this.exit = new JMenuItem("Exit");
        exit.addActionListener(this::onExitAction);

        menuAction.add(encrypt);
        menuAction.add(decrypt);
        menuAction.add(exit);

        this.menuInput = new JMenu("Input");
        this.plaintextItem = new JMenuItem("Plaintext");
        plaintextItem.setEnabled(false);
        plaintextItem.addActionListener(this::onReverseAction);
        plaintextItem.setMnemonic(KeyEvent.VK_P);

        this.ciphertextItem = new JMenuItem("Ciphertext");  //jak tu pro reverse action?
        ciphertextItem.addActionListener(this::onReverseAction);
        ciphertextItem.setMnemonic(KeyEvent.VK_K);
        menuInput.add(plaintextItem);
        menuInput.add(ciphertextItem);

        this.menuHelp = new JMenu("Help");
        this.help = new JMenuItem("About Caesar's cipher (link)");
        help.addActionListener(this::onHelpAction);
        menuHelp.add(help);

        //mnemonic
        menuAction.setMnemonic(KeyEvent.VK_1);
        menuInput.setMnemonic(KeyEvent.VK_2);
        menuInput.setMnemonic(KeyEvent.VK_3);

        mainMenu.add(menuAction);
        mainMenu.add(menuInput);
        mainMenu.add(menuHelp);
        this.setJMenuBar(mainMenu);

        this.keyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keyValue = 3;
        this.keyText = new JTextArea("Key: " + keyValue);
        this.changeKey = new JButton("Change Key");
        changeKey.addActionListener(this::onChangeKeyAction);

        keyPanel.add(keyText);
        keyPanel.add(changeKey);
        mainPanel.add(keyPanel);

        this.setContentPane(mainPanel);
    }

    private void onReverseAction(ActionEvent e) {
        JPanel inputDisable = (JPanel) mainPanel.getComponent(1);
        setInput(false);
        setInput(true);
        setMenuActionInput();
        mainPanel.add(mainPanel.getComponent(2), 1);
        mainPanel.add(inputDisable, 2);
        setContentPane(mainPanel);
    }

    private void setInput(boolean what) {
        JPanel input = (JPanel) mainPanel.getComponent(what ? 2 : 1);
        JTextField inputField = (JTextField) input.getComponent(1);
        inputField.setEditable(what);
        JButton inputButton = (JButton) input.getComponent(2);
        inputButton.setEnabled(what);
        inputButton.setBackground(what ? Color.white : Color.gray);
        if(!what)
            inputField.setText("");
    }

    private void setMenuActionInput() {
        if(encrypt.isEnabled()) {
            encrypt.setEnabled(false);
            decrypt.setEnabled(true);
            ciphertextItem.setEnabled(false);
            plaintextItem.setEnabled(true);
        }
        else {
            encrypt.setEnabled(true);
            decrypt.setEnabled(false);
            ciphertextItem.setEnabled(true);
            plaintextItem.setEnabled(false);
        }
    }

    private void onEncryptAction(ActionEvent e) {
        String ciphertextRes = encrypt(plaintext.getText());
        if(ciphertextRes == null)
            displayInputErrorDialog();
            //dialog error characters, lepsi cerveny ramecek a hlaska ze je to spatne
        else
            ciphertext.setText(ciphertextRes);
    }

    private void onDecryptAction(ActionEvent e) {
        String plaintextRes = decrypt(ciphertext.getText());
        if(plaintextRes == null)
            displayInputErrorDialog();
            //dialog error characters, lepsi cerveny ramecek a hlaska ze je to spatne
        else
            plaintext.setText(plaintextRes);
    }

    private void displayInputErrorDialog() {
        JOptionPane.showMessageDialog(CipherWindow.this,
                "Please input only uppercase and lowercase characters, a space and a dot.",
                "Input error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void onChangeKeyAction(ActionEvent e) {
        String input = displayInputKeyDialog();
        if(input == null)
            return;
        while (!checkKeyInput(input)) {
            input = displayInputKeyErrorDialog();
            if(input == null)
                return;
        }
        keyValue = Integer.parseInt(input);
        keyText.setText("Key: " + keyValue);
    }

    private boolean checkKeyInput(String input) {
        if ((input != null) && (!input.isEmpty())) {
            try {
                int newKey = Integer.parseInt(input);
                if (newKey < 0 || newKey > 26)
                    throw new Exception();
                else
                    return true;
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }

    private String displayInputKeyDialog() {
        return (String)JOptionPane.showInputDialog(
                CipherWindow.this,
                "Please input a new key",
                "Change key",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");
    }

    private String displayInputKeyErrorDialog() {
        return (String) JOptionPane.showInputDialog(
                CipherWindow.this,
                "Wrong input, please input a number between 0 and 26",
                "Change key",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");
    }

    private void onHelpAction(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(new URI("https://en.wikipedia.org/wiki/Caesar_cipher"));
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private void onExitAction(ActionEvent e) {
        System.exit(0);
    }

    private String encrypt(String plaintext) {
        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i++) {
            char letter = plaintext.charAt(i);

            if (Character.isUpperCase(letter))
                ciphertext.append((char) ((letter + keyValue - 'A') % 26 + 'A'));
            else if (Character.isLowerCase(letter))
                ciphertext.append((char) ((letter + keyValue - 'a') % 26 + 'a'));
            else if (letter == ' ' || letter == '.') {
                ciphertext.append(letter);
            } else
                return null;     //dialog okno s errorem?
        }
        return ciphertext.toString();
    }

    private String decrypt(String ciphertext) {
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i++) {
            char letter = ciphertext.charAt(i);
            if
                (Character.isUpperCase(letter)) plaintext.append((char) ((letter - keyValue - 'A' + 26) % 26 + 'A'));
            else if (Character.isLowerCase(letter))
                plaintext.append((char) ((letter - keyValue - 'a' + 26) % 26 + 'a'));
            else if
                (letter == ' ' || letter == '.') plaintext.append(letter);
            else {
                return null;
            }
        }
        return plaintext.toString();
    }

}
