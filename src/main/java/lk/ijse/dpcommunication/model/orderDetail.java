package lk.ijse.dpcommunication.model;

public class orderDetail {
    private String orderId;
    private String description;
    private double unitPrice;
    private int qty;
    private double total;

    public orderDetail() {
    }

    public orderDetail(String orderId, String description, double unitPrice, int qty, double total) {
        this.orderId = orderId;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDescription() {
        return description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public double getTotal() {
        return total;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

