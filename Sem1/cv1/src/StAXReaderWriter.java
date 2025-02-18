import javax.xml.stream.*;
import java.io.*;

public class StAXReaderWriter implements ReceiptReaderWriter{
    private XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    private XMLStreamReader reader;

    private XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
    private XMLStreamWriter xmlWriter;

    @Override
    public Receipt loadReceipt(InputStream input) throws Exception {
        reader = xmlInputFactory.createXMLStreamReader(input);
        Receipt receipt = new Receipt();
        String buffer = "";
        ReceiptItem item = null;
        boolean done = false;

         while (!done) {
             switch (reader.getEventType()) {
                 case XMLStreamReader.START_ELEMENT:
                     if (reader.getName().toString().equals("receipt"))
                         receipt.setTotal(Integer.parseInt(reader.getAttributeValue(null, "total")));

                     if (reader.getName().toString().equals("item")) {

                         item = new ReceiptItem(Integer.parseInt(reader.getAttributeValue(null, "amount")), Integer.parseInt(reader.getAttributeValue(null, "unitPrice")));
                     }
                     break;

                 case XMLStreamReader.END_ELEMENT:
                     switch (reader.getName().toString()) {
                         case "name":
                             receipt.setName(buffer);
                             break;
                         case "itin":
                             receipt.setItin(buffer);
                             break;
                         case "item":
                             assert item != null;
                             item.setItemName(buffer);
                             receipt.addItem(item);
                             break;
                     }
                     break;

                 case XMLStreamReader.CHARACTERS:
                     buffer = reader.getText().trim();
                     break;
             }

             if (reader.hasNext()) {
                 reader.next();
             } else {
                 done = true;
             }
         }
        return receipt;
    }

    @Override
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception {
        xmlWriter = xmlOutputFactory.createXMLStreamWriter(output);

        xmlWriter.writeStartDocument();

        xmlWriter.writeStartElement("receipt");
        xmlWriter.writeAttribute("total", String.valueOf(receipt.getTotal()));

        xmlWriter.writeStartElement("name");
        xmlWriter.writeCharacters(receipt.getName());
        xmlWriter.writeEndElement();

        xmlWriter.writeStartElement("itin");
        xmlWriter.writeCharacters(receipt.getItin());
        xmlWriter.writeEndElement();

        xmlWriter.writeStartElement("items");
        for(ReceiptItem item: receipt.getItems()) {
            xmlWriter.writeStartElement("item");
            xmlWriter.writeAttribute("amount", String.valueOf(item.getItemAmount()));
            xmlWriter.writeAttribute("unitPrice", String.valueOf(item.getItemPrice()));
            xmlWriter.writeCharacters(item.getItemName());
            xmlWriter.writeEndElement();
        }

        xmlWriter.writeEndElement(); //items
        xmlWriter.writeEndElement();

        xmlWriter.writeEndDocument();
    }
}

