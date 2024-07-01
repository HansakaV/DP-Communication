package lk.ijse.dpcommunication.model;

public class InstanceFinancialDTO {
    private String customer_name;
    private String order_date;
    private  String item_name;
    private double unit_price;
    private double qty;
    private double cost;
    private  double received_money;
    private double arrears;
    private String status;

    public InstanceFinancialDTO() {
    }

    public InstanceFinancialDTO(String customer_name, String order_date, String item_name, double unit_price, double qty, double cost, double received_money, double arrears, String status) {
        this.customer_name = customer_name;
        this.order_date = order_date;
        this.item_name = item_name;
        this.unit_price = unit_price;
        this.qty = qty;
        this.cost = cost;
        this.received_money = received_money;
        this.arrears = arrears;
        this.status = status;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getReceived_money() {
        return received_money;
    }

    public void setReceived_money(double received_money) {
        this.received_money = received_money;
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
        return "InstanceFinancialDTO{" +
                "customer_name='" + customer_name + '\'' +
                ", order_date='" + order_date + '\'' +
                ", item_name='" + item_name + '\'' +
                ", unit_price=" + unit_price +
                ", qty=" + qty +
                ", cost=" + cost +
                ", received_money=" + received_money +
                ", arrears=" + arrears +
                ", status='" + status + '\'' +
                '}';
    }
}
