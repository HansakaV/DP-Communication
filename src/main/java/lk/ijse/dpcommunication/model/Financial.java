package lk.ijse.dpcommunication.model;

public class Financial {
    private String orderID;
    private String customerName;
    private String orderDate;
    private double cost;
    private double receivedMoney;
    private double arrears;
    private String status;

    public Financial() {
    }

    public Financial(String orderID, String customerName, String orderDate, double cost, double receivedMoney, double arrears, String status) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.cost = cost;
        this.receivedMoney = receivedMoney;
        this.arrears = arrears;
        this.status = status;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getReceivedMoney() {
        return receivedMoney;
    }

    public void setReceivedMoney(double receivedMoney) {
        this.receivedMoney = receivedMoney;
    }

    public double getArrears() {
        return arrears;
    }

    public void setArrears(double arrears) {
        this.arrears = arrears;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Financial{" +
                "orderID='" + orderID + '\'' +
                ", customerName='" + customerName + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", cost=" + cost +
                ", receivedMoney=" + receivedMoney +
                ", arrears=" + arrears +
                ", status='" + status + '\'' +
                '}';
    }
}
