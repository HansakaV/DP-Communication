package lk.ijse.dpcommunication.model;

import java.util.Date;

public class cost {
    private String date;
    private String description;
    private double value;

    public cost() {
    }

    public cost(String date, String description, double value) {
        this.date = date;
        this.description = description;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "cost{" +
                "date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", value=" + value +
                '}';
    }
}
