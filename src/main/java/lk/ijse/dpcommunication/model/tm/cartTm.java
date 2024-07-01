package lk.ijse.dpcommunication.model.tm;

import lombok.*;

import java.text.DecimalFormat;
import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class cartTm {
    private String date;
    private String orderId;
    private String customerId;
    private String description;
    private int qty;
    private double unitPrice;
    private double total;


}
