package lk.ijse.dpcommunication.controller;

public class NormalOrder implements Order{
    private Long id;
    private int quantity;

    public NormalOrder(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    @Override
    public Long getId() {
        return id;
    }

    // Additional methods specific to NormalOrder
    public int getQuantity() {
        return quantity;
    }
}
