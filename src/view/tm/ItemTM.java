package view.tm;

public class ItemTM implements Comparable<ItemTM>{
    private String itemCode;
    private String discription;
    private String packSize;
    private double unitPrice;
    private int qtyOnHand;

    public ItemTM() {
    }

    public ItemTM(String itemCode, String discription, String packSize, double unitPrice, int qtyOnHand) {
        this.itemCode = itemCode;
        this.discription = discription;
        this.packSize = packSize;
        this.unitPrice = unitPrice;
        this.qtyOnHand = qtyOnHand;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    @Override
    public String toString() {
        return "ItemTM{" +
                "itemCode='" + itemCode + '\'' +
                ", discription='" + discription + '\'' +
                ", packSize='" + packSize + '\'' +
                ", unitPrice=" + unitPrice +
                ", qtyOnHand=" + qtyOnHand +
                '}';
    }

    @Override
    public int compareTo(ItemTM o) {
        return itemCode.compareTo(o.getItemCode());
    }
}
