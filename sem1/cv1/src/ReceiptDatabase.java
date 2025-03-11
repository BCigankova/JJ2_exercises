import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;



/*
vypsat sql queries v ij
predelat do samostatneho rpjetu a dat PKs final a smazat settery
receipt tostring metoda,optional
lepsi testy?
 */


public class ReceiptDatabase implements AutoCloseable {

    private PreparedStatement getAllItins;//primary key
    private PreparedStatement getAllNames;
    private PreparedStatement getAllTotals;

    private PreparedStatement getReceiptByItin;


    private PreparedStatement insertReceipt;
    private PreparedStatement updateReceipt;
    private PreparedStatement deleteReceipt;  //pozor na foerigh key?

    private PreparedStatement getItemsForReceipt;
    private PreparedStatement insertReceiptItem;
    private PreparedStatement updateReceiptItem;
    private PreparedStatement deleteReceiptItem;
    private PreparedStatement deleteAllReceipts;
    private PreparedStatement deleteAllItems;
    private PreparedStatement getAllItems;




    public ReceiptDatabase(Connection con) throws ReceiptDBException {
        try {
            getAllItins = con.prepareStatement("SELECT itin FROM receipts");
            getAllNames = con.prepareStatement("SELECT name FROM receipts");
            getAllTotals = con.prepareStatement("SELECT total FROM receipts");
            getAllItems = con.prepareStatement("SELECT * FROM RECEIPTITEM");

            getReceiptByItin = con.prepareStatement("SELECT * FROM receipts WHERE itin = ?");

            insertReceipt = con.prepareStatement("INSERT INTO receipts (itin, name, total) VALUES (?, ?, ?)"); //dodelat: jak na reprezentaci vicero items?
            updateReceipt = con.prepareStatement("UPDATE receipts SET name = ?, total = ? WHERE itin = ?"); //dodelat, co updatuju?
            deleteReceipt = con.prepareStatement("DELETE FROM receipts WHERE itin = ?");

            getItemsForReceipt = con.prepareStatement("SELECT * FROM RECEIPTITEM WHERE receipt_itin = ?"); //dodelat
            insertReceiptItem = con.prepareStatement("INSERT INTO RECEIPTITEM (RECEIPT_ITIN, name, AMOUNT, unit_price) VALUES (?, ?, ?, ?)");
            updateReceiptItem = con.prepareStatement("UPDATE RECEIPTITEM SET name = ?, amount = ?, unit_price = ? WHERE receipt_itin = ?");
            deleteReceiptItem = con.prepareStatement("DELETE FROM RECEIPTITEM WHERE receipt_itin = ?");
            deleteAllReceipts = con.prepareStatement("DELETE FROM RECEIPTS");
            deleteAllItems = con.prepareStatement("DELETE FROM RECEIPTITEM");

        } catch (SQLException e) {
            throw new ReceiptDBException("Unable to initialize prepared statements.", e);
        }
    }

    public List<String> getAllItems() throws ReceiptDBException {
        List<String> items = new ArrayList<>();
        try (ResultSet results = getAllItems.executeQuery()) {
            while (results.next())
                items.add(results.getString(3));
        } catch (Exception e) {
            throw new ReceiptDBException("Unable to list all itins of receipts", e);
        }
        return items;
    }

    public List<String> getReceiptItins() throws ReceiptDBException {
        List<String> itins = new ArrayList<>();
        try (ResultSet results = getAllItins.executeQuery()) {
            while (results.next())
                itins.add(results.getString(1));
        } catch (Exception e) {
            throw new ReceiptDBException("Unable to list all itins of receipts", e);
        }
        return itins;
    }

    public List<String> getReceiptNames() throws ReceiptDBException {
        List<String> names = new ArrayList<>();
        try (ResultSet results = getAllNames.executeQuery()) {
            while (results.next())
                names.add(results.getString(1));
        } catch (Exception e) {
            throw new ReceiptDBException("Unable to list all names of companies", e);
        }
        return names;
    }

