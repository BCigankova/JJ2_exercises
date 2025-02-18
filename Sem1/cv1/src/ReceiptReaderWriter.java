import java.io.*;
public interface ReceiptReaderWriter {
    /**
     * Nacte ze streamu XML soubor a dle nej vytvori
     * prislusny objekt reprezentujici uctenku
     */
    public Receipt loadReceipt(InputStream input)
            throws Exception;

    /**
     * Ulozi do prislusneho streamu XML soubor
     * predstavujici danou uctenku
     */
    public void storeReceipt(OutputStream output,
                             Receipt receipt)
            throws Exception;
}
