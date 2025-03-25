import javax.swing.*;
import java.math.BigInteger;
import java.util.Arrays;

public class SeqWorker extends SwingWorker<BigInteger, Void> {
    private String seq;
    private int arg;
    private SeqController control;

    public SeqWorker(String name, int number, SeqController c) {
        super();
        this.seq = name;
        this.arg = number;
        this.control = c;
    }

    @Override
    protected BigInteger doInBackground() throws Exception {
        return pocitacci(Arrays.asList(control.getSequences()).indexOf(seq) + 2, this.arg);
    }

    @Override
    protected void done() {
        try {
            if (this.isCancelled())
                control.canceled();
            else
                control.done(this.get(), this.seq, this.arg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BigInteger pocitacci(int seq, int n) {
        if(this.isCancelled())
            return null;
        if(n < seq - 1)
            return new BigInteger("0");
        if(n ==  seq - 1)
            return new BigInteger("1");
        BigInteger res =  new BigInteger("0");
        for(int i = 0; i < seq; i++) {
            res = res.add(pocitacci(seq, n - i - 1));
        }
        return res;
    }

}
