import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXReceiptHandler extends DefaultHandler {
    private Receipt receipt;
    private ReceiptItem item;
    private String buffer;

    public SAXReceiptHandler(Receipt receipt) {
        this.receipt = receipt;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("receipt"))
            receipt.setTotal(Integer.parseInt(attributes.getValue("total")));

        if (qName.equals("item")) {
            item = new ReceiptItem((Integer.parseInt(attributes.getValue("amount"))), Integer.parseInt(attributes.getValue("unitPrice")));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "name":
                receipt.setName(buffer);
                break;
            case "itin":
                receipt.setItin(buffer);
                break;
            case "item":
                item.setItemName(buffer);
                receipt.addItem(item);
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        buffer = new String(ch, start, length).trim();
    }
}
