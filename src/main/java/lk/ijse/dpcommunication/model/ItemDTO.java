package lk.ijse.dpcommunication.model;

public class ItemDTO {
    private String orderDate;
    private String itemName;
    private String status;
    private double unitPrice;
    private int qty;
    private double total;
    private String payDate;
    private double receivedMoney;

    // Constructors, getters, and setters


    public ItemDTO() {
    }

    public ItemDTO(String orderDate, String itemName, String status, double unitPrice, int qty, double total, String payDate, double receivedMoney) {
        this.orderDate = orderDate;
        this.itemName = itemName;
        this.status = status;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.total = total;
        this.payDate = payDate;
        this.receivedMoney = receivedMoney;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public double getReceivedMoney() {
        return receivedMoney;
    }

    public void setReceivedMoney(double receivedMoney) {
        this.receivedMoney = receivedMoney;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "orderDate='" + orderDate + '\'' +
                ", itemName='" + itemName + '\'' +
                ", status='" + status + '\'' +
                ", unitPrice=" + unitPrice +
                ", qty=" + qty +
                ", total=" + total +
                ", payDate='" + payDate + '\'' +
                ", receivedMoney=" + receivedMoney +
                '}';
    }
}
