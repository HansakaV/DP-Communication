package lk.ijse.dpcommunication.model;

import java.util.Objects;

public class PayLaterFinancialDTO {
    private String date;
    private String order_id;
    private String customer_name;
    private String item_name;
    private double qty;
    private double unit_price;
    private double total;
    private String payment;
    private double updated_total;

    public PayLaterFinancialDTO() {
    }

    public PayLaterFinancialDTO(String date, String order_id, String customer_name, String item_name, double qty, double unit_price, double total, String payment, double updated_total) {
        this.date = date;
        this.order_id = order_id;
        this.customer_name = customer_name;
        this.item_name = item_name;
        this.qty = qty;
        this.unit_price = unit_price;
        this.total = total;
        this.payment = payment;
        this.updated_total = updated_total;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
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

    public double getUpdated_total() {
        return updated_total;
    }

    public void setUpdated_total(double updated_total) {
        this.updated_total = updated_total;
    }


    @Override
    public String toString() {
        return "PayLaterFinancialDTO{" +
                "date='" + date + '\'' +
                ", order_id='" + order_id + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", item_name='" + item_name + '\'' +
                ", qty=" + qty +
                ", unit_price=" + unit_price +
                ", total=" + total +
                ", payment='" + payment + '\'' +
                ", updated_total=" + updated_total +
                '}';
    }
}
