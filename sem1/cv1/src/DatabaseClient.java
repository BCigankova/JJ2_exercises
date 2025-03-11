import java.sql.*;
import java.util.LinkedList;

public class DatabaseClient {
    private static final String DB_NAME = "receiptdb";
    private static final String HOST = "localhost:1527";
    private Connection con;


    public void connect() throws SQLException {
        String connectionURL = "jdbc:derby://" + HOST + "/" + DB_NAME;
        con = DriverManager.getConnection(connectionURL);
    }


    public void close() throws SQLException {
        con.close();
    }


    public void listReceipts() throws SQLException {
        try (Statement stmt = con.createStatement()) {
            stmt.executeQuery("SELECT * FROM receipts");
            try (ResultSet results = stmt.getResultSet()) {
                while (results.next()) {
                    String itin = results.getString(1);
                    String name = results.getString(2);
                    int total = results.getInt(3);

                    System.out.println(itin + ", " + name + ", " + total + "\nitems:");

                    String query = "SELECT * FROM receiptItem WHERE receipt_itin = ?";  //lepsi pro sql injection? nebo neni potreba?
                    try (PreparedStatement itemStmt = con.prepareStatement(query)) {
                        itemStmt.setString(1, itin);
                        try (ResultSet itemResults = itemStmt.executeQuery()) {
                            while (itemResults.next()) {
                                String itemName = itemResults.getString(3);
                                int amount  = itemResults.getInt(4);
                                int price = itemResults.getInt(5);
                                System.out.println(itemName + ", " + amount + ", " + price);
                            }
                        }
                    }
                }
            }
        }
    }


    public void insertReceipt(String itin, String name, int total) throws SQLException {
        String query = "INSERT INTO receipts (itin, name, total) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, itin);
            stmt.setString(2, name);
            stmt.setInt(3, total);
            stmt.executeUpdate();
        }
    }

    public void insertReceiptItem(Receipt receipt, String name, int amount, int unitPrice) throws SQLException {
        String query = "INSERT INTO RECEIPTITEM (RECEIPT_ITIN, name, AMOUNT, UNIT_PRICE) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, receipt.getItin());
            stmt.setString(2, name);
            stmt.setInt(3, amount);
            stmt.setInt(3, unitPrice);
            stmt.executeUpdate();
        }
    }

    public void insertReceiptWithItems(String itin, String name, int total, LinkedList<ReceiptItem> items) throws SQLException {
        insertReceipt(itin, name, total);
        Receipt receipt = new Receipt(itin, name, total);
        for(ReceiptItem item: items)
            insertReceiptItem(receipt, item.getItemName(), item.getItemAmount(), item.getItemPrice());
    }

    public static void main(String[] args) {
        DatabaseClient client = new DatabaseClient();
        try {
            client.connect();
            client.listReceipts();
            client.insertReceipt("CZ130", "ABC", 123);
            client.listReceipts();
            LinkedList<ReceiptItem> items = new LinkedList<>();
            items.add(new ReceiptItem("item1", 1, 10));
            client.listReceipts();
            client.close();
        } catch (SQLException e) {
            System.out.println("ajaj");
            e.printStackTrace();
        }
    }

}