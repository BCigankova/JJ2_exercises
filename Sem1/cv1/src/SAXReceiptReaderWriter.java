import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.io.OutputStream;

public class SAXReceiptReaderWriter implements ReceiptReaderWriter{
    private SAXParser parser;

    public SAXReceiptReaderWriter() throws ParserConfigurationException, SAXException {
        this.parser = SAXParserFactory.newInstance().newSAXParser();
    }

    @Override
    public Receipt loadReceipt(InputStream input) throws Exception {
        Receipt receipt = new Receipt();
        parser.parse(input, new SAXReceiptHandler(receipt));
        return receipt;
    }

    @Override
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception {
        throw new Exception("Unable to store receipt with SAX");
    }
}