    public List<Integer> getReceiptTotals() throws ReceiptDBException {
        List<Integer> totals = new ArrayList<>();
        try (ResultSet results = getAllTotals.executeQuery()) {
            while (results.next())
                totals.add(results.getInt(1));
        } catch (Exception e) {
            throw new ReceiptDBException("Unable to list all totals", e);
        }
        return totals;
    }

    public Optional<Integer> getSumTotals() throws ReceiptDBException {
        return (getReceiptTotals().stream().reduce(Integer::sum));
    }

    public Receipt getReceiptByItin(String itin) throws ReceiptDBException {
        Receipt receipt = null;
        try {
            getReceiptByItin.setString(1, itin);
            try (ResultSet results = getReceiptByItin.executeQuery()) {
                if(results.next())
                    receipt = new Receipt(results.getString(1), results.getString(2), results.getInt(3));
            } catch (Exception e) {
                throw new ReceiptDBException("Unable to get receipt", e);
            }
        } catch (Exception e) {
            throw new ReceiptDBException("Unable to get receipt", e);
        }
        return receipt;
    }

    /*public Receipt getReceiptsWithItem(String name) throws ReceiptDBException {
        Receipt item = null;
        try {
            getItemsByName.setString(1, name);
            try (ResultSet results = getItemsByName.executeQuery()) {
                while(results.next())
                    item = new ReceiptItem(results.getString(1), results.getInt(2), results.getInt(3));
            } catch (Exception e) {
                throw new ReceiptDBException("Unable to get item with name" + name, e);
            }
        } catch (Exception e) {
            throw new ReceiptDBException("Unable to get item with name" + name, e);
        }
        return item;
    }



    public int getAmount(String name) throws ReceiptDBException {
        try {
            getItemsByName.setString(1, name);
            try (ResultSet results = getItemsByName.executeQuery()) {
                if(results.next())
                    return results.getInt(2);
            } catch (Exception e) {
                throw new ReceiptDBException("Unable to get item with name" + name, e);
            }
        } catch (Exception e) {
            throw new ReceiptDBException("Unable to get item with name" + name, e);
        }
        return -1;
    }

    public int getPrice(String name) throws ReceiptDBException {
        try {
            getItemsByName.setString(1, name);
            try (ResultSet results = getItemsByName.executeQuery()) {
                if(results.next())
                    return results.getInt(3);
            } catch (Exception e) {
                throw new ReceiptDBException("Unable to get item with name" + name, e);
            }
        } catch (Exception e) {
            throw new ReceiptDBException("Unable to get item with name" + name, e);
        }
        return -1;
    }
    */

    public Receipt createReceipt(String itin, String name, int total) throws ReceiptDBException {
        try {
            insertReceipt.setString(1, itin);
            insertReceipt.setString(2, name);
            insertReceipt.setInt(3, total);
            insertReceipt.executeUpdate();

            return new Receipt(itin, name, total);
        } catch (SQLException e) {
            throw new ReceiptDBException("Unable to create new receipt", e);
        }
    }

    public void commitChangesReceipt(Receipt receipt) throws ReceiptDBException {
        try {
            updateReceipt.setString(3, receipt.getItin());
            updateReceipt.setString(1, receipt.getName());
            updateReceipt.setInt(2, receipt.getTotal());
            updateReceipt.executeUpdate();
        } catch (SQLException e) {
            throw new ReceiptDBException("Unable to update receipt", e);
        }
    }

    public void removeReceipt(Receipt receipt) throws ReceiptDBException {
        try {
            deleteReceipt.setString(1, receipt.getItin());
            deleteReceiptItem.setString(1, receipt.getItin());
            deleteReceiptItem.executeUpdate();
            deleteReceipt.executeUpdate();
        } catch (SQLException e) {
            throw new ReceiptDBException("Unable to delete receipt", e);
        }
    }

