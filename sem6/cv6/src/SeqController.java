import javax.swing.*;
import java.math.BigInteger;
import java.util.Arrays;

public class SeqController {
    private Cislonacci seqFrame;
    private SwingWorker<BigInteger, Void> sw;
    private String[] sequences = {"fibonacci", "tribonacci", "tetranacci", "pentanacci", "hexanacci", "octanacci"};

    public SeqController(Cislonacci fr) {
        this.seqFrame = fr;
    }

    public String[] getSequences() {
        return sequences;
    }

    protected void computeAction (String name, String number) {
        if(validateInput(name, number)) {
            seqFrame.setStateCalculation(1);
            sw = new SeqWorker(name, Integer.parseInt(number), this);
            sw.execute();
        }
    }

    protected void stopAction() {
        seqFrame.setStateCalculation(2);
        sw.cancel(true);
    }

    private boolean validateInput(String name, String number) {
        boolean res = true;
        if(!Arrays.asList(sequences).contains(name)) {
            seqFrame.displayErrorDialog("seq");
            seqFrame.nullInputNameField();
            res = false;
        }

        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            seqFrame.displayErrorDialog("num");
            seqFrame.nullInputNumberField();
            res = false;
        }

        return res;
    }


    protected void canceled() {
        seqFrame.setStateCalculation(2);
    }

    protected void done(BigInteger res, String seq, int n) {
        seqFrame.setStateCalculation(3);
        seqFrame.addFormattedToList(res, seq, n);
    }
}

