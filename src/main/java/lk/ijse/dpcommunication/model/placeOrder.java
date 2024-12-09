package lk.ijse.dpcommunication.model;

import lk.ijse.dpcommunication.controller.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor

public class placeOrder {
    private order Placeorder;
    private List<orderDetail> odList;

}
