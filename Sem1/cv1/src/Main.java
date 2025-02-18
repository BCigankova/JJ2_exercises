import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        File filesource = new File("/home/barbora/Documents/Inf/2/LS/JJ2/Sem1/xml_source.txt");
        InputStream fileInput = new FileInputStream(filesource);

        Receipt receipt = new SAXReceiptReaderWriter().loadReceipt(fileInput);
        System.out.println("SAX");
        receipt.print();

        InputStream input = new ByteArrayInputStream("<?xml version=\"1.0\"?><receipt total=\"1642\"><name>ACME corp.</name><itin>CZ12345678</itin><items><item amount=\"50\" unitPrice=\"24\">Nitroglycerin</item><item amount=\"4\" unitPrice=\"100\">Jet Propelled Pogo Stick</item><item amount=\"1\" unitPrice=\"42\">Hen Grenade</item></items></receipt>".getBytes());

        receipt = new StAXReaderWriter().loadReceipt(input);
        System.out.println("\nStAX");
        receipt.print();

        File filewriteStAX = new File("/home/barbora/Documents/Inf/2/LS/JJ2/Sem1/StAXres.txt");
        OutputStream outputStAX = new FileOutputStream(filewriteStAX);
        new StAXReaderWriter().storeReceipt(outputStAX, receipt);

        input = new ByteArrayInputStream("<?xml version=\"1.0\"?><receipt total=\"1642\"><name>ACME corp.</name><itin>CZ12345678</itin><items><item amount=\"50\" unitPrice=\"24\">Nitroglycerin</item><item amount=\"4\" unitPrice=\"100\">Jet Propelled Pogo Stick</item><item amount=\"1\" unitPrice=\"42\">Hen Grenade</item></items></receipt>".getBytes());

        receipt = new DOMReceiptReaderWriter().loadReceipt(input);
        System.out.println("\nDom");
        receipt.print();

        File filewriteDOM = new File("/home/barbora/Documents/Inf/2/LS/JJ2/Sem1/DOMres.txt");
        OutputStream outputDOM = new FileOutputStream(filewriteDOM);
        new DOMReceiptReaderWriter().storeReceipt(outputDOM, receipt);
    }
}