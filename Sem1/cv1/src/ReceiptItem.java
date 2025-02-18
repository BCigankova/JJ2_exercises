public class ReceiptItem {
    private String itemName;
    private int itemPrice;
    private int itemAmount;

    public ReceiptItem(int itemAmount, int itemPrice) {
        this.itemPrice = itemPrice;
        this.itemAmount = itemAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public void print() {
        System.out.println("Name: " + itemName + " Amount: " + itemAmount + " Unit price: " + itemPrice);
    }
}