    public List<ReceiptItem> getItemsForReceipt(Receipt receipt) throws ReceiptDBException {
        List<ReceiptItem> items = new LinkedList<>();
        try {
            getItemsForReceipt.setString(1, receipt.getItin());
            try (ResultSet results = getItemsForReceipt.executeQuery()) {
                while (results.next()) {
                    items.add(new ReceiptItem(results.getString(3), results.getInt(4), results.getInt(5)));
                }
            } catch (Exception e) {
                throw new ReceiptDBException("Unable to list all items of receipt", e);
            }
        } catch (Exception e) {
            throw new ReceiptDBException("Unable to list all items of receipt", e);
        }
        return items;
    }

    public void createReceiptItem(Receipt receipt, String name, int amount, int unitPrice) throws ReceiptDBException {
        try {
            insertReceiptItem.setString(1, receipt.getItin());
            insertReceiptItem.setString(2, name);
            insertReceiptItem.setInt(3, amount);
            insertReceiptItem.setInt(4, unitPrice);
            insertReceiptItem.executeUpdate();
        } catch (SQLException e) {
            throw new ReceiptDBException("Unable to create new item", e);
        }
    }

    public void commitChangesReceiptItem(Receipt receipt, ReceiptItem item) throws ReceiptDBException {
        try {
            updateReceiptItem.setString(1, item.getItemName());
            updateReceiptItem.setInt(2, item.getItemAmount());
            updateReceiptItem.setInt(3, item.getItemPrice());
            updateReceiptItem.setString(4, receipt.getItin());
            updateReceiptItem.executeUpdate();
        } catch (SQLException e) {
            throw new ReceiptDBException("Unable to update item", e);
        }
    }

    public void removeReceiptItems(Receipt receipt) throws ReceiptDBException {
        try {
            deleteReceiptItem.setString(1, receipt.getItin());
            deleteReceiptItem.executeUpdate();
        } catch (SQLException e) {
            throw new ReceiptDBException("Unable to delete item", e);
        }
    }

    public void insertAllItemsForReceipt(Receipt receipt, List<ReceiptItem> items) throws ReceiptDBException {
        try {
            for(ReceiptItem item: items) {
                createReceiptItem(receipt, item.getItemName(), item.getItemAmount(), item.getItemPrice());
                insertReceiptItem.setString(1, receipt.getItin());
                insertReceiptItem.setString(2, item.getItemName());
                insertReceiptItem.setInt(3, item.getItemAmount());
                insertReceiptItem.setInt(4, item.getItemPrice());
                insertReceiptItem.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ReceiptDBException("Unable to insert items", e);
        }
    }

    public Receipt createReceiptWithItems(String itin, String name, int total, List<ReceiptItem> items) throws ReceiptDBException {
        Receipt receipt = createReceipt(itin, name, total);
        for(ReceiptItem item:items)
            receipt.addItem(item);
        for(ReceiptItem item: items) {
            createReceiptItem(receipt, item.getItemName(), item.getItemAmount(), item.getItemPrice());
//                insertReceiptItem.setString(1, itin);
//                insertReceiptItem.setString(2, item.getItemName());
//                insertReceiptItem.setInt(3, item.getItemAmount());
//                insertReceiptItem.setInt(4, item.getItemPrice());
//                insertReceiptItem.executeUpdate();
        }
        return receipt;
    }

    public void deleteAll() throws ReceiptDBException {
        try {
            deleteAllItems.executeUpdate();
            try {
                deleteAllReceipts.executeUpdate();
            } catch (SQLException e) {
                throw new ReceiptDBException("Unable to delete all receipts", e);
            }
        } catch (SQLException e) {
            throw new ReceiptDBException("Unable to delete all itins of receipts", e);
        }
    }

    @Override
    public void close() {
        try {
             getAllItins.close();
             getAllNames.close();
             getAllTotals.close();

             getReceiptByItin.close();

             insertReceipt.close();
             updateReceipt.close();
             deleteReceipt.close();

             getItemsForReceipt.close();
             insertReceiptItem.close();
             updateReceiptItem.close();
             deleteReceiptItem.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}