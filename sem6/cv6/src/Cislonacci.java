import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;

public class Cislonacci extends JFrame {
    //main panel
    private JPanel mainPanel;

    //input sequence name
    private JPanel inputPanel;
    private JLabel inputNameLabel;
    private JTextField inputNameField;  //check input

    //input number to calculate
    private JLabel inputNumberLabel;
    private JTextField inputNumberField;    //checkinput

    //buttons + description
    private JPanel calculationPanel;
    private JLabel[] calculationStateLabels;

    private JButton startButton;
    private JButton stopButton;  //stop nebo cancel?

    //output panel
    private JPanel outputPanel;
    private JLabel outputLabel;

    private JScrollPane outputScroll;
    private JList<String> outputList;
    private DefaultListModel<String> listModel;

    private SeqController control;

    public Cislonacci () {

        //window init
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(700, 300));
        this.pack();

        //mainpanel init
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //input init
        inputPanel = new JPanel(new GridLayout(2, 2, 20, 20));

        inputNameLabel = new JLabel("input sequence name:");
        inputPanel.add(inputNameLabel);

        inputNumberLabel = new JLabel("input number to calculate");
        inputPanel.add(inputNumberLabel);

        inputNameField = new JTextField(30);
        inputPanel.add(inputNameField);

        inputNumberField = new JTextField(30);
        inputPanel.add(inputNumberField);

        //calc init
        calculationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        //calc info
        calculationStateLabels = new JLabel[4];
        calculationStateLabels[0] = new JLabel("-");
        calculationStateLabels[1] = new JLabel("Processing");
        calculationStateLabels[2] = new JLabel("Stopped");
        calculationStateLabels[3] = new JLabel("Done");
        calculationPanel.add(calculationStateLabels[0]);  //zakladni

        //calc start
        startButton = new JButton("Start");
        startButton.addActionListener(e -> control.computeAction(inputNameField.getText(), inputNumberField.getText()));
        calculationPanel.add(startButton);

        //calc stop
        stopButton = new JButton("Cancel");
        stopButton.addActionListener(e -> control.stopAction());
        stopButton.setEnabled(false);
        calculationPanel.add(stopButton);

        //output init
        outputPanel = new JPanel(new FlowLayout());  //tu byl boxlayout

        outputLabel = new JLabel("Results: ");
        outputPanel.add(outputLabel);

        //output list
        listModel = new DefaultListModel<>();
        outputList = new JList<>(listModel);
        outputScroll = new JScrollPane(outputList);
        outputPanel.add(outputScroll);

        //add panels to main panel
        mainPanel.add(inputPanel);
        mainPanel.add(calculationPanel);
        mainPanel.add(outputPanel);

        //finish init
        this.setContentPane(mainPanel);
        this.setVisible(true);

        //control
        control = new SeqController(this);  //do konstruktoru neco?
    }

    protected void setStateCalculation(int state) {
        calculationPanel.remove(0);
        calculationPanel.add(calculationStateLabels[state], 0);
        setContentPane(mainPanel);

        if(state == 1) {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        }

        if(state == 2 || state == 3) {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }

    protected void displayErrorDialog(String what) {
        JOptionPane.showMessageDialog(Cislonacci.this, what.equals("seq") ?
                        "Please input valid sequence" : "Please input valid integer",
                "Input error",
                JOptionPane.ERROR_MESSAGE);
    }

    protected void nullInputNameField() {
        inputNameField.setText("");
    }

    protected void nullInputNumberField() {
        inputNumberField.setText("");
    }

    public void addFormattedToList(BigInteger bigint, String seq, int n) {
        listModel.addElement("Sequence: " + seq + ", index: " + n + ", result: " + bigint.toString());
    }

}
