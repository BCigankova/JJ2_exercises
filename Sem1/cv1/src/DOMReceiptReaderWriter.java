import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class DOMReceiptReaderWriter implements ReceiptReaderWriter{
    private DocumentBuilder documentBuilder;

    public DOMReceiptReaderWriter() throws ParserConfigurationException {
        this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }
    @Override
    public Receipt loadReceipt(InputStream input) throws Exception {
        Receipt receipt = new Receipt();

        Document doc = documentBuilder.parse(input);

        Element root = doc.getDocumentElement();
        Node att = root.getAttributes().getNamedItem("total");
        receipt.setTotal(Integer.parseInt(att.getTextContent()));

        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            Node node = root.getChildNodes().item(i);  //v node ja aktualne zpracovana noda, name, itin nebo items
            switch(node.getNodeName()) {
                case "name":
                    receipt.setName(node.getTextContent());
                    break;
                case "itin":
                    receipt.setItin(node.getTextContent());
                    break;
                case "items":
                    for(int j = 0; j < node.getChildNodes().getLength(); j++) {
                        Node itemnode = node.getChildNodes().item(j); //v itemnode jsou ted itemy pod items
                        ReceiptItem item = new ReceiptItem(Integer.parseInt(itemnode.getAttributes().getNamedItem("amount").getTextContent()), Integer.parseInt(itemnode.getAttributes().getNamedItem("unitPrice").getTextContent()));
                        item.setItemName(itemnode.getTextContent());
                        receipt.addItem(item);
                    }
                    break;
            }

        }
        return receipt;
    }

    @Override
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception {
        Document doc = documentBuilder.newDocument();

        Element rootElement = doc.createElement("receipt");
        rootElement.setAttribute("total", String.valueOf(receipt.getTotal()));
        doc.appendChild(rootElement);

        Element childNode1 = doc.createElement("name");
        childNode1.setTextContent(receipt.getName());
        rootElement.appendChild(childNode1);

        Element childNode2 = doc.createElement("itin");
        childNode2.setTextContent(receipt.getItin());
        rootElement.appendChild(childNode2);

        Element childNode3 = doc.createElement("items");
        rootElement.appendChild(childNode3);

        for(ReceiptItem item: receipt.getItems()) {
            Element childNode4 = doc.createElement("item");
            childNode4.setAttribute("amount", String.valueOf(item.getItemAmount()));
            childNode4.setAttribute("unitPrice", String.valueOf(item.getItemPrice()));
            childNode4.setTextContent(item.getItemName());
            childNode3.appendChild(childNode4);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);

        //samostatna funkce?

    }
}
