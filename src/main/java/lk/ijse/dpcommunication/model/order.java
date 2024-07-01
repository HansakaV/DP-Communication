package lk.ijse.dpcommunication.model;

public class order {
    private String date;
    private String orderId;
    private String customerId;
    private String description;
    private int qty;
    private double unitPrice;
    private double total;
    private String payment;
    private double updatedTotal;

    public order() {
    }

    public order(String date, String orderId, String customerId, String description, int qty, double unitPrice, double total, String payment, double updatedTotal) {
        this.date = date;
        this.orderId = orderId;
        this.customerId = customerId;
        this.description = description;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.total = total;
        this.payment = payment;
        this.updatedTotal = updatedTotal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public double getUpdatedTotal() {
        return updatedTotal;
    }

    public void setUpdatedTotal(double updatedTotal) {
        this.updatedTotal = updatedTotal;
    }

    @Override
    public String toString() {
        return "order{" +
                "date='" + date + '\'' +
                ", orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", description='" + description + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                ", total=" + total +
                ", payment='" + payment + '\'' +
                ", updatedTotal=" + updatedTotal +
                '}';
    }
}


