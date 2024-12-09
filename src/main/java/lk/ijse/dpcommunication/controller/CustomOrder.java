package lk.ijse.dpcommunication.controller;

public class CustomOrder implements Order {
    private Long id;
    private String customerDetails;

    public CustomOrder(Long id, String customerDetails) {
        this.id = id;
        this.customerDetails = customerDetails;
    }

    @Override
    public Long getId() {
        return id;
    }

    // Additional methods specific to CustomOrder
    public String getCustomerDetails() {
        return customerDetails;
    }
}
