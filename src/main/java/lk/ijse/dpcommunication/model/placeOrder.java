package lk.ijse.dpcommunication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class placeOrder {
    private order Placeorder;
    private List<orderDetail> odList;
}
