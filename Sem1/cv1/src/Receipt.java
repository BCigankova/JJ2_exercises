import java.util.LinkedList;
import java.util.List;

public class Receipt {
    private int total;
    private String name;
    private String itin;
    private List<ReceiptItem> items = new LinkedList<>();


    public void addItem(ReceiptItem item) {
        items.add(item);
    }

    public List<ReceiptItem> getItems() {
        return this.items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItin() {
        return itin;
    }

    public void setItin(String itin) {
        this.itin = itin;
    }

    public void print() {
        System.out.println("Company name: " + name + " itin: " + itin + " total: " + total);
        System.out.println("Items:");
        for(ReceiptItem item: items)
            item.print();
    }
}